package clovis.core

import clovis.core.cache.CacheResult

/**
 * Represents a result from an API, with common cases as subtypes.
 */
sealed class Result<I : Id, out O : Identifiable<I>> {

	/**
	 * The ID of the object that was requested.
	 *
	 * @see Identifiable.id
	 */
	abstract val id: I

	/**
	 * The [requested object][value] was successfully found.
	 */
	data class Success<I : Id, O : Identifiable<I>>(val value: O) : Result<I, O>() {
		override val id get() = value.id
	}

	/**
	 * The request is currently ongoing. [lastKnownValue] gives the previous value.
	 *
	 * This type is used as the default state for [CacheResult], for example.
	 */
	data class Loading<I : Id, O : Identifiable<I>>(
		override val id: I,
		val lastKnownValue: Result<I, O>?,
	) : Result<I, O>()

	/**
	 * The request couldn't complete, because the provided [id] doesn't match with any existing object.
	 */
	data class NotFound<I : Id>(override val id: I, val message: String?) : Result<I, Nothing>()

	/**
	 * The request couldn't complete, because the provided credentials are insufficient to warrant a reply.
	 * The requested object may or may not exist.
	 */
	data class Unauthorized<I : Id>(override val id: I, val message: String?) : Result<I, Nothing>()

	/**
	 * The request couldn't complete, because the server is currently unavailable (no internet connection, server down…).
	 */
	data class Unavailable<I : Id>(override val id: I, val message: String?) : Result<I, Nothing>()
}

//region Extensions

/**
 * Whether this [Result] can be considered successful.
 *
 * `true` if it is a [Result.Success], `null` if it is a [Result.Loading], and `false` otherwise.
 */
val Result<*, *>.success: Boolean?
	get() = when (this) {
		is Result.Loading -> null
		is Result.Success -> true
		else -> false
	}

//endregion
