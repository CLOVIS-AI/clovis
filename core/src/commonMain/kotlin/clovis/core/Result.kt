package clovis.core

import clovis.core.cache.CacheResult

/**
 * Represents a result from an API, with common cases as subtypes.
 */
sealed class Result<Id : IdBound, out O : Identifiable<Id>> {

	/**
	 * The ID of the object that was requested.
	 *
	 * @see Identifiable.id
	 */
	abstract val id: Id

	/**
	 * The [requested object][value] was successfully found.
	 */
	data class Success<Id : IdBound, O : Identifiable<Id>>(val value: O) : Result<Id, O>() {
		override val id get() = value.id
	}

	/**
	 * The request is currently ongoing. [lastKnownValue] gives the previous value.
	 *
	 * This type is used as the default state for [CacheResult], for example.
	 */
	data class Loading<Id : IdBound, O : Identifiable<Id>>(
		override val id: Id,
		val lastKnownValue: Result<Id, O>?,
	) : Result<Id, O>()

	/**
	 * The request couldn't complete, because the provided [id] doesn't match with any existing object.
	 */
	data class NotFound<Id : IdBound>(override val id: Id, val message: String?) : Result<Id, Nothing>()

	/**
	 * The request couldn't complete, because the provided credentials are insufficient to warrant a reply.
	 * The requested object may or may not exist.
	 */
	data class Unauthorized<Id : IdBound>(override val id: Id, val message: String?) : Result<Id, Nothing>()

	/**
	 * The request couldn't complete, because the server is currently unavailable (no internet connection, server down…).
	 */
	data class Unavailable<Id : IdBound>(override val id: Id, val message: String?) : Result<Id, Nothing>()
}
