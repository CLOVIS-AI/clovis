package clovis.server

import clovis.server.utils.Request
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

//region Utilities

suspend inline fun <reified T : Any> ApplicationCall.respondRequest(result: Request<T>) {
	result.fold(
		{ with(this) { with(it) { handle() } } },
		{ respond(it) }
	)
}

//endregion
//region Types

sealed class RequestFailure {

	abstract suspend fun ApplicationCall.handle()

}

class ParameterType(
	private val param: String,
	private val value: Any?,
	private val expected: Class<*>
) : RequestFailure() {

	override suspend fun ApplicationCall.handle() = respondText(
		"Parameter '$param' has the wrong type: expected a $expected but found $value",
		status = HttpStatusCode.BadRequest
	)

}

/**
 * Something went wrong while accessing the database.
 */
sealed class DatabaseException(val exception: Throwable) : RequestFailure() {

	override suspend fun ApplicationCall.handle() {
		exception.printStackTrace()

		return respondText(
			"A database exception happened during handling",
			status = HttpStatusCode.InternalServerError
		)
	}

	/**
	 * Something went wrong, but we couldn't recognize why.
	 */
	class Unknown(exception: Throwable) : DatabaseException(exception)

	/**
	 * A database constraint was violated.
	 */
	class ConstraintViolation(exception: Throwable) : DatabaseException(exception)
}

//endregion
