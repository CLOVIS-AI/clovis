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
interface Provider<O> {

	val cache: Cache<O>

	/**
	 * Requests data directly from the external resource (bypasses the [cache]).
	 *
	 * The [Flow] returned by this method is short-lived: it will stop emitting as soon as the first non-loading value is found.
	 * If the same request is done in the future, flows previously returned by this method will not be updated.
	 *
	 * The [ref] provided to this method must be of a type accepted by this provider.
	 * To decrease the chance of providing the wrong type of reference, use [Ref.directRequest] instead,
	 * which selects the correct provider automatically.
	 */
	fun directRequest(ref: Ref<O>): Flow<Progress<O>>

	/**
	 * Decodes a [String] to a [Ref].
	 *
	 * The format requirements are explained in [Ref.encodeRef]'s documentation.
	 * A reference returned by this method MUST be a valid parameter to this provider's [directRequest] method.
	 */
	fun decodeRef(encoded: String): Ref<O>
}
