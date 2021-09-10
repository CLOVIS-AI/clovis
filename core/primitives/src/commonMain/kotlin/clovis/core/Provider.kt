package clovis.core

import clovis.core.cache.Cache

/**
 * An object that is able to asynchronously query an external resource to get some data.
 *
 * @see CachedProvider
 */
interface Provider<I : Id<O>, O> {

	/**
	 * Request the object identified by [id].
	 */
	suspend fun request(id: I): Result<I, O>

}

interface CachedProvider<I : Id<O>, O> {

	val provider: Provider<I, O>
	val cache: Cache<I, O>

}
