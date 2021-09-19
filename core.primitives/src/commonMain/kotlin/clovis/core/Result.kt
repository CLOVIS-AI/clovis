package clovis.core

import clovis.core.cache.CacheResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first

/**
 * Represents a result from an API, with common cases as subtypes.
 */
sealed interface Progress<R : Ref<R, O>, O> {

	/**
	 * The reference on the object that was queried.
	 */
	val ref: R

	/**
	 * The request is currently ongoing. [lastKnownValue] gives the previous value.
	 *
	 * This type is used as the default state for [CacheResult], for example.
	 */
	data class Loading<R : Ref<R, O>, O>(
		override val ref: R,
		val lastKnownValue: Result<R, O>?,
	) : Progress<R, O>

	/**
	 * Represents a [Progress] that has finished [Loading].
	 */
	sealed interface Result<R : Ref<R, O>, O> : Progress<R, O>

	/**
	 * The [requested object][value] was successfully found.
	 */
	data class Success<R : Ref<R, O>, O>(
		override val ref: R,
		val value: O,
	) : Result<R, O>

	/**
	 * Represents an unsuccessful [Result].
	 */
	sealed interface Failure<R : Ref<R, O>, O> : Result<R, O> {
		fun throwException(): Nothing = throw IllegalStateException(toString())
	}

	/**
	 * The request couldn't complete, because the provided [ref] doesn't match with any existing object.
	 */
	data class NotFound<R : Ref<R, O>, O>(override val ref: R, val message: String?) : Failure<R, O> {
		override fun toString() = "NotFound $ref" +
				(if (message != null) " $message" else "")
	}

	/**
	 * The request couldn't complete, because the provided credentials are insufficient to warrant a reply.
	 * The requested object may or may not exist.
	 */
	data class Unauthorized<R : Ref<R, O>, O>(override val ref: R, val message: String?) : Failure<R, O> {
		override fun toString() = "Unauthorized $ref" +
				(if (message != null) " $message" else "")
	}

	/**
	 * The request couldn't complete, because the server is currently unavailable (no internet connection, server down…).
	 */
	data class Unavailable<R : Ref<R, O>, O>(override val ref: R, val message: String?) : Failure<R, O> {
		override fun toString() = "Unavailable $ref" +
				(if (message != null) " $message" else "")
	}
}

//region Extensions

/**
 * Skips all [Progress.Loading] values.
 */
fun <R, O> Flow<Progress<R, O>>.skipLoading() where R : Ref<R, O> =
	filterIsInstance<Progress.Result<R, O>>()

/**
 * Awaits the first [Progress.Result] value.
 */
suspend fun <R, O> Flow<Progress<R, O>>.awaitResult() where R : Ref<R, O> =
	skipLoading().first()

/**
 * Awaits the first [Progress.Result] value, returns it if it is successful, throws if it isn't.
 */
suspend fun <R, O> Flow<Progress<R, O>>.awaitResultOrThrow(): O where R : Ref<R, O> =
	when (val result = awaitResult()) {
		is Progress.Success -> result.value
		is Progress.Failure -> result.throwException()
		else -> error("Impossible case, a result that is neither Success nor Failure")
	}

//endregion
