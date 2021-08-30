package clovis.core

import clovis.core.cache.Cache
import clovis.core.cache.IdBound

/**
 * An object that is able to asynchronously query an external resource to get some data.
 *
 * @see CachedProvider
 */
interface Provider<Id : IdBound, O : Identifiable<Id>> {

	/**
	 * Request the object identified by [id].
	 */
	suspend fun request(id: Id): Result<Id, O>

}

interface CachedProvider<Id : IdBound, O : Identifiable<Id>> {

	val provider: Provider<Id, O>
	val cache: Cache<Id, O>

}
