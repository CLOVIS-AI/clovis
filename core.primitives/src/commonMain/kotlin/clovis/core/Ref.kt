package clovis.core

import clovis.core.cache.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * A reference to a specific [object][O].
 *
 * @param O The type of the object being referenced.
 */
interface Ref<Self : Ref<Self, O>, O> {

	/**
	 * Requests data directly from the external resource.
	 *
	 * Unlike [request], this method does not go through the [provider]'s [cache][Provider.cache], nor does it update it.
	 *
	 * The [Flow] returned by this method is short-lived: it will stop emitting as soon as the first non-loading value is found.
	 * If the same request is done in the future, flows previously returned by this method will not be updated.
	 */
	fun directRequest(): Flow<Progress<Self, O>>

	/**
	 * The [Provider] responsible for this reference.
	 */
	val provider: Provider<Self, O>

}

//region Extensions

/**
 * Requests the data referenced by this [Ref].
 *
 * For performance reasons, the data is queried through the [Ref.provider]'s [cache][Provider.cache].
 * To query the data directly, use [Ref.directRequest].
 *
 * The [Flow] returned by this method is long-lived; see [Cache.get].
 */
fun <R, O> R.request() where R : Ref<R, O> =
	provider.cache[this]
		.distinctUntilChanged()

suspend fun <R, O> R.expire() where R : Ref<R, O> =
	provider.cache.expire(this)

suspend fun <R, O> R.update(value: O) where R : Ref<R, O> =
	provider.cache.update(this, value)

//endregion
