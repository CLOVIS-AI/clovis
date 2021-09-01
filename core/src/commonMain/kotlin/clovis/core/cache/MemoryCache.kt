package clovis.core.cache

import clovis.core.Id
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
class MemoryCache<I : Id, O : Identifiable<I>> constructor(
	private val upstream: Cache<I, O>,
	private val scope: CoroutineScope,
	private val staleAfter: Duration = Duration.minutes(3),
	private val expiredAfter: Duration = Duration.minutes(30),
) : Cache<I, O> {

	private val data = HashMap<I, MutableStateFlow<CacheEntry<I, O>>>()
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

	private fun unsafeGet(id: I) = data.getOrPut(id) {
		MutableStateFlow(CacheEntry(null))
	}

	private fun requestProvider(element: MutableStateFlow<CacheEntry<I, O>>, id: I) {
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

	override fun get(id: I): CacheResult<I, O> = flow {
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

	override suspend fun expire(id: I) {
		lock.withPermit {
			val element = unsafeGet(id)
			element.value = CacheEntry(element.value.obj, Instant.DISTANT_PAST)
		}
	}
}

private data class CacheEntry<Id : clovis.core.Id, O : Identifiable<Id>>(
	val obj: Result<Id, O>?,
	val timestamp: Instant = Clock.System.now(),
)
