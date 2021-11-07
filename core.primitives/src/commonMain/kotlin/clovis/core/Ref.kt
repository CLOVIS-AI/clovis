package clovis.core

import clovis.core.cache.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * A reference to a specific [object][O].
 *
 * A reference is a small object that allows to pass around an object from an API without querying it.
 * Multiple properties and methods are available:
 * - [provider] is the [Provider] implementation responsible for the referenced object,
 * - [request] to query for this object (might be intercepted the provider's [cache][Provider.cache]),
 * - [directRequest] to query to this object directly (bypasses the provider's cache),
 *
 * The aim is that implementations implement a simple class that stores some kind of ID as well as the [provider] responsible for querying and operating on the matching data.
 *
 * @param Self The class that implements this [Ref]
 * @param O The type of the object being referenced.
 */
interface Ref<Self : Ref<Self, O>, O> {

	/**
	 * The [Provider] responsible for this reference.
	 */
	val provider: Provider<Self, O>

	/**
	 * Encodes this [Ref] as a [String].
	 *
	 * This is used to transmit the reference to other machines when using remote providers.
	 * The returned [String] can be in whatever format the implementer likes, as long as:
	 * - the exact same format can be given to [Provider.decodeRef] to construct a reference identical to the one
	 * passed as parameter to [encodeRef] (identical according to [Any.equals]),
	 * - it does not contain any secret information (it might be sent to another machine).
	 */
	fun encodeRef(): String
}

//region Extensions

/**
 * Requests the data referenced by this [Ref].
 *
 * For performance reasons, the data is queried through the [Ref.provider]'s [cache][Provider.cache].
 * To query the data directly, use [directRequest].
 *
 * The [Flow] returned by this method is long-lived; see [Cache.get].
 */
fun <R, O> R.request() where R : Ref<R, O> =
	provider.cache[this]
		.distinctUntilChanged()

/**
 * Requests the data referenced by this [Ref] directly.
 *
 * Unlike [request], this call does *not* go through the [Ref.provider]'s [cache][Provider.cache].
 *
 * The [Flow] returned by this method short-lived; see [Provider.directRequest].
 */
fun <R, O> R.directRequest() where R : Ref<R, O> =
	provider.directRequest(this)

suspend fun <R, O> R.expire() where R : Ref<R, O> =
	provider.cache.expire(this)

suspend fun <R, O> R.update(value: O) where R : Ref<R, O> =
	provider.cache.update(this, value)

//endregion
