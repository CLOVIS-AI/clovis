package clovis.core.cache

import clovis.core.Id
import clovis.core.Provider
import clovis.core.Result
import kotlinx.coroutines.flow.flow

/**
 * A no-op implementation of the [Cache] interface.
 *
 * This implementation can be used as a bridge between more useful cache implementations and a [Provider].
 */
class DirectCache<I : Id<O>, O>(
	private val provider: Provider<I, O>,
) : Cache<I, O> {

	override fun get(id: I): CacheResult<I, O> = flow {
		// Emit a 'loading' value instantly
		emit(Result.Loading(id, lastKnownValue = null))

		// Emit the value from the requested when it is available
		emit(provider.request(id))
	}

	override suspend fun updateAll(values: Collection<Pair<I, O>>) = Unit

	override suspend fun expire(id: I) = Unit
}
