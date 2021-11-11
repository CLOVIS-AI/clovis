package clovis.core

import clovis.core.cache.CacheResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first

/**
 * Represents a result from an API, with common cases as subtypes.
 */
sealed interface Progress<O> {

	/**
	 * The reference on the object that was queried.
	 */
	val ref: Ref<O>

	/**
	 * The request is currently ongoing. [lastKnownValue] gives the previous value.
	 *
	 * This type is used as the default state for [CacheResult], for example.
	 */
	data class Loading<O>(
		override val ref: Ref<O>,
		val lastKnownValue: Result<O>?,
	) : Progress<O>

	/**
	 * Represents a [Progress] that has finished [Loading].
	 */
	sealed interface Result<O> : Progress<O>

	/**
	 * The [requested object][value] was successfully found.
	 */
	data class Success<O>(
		override val ref: Ref<O>,
		val value: O,
	) : Result<O>

	/**
	 * Represents an unsuccessful [Result].
	 */
	sealed interface Failure<O> : Result<O> {
		fun throwException(): Nothing = throw IllegalStateException(toString())
	}

	/**
	 * The request couldn't complete, because the provided [ref] doesn't match with any existing object.
	 */
	data class NotFound<O>(override val ref: Ref<O>, val message: String?) : Failure<O> {
		override fun toString() = "NotFound $ref" +
				(if (message != null) " $message" else "")
	}

	/**
	 * The request couldn't complete, because the provided credentials are insufficient to warrant a reply.
	 * The requested object may or may not exist.
	 */
	data class Unauthorized<O>(override val ref: Ref<O>, val message: String?) : Failure<O> {
		override fun toString() = "Unauthorized $ref" +
				(if (message != null) " $message" else "")
	}

	/**
	 * The request couldn't complete, because the server is currently unavailable (no internet connection, server down…).
	 */
	data class Unavailable<O>(override val ref: Ref<O>, val message: String?) : Failure<O> {
		override fun toString() = "Unavailable $ref" +
				(if (message != null) " $message" else "")
	}
}

//region Extensions

/**
 * Skips all [Progress.Loading] values.
 */
fun <O> Flow<Progress<O>>.skipLoading() =
	filterIsInstance<Progress.Result<O>>()

/**
 * Awaits the first [Progress.Result] value.
 */
suspend fun <O> Flow<Progress<O>>.firstResult() =
	skipLoading().first()

/**
 * Awaits the first [Progress.Result] value, returns it if it is successful, throws if it isn't.
 */
suspend fun <O> Flow<Progress<O>>.firstResultOrThrow(): O =
	when (val result = firstResult()) {
		is Progress.Success -> result.value
		is Progress.Failure -> result.throwException()
		else -> error("Impossible case, a result that is neither Success nor Failure")
	}

//endregion
