package clovis.core.cache

import clovis.core.Identifiable
import clovis.core.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class MemoryCache<Id : IdBound, O : Identifiable<Id>> constructor(
	private val upstream: Cache<Id, O>,
	private val scope: CoroutineScope,
	private val staleAfter: Duration = Duration.minutes(3),
	private val expiredAfter: Duration = Duration.minutes(30),
) : Cache<Id, O> {

	private val data = HashMap<Id, MutableStateFlow<CacheEntry<Id, O>>>()
	private val lock = Semaphore(1)

	init {
		scope.launch {
			while (true) {
				lock.withPermit {
					val i = data.iterator()

					while (i.hasNext()) {
						val (_, element) = i.next()

						if (element.value.timestamp + expiredAfter <= Clock.System.now())
							i.remove()
					}
				}

				delay(expiredAfter)
			}
		}
	}

	private fun unsafeGet(id: Id) = data.getOrPut(id) {
		MutableStateFlow(CacheEntry(null))
	}

	private fun requestProvider(element: MutableStateFlow<CacheEntry<Id, O>>, id: Id) {
		require(scope.isActive) { "This cache currently doesn't have an active job, which makes it unable to function properly." }

		// Start of the request, set state to 'loading'
		val loader = Result.Loading(id, lastKnownValue = element.value.obj)
		element.value = CacheEntry(loader)

		scope.launch {
			// Catch exceptions, just in case the provider is not implemented correctly
			try {
				// Query the provider
				upstream[id]
					.onEach { element.value = CacheEntry(it) } // Update the state
					.first { it !is Result.Loading } // When we find something else than Loading, we can unsubscribe
			} catch (e: Exception) {
				element.value = CacheEntry(null)
				throw IllegalStateException("The provider threw an exception. This should never happen.", e)
			}
		}
	}

	override fun get(id: Id): CacheResult<Id, O> = flow {
		emit(Result.Loading(id, lastKnownValue = null))

		emitAll(lock.withPermit {
			val element = unsafeGet(id)

			element
				.onEach {
					if (it.obj == null || it.timestamp + staleAfter <= Clock.System.now())
						requestProvider(element, id)
				}
				.map { it.obj } // If a property of the cached entry changes, but the object doesn't, no need to update the subscribers
				.distinctUntilChanged()
				.filterNotNull()
		})
	}

	override suspend fun updateAll(values: Collection<O>) {
		lock.withPermit {
			for (value in values) {
				unsafeGet(value.id).value = CacheEntry(Result.Success(value))
			}
		}
	}

	override suspend fun expire(id: Id) {
		lock.withPermit {
			val element = unsafeGet(id)
			element.value = CacheEntry(element.value.obj, Instant.DISTANT_PAST)
		}
	}
}

private data class CacheEntry<Id : IdBound, O : Identifiable<Id>>(
	val obj: Result<Id, O>?,
	val timestamp: Instant = Clock.System.now(),
)
