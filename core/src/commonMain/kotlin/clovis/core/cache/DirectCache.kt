package clovis.core.cache

import clovis.core.Identifiable
import clovis.core.Provider
import clovis.core.Result
import kotlinx.coroutines.flow.flow

/**
 * A no-op implementation of the [Cache] interface.
 *
 * This implementation can be used as a bridge between more useful cache implementations and a [Provider].
 */
class DirectCache<Id : IdBound, O : Identifiable<Id>>(
	private val provider: Provider<Id, O>,
) : Cache<Id, O> {

	override fun get(id: Id): CacheResult<Id, O> = flow {
		// Emit a 'loading' value instantly
		emit(Result.Loading(id, lastKnownValue = null))

		// Emit the value from the requested when it is available
		emit(provider.request(id))
	}

	override suspend fun updateAll(values: Collection<O>) = Unit

	override suspend fun expire(id: Id) = Unit
}
