package clovis.core.cache

import clovis.core.Progress
import clovis.core.Ref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

typealias CacheResult<O> = Flow<Progress<O>>
typealias MutableCacheResult<O> = MutableStateFlow<Progress<O>>

/**
 * Store information in a cheaply accessible medium, to reduce the cost of network requests.
 *
 * Cache implementations make accessing data faster than querying a backend, because data that is often used can be queried once and reused many times.
 *
 * Data in a cache can have three states:
 * - Up-to-date, in which case the cache gives the value immediately and performs no further operations,
 * - Stale, in which case the cache gives the stale value and asynchronously queries a fresh one,
 * - Expired, in which case the cache removes the value.
 *
 * For a user interface, these mean that:
 * - Up-to-date: the correct value is available immediately (a [Result] different from [Progress.Loading]),
 * - Stale: an old value is available immediately, which will be replaced by an up-to-date version as soon as it is available ([Progress.Loading] with a [Progress.Loading.lastKnownValue]),
 * - Expired: no values are available ([Progress.Loading] without a [Progress.Loading.lastKnownValue]).
 */
interface Cache<O> {

	/**
	 * Get the object identified by the provided [ref].
	 *
	 * Depending on the implementation, this may or may not use local memory, storage, or perform a network request.
	 *
	 * Unlike [Ref.directRequest], the [Flow] returned by this method is long-lived:
	 * even if the object is [updated][update] multiple times, the flow will continue notifying its subscribers of the new values.
	 */
	operator fun get(ref: Ref<O>): CacheResult<O>

	/**
	 * Provide a [value] to the cache as up-to-date information.
	 *
	 * You can use this method to feed data to the cache, when you have a more efficient way to get it than it has;
	 * for example if your API returns nested objects.
	 *
	 * When multiple cache layers are used, only the layer this is called on is updated.
	 *
	 * @see updateAll
	 */
	suspend fun update(ref: Ref<O>, value: O) = updateAll(listOf(ref to value))

	/**
	 * Provide multiple [values] to the cache as up-to-date information.
	 *
	 * @see update
	 */
	suspend fun updateAll(values: Iterable<Pair<Ref<O>, O>>)

	/**
	 * Communicates to the cache that its value is out-of-date, and should be queried again.
	 *
	 * On the next [get] call, the cache will start querying for a new value.
	 *
	 * When multiple cache layers are used, they are all expired.
	 *
	 * @see forceRefresh
	 */
	suspend fun expire(ref: Ref<O>)

	/**
	 * Gets a value that is guaranteed to be up-to-date.
	 *
	 * Convenience method for [expire] followed by [get].
	 */
	suspend fun forceRefresh(ref: Ref<O>): CacheResult<O> {
		expire(ref)
		return get(ref)
	}

}
