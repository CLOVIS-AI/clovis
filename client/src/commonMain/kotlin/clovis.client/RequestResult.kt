package clovis.client

import io.ktor.client.features.*
import io.ktor.http.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Represents the result or failure of a request.
 *
 * When Kotlin Arrow becomes stable, will be replaced by `Either<RequestFailure, T>`. //TODO in #14
 */
sealed class RequestResult<out T>

/**
 * A request failure handled by an [HttpStatusCode].
 */
data class HttpFailure(val code: HttpStatusCode, val text: String, val exception: ClientRequestException? = null) :
	RequestResult<Nothing>() {
	override fun toString() = "HTTP error \"$code\": \"$text\"" + if (exception != null) "\n$exception" else ""
}

/**
 * A successful request, holding a [result].
 */
data class Success<T>(val result: T) : RequestResult<T>() {
	override fun toString() = "HTTP response: $result"
}

/**
 * Returns [Success.result] if the request was successful, `null` otherwise.
 */
fun <T> RequestResult<T>.successOrNull() = when (this) {
	is Success<T> -> this.result
	else -> {
		println("Failed result has been ignored by 'successOrNull': $this")
		null
	}
}

/**
 * Asserts that a [request] is successful, failing and printing a [message] if it isn't.
 *
 * A call to this function is semantically equivalent to:
 * ```kotlin
 * val request: RequestResult<T> = …
 * assert(request is Success)
 * val request = request as Success
 * ```
 */
@OptIn(ExperimentalContracts::class)
fun <T> assertIsSuccess(
	request: RequestResult<T>,
	message: (RequestResult<T>) -> String = { "Request failed with $it" }
) {
	contract {
		returns() implies (request is Success<T>)
	}

	if (request !is Success)
		throw AssertionError(message)
}
