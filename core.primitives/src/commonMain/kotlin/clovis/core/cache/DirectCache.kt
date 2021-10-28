package clovis.core.cache

import clovis.core.Progress
import clovis.core.Ref
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

/**
 * A no-op implementation of the [Cache] interface.
 *
 * Note that unlike all other [Cache] implementations, [get] is a short-lived flow.
 */
class DirectCache<R : Ref<R, O>, O> : Cache<R, O> {

	/**
	 * A short-lived flow of the results of querying the [ref].
	 *
	 * See [Cache.get].
	 */
	override fun get(ref: R): CacheResult<R, O> = flow {
		// Emit a 'loading' value instantly
		emit(Progress.Loading(ref, lastKnownValue = null))

		// Emit the value from the requested when it is available
		emitAll(ref.provider.directRequest(ref))
	}

	override suspend fun updateAll(values: Iterable<Pair<R, O>>) = Unit

	override suspend fun expire(ref: R) = Unit
}
