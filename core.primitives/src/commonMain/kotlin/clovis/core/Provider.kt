package clovis.core

import clovis.core.cache.Cache
import kotlinx.coroutines.flow.Flow

/**
 * Common interface for API endpoints.
 *
 * A [Provider] represents all endpoints for a specific data type (for example, a wallet).
 * Each method represents a different endpoint to implement actions on the relevant objects (list all objects, create a new one…).
 *
 * The [Provider] interface only enforces two common contracts:
 * - [directRequest]: How to query an object from the backend from its ID (see [Ref]).
 * - [cache]: How to temporarily store fetched data on the client-side, to decrease the number of requests.
 *
 * Apart from the [directRequest] method, no method should return an API object.
 * Instead, all methods should return a [reference][Ref] on the object, so each reference can be requested individually through the cache.
 */
interface Provider<R : Ref<R, O>, O> {

	val cache: Cache<R, O>

	/**
	 * Requests data directly from the external resource (bypasses the [cache]).
	 *
	 * The [Flow] returned by this method is short-lived: it will stop emitting as soon as the first non-loading value is found.
	 * If the same request is done in the future, flows previously returned by this method will not be updated.
	 */
	fun directRequest(ref: R): Flow<Progress<R, O>>

	/**
	 * Decodes a [String] to a [Ref].
	 *
	 * The format requirements are explained in [Ref.encodeRef]'s documentation.
	 */
	fun decodeRef(encoded: String): R
}
