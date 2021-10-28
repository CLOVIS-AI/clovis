package clovis.core

import clovis.core.cache.Cache
import kotlinx.coroutines.flow.Flow

interface Provider<R : Ref<R, O>, O> {

	val cache: Cache<R, O>

	/**
	 * Requests data directly from the external resource (bypasses the [cache]).
	 *
	 * The [Flow] returned by this method is short-lived: it will stop emitting as soon as the first non-loading value is found.
	 * If the same request is done in the future, flows previously returned by this method will not be updated.
	 */
	fun directRequest(ref: R): Flow<Progress<R, O>>

}
