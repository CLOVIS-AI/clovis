package clovis.core.cache

import clovis.core.Progress
import clovis.core.Ref
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
class MemoryCache<O> constructor(
	private val upstream: Cache<O>,
	private val scope: CoroutineScope,
	private val staleAfter: Duration = Duration.minutes(3),
	private val clearAfter: Duration = Duration.minutes(30),
) : Cache<O> {

	private val data = HashMap<Ref<O>, MutableStateFlow<CacheEntry<O>>>()
	private val lock = Semaphore(1)

	init {
		scope.launch {
			while (true) {
				lock.withPermit {
					val i = data.iterator()

					while (i.hasNext()) {
						val (_, element) = i.next()

						if (element.value.timestamp + clearAfter <= Clock.System.now())
							i.remove()
					}
				}

				delay(clearAfter)
			}
		}
	}

	private fun unsafeGet(ref: Ref<O>) = data.getOrPut(ref) {
		MutableStateFlow(CacheEntry(null))
	}

	private fun requestProvider(element: MutableStateFlow<CacheEntry<O>>, ref: Ref<O>) {
		require(scope.isActive) { "This cache currently doesn't have an active job, which makes it unable to function properly." }

		// Start of the request, set state to 'loading'
		val loader = Progress.Loading(ref, lastKnownValue = element.value.obj as? Progress.Result<O>)
		element.value = CacheEntry(loader)

		scope.launch {
			// Catch exceptions, just in case the provider is not implemented correctly
			try {
				// Query the provider
				upstream[ref]
					.onEach { element.value = CacheEntry(it) } // Update the state
					.first { it !is Progress.Loading } // When we find something else than Loading, we can unsubscribe
			} catch (e: Exception) {
				element.value = CacheEntry(null)
				throw IllegalStateException("The provider threw an exception. This should never happen.", e)
			}
		}
	}

	override fun get(ref: Ref<O>): CacheResult<O> = flow {
		emit(Progress.Loading(ref, lastKnownValue = null))

		emitAll(lock.withPermit {
			val element = unsafeGet(ref)

			element
				.onEach {
					if (it.obj == null || it.timestamp + staleAfter <= Clock.System.now())
						requestProvider(element, ref)
				}
				.map { it.obj } // If a property of the cached entry changes, but the object doesn't, no need to update the subscribers
				.distinctUntilChanged()
				.filterNotNull()
		})
	}

	override suspend fun updateAll(values: Iterable<Pair<Ref<O>, O>>) {
		lock.withPermit {
			for ((id, value) in values) {
				unsafeGet(id).value = CacheEntry(Progress.Success(id, value))
			}
		}
	}

	override suspend fun expire(ref: Ref<O>) {
		upstream.expire(ref)
		lock.withPermit {
			val element = unsafeGet(ref)
			element.value = CacheEntry(element.value.obj, Instant.DISTANT_PAST)
		}
	}
}

private data class CacheEntry<O>(
	val obj: Progress<O>?,
	val timestamp: Instant = Clock.System.now(),
)
