package clovis.core.cache

import clovis.core.IdBound
import clovis.core.Identifiable
import clovis.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

typealias CacheResult<Id, O> = Flow<Result<Id, O>>
typealias MutableCacheResult<Id, O> = MutableStateFlow<Result<Id, O>>

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
 * - Up-to-date: the correct value is available immediately (a [Result] different from [Result.Loading]),
 * - Stale: an old value is available immediately, which will be replaced by an up-to-date version as soon as it is available ([Result.Loading] with a [Result.Loading.lastKnownValue]),
 * - Expired: no values are available ([Result.Loading] without a [Result.Loading.lastKnownValue]).
 */
interface Cache<Id : IdBound, O : Identifiable<Id>> {

	/**
	 * Get the object identified by the provided [id].
	 *
	 * Depending on the implementation, this may or may not use local memory, storage, or perform a network request.
	 */
	operator fun get(id: Id): CacheResult<Id, O>

	/**
	 * Provide a [value] to the cache as up-to-date information.
	 *
	 * You can use this method to feed data to the cache, when you have a more efficient way to get it than it has;
	 * for example if your API returns nested objects.
	 *
	 * @see updateAll
	 */
	suspend fun update(value: O) = updateAll(listOf(value))

	/**
	 * Provide multiple [values] to the cache as up-to-date information.
	 *
	 * @see update
	 */
	suspend fun updateAll(values: Collection<O>)

	/**
	 * Communicates to the cache that its value is out-of-date, and should be queried again.
	 *
	 * On the next [get] call, the cache will start querying for a new value.
	 *
	 * @see forceRefresh
	 */
	suspend fun expire(id: Id)

	/**
	 * Gets a value that is guaranteed to be up-to-date.
	 *
	 * Convenience method for [expire] followed by [get].
	 */
	suspend fun forceRefresh(id: Id): CacheResult<Id, O> {
		expire(id)
		return get(id)
	}

}
