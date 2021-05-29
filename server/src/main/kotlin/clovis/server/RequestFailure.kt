package clovis.server

import arrow.core.Either
import clovis.server.db.DatabaseProblem
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
data class DatabaseFailure(val underlyingException: DatabaseProblem) : RequestFailure() {

	override suspend fun ApplicationCall.handle() {
		underlyingException.exception.printStackTrace()

		return respondText(
			"A database exception happened during handling",
			status = HttpStatusCode.InternalServerError
		)
	}

	companion object {
		fun <T> Either<DatabaseProblem, T>.asRequest(): Request<T> = mapLeft { DatabaseFailure(it) }
	}
}

/**
 * Missing authentication for a specific authentication [realm].
 */
data class InvalidAuthentication(val realm: String) : RequestFailure() {
	override suspend fun ApplicationCall.handle() {
		respondText("Couldn't get authentication for realm '$realm'", status = HttpStatusCode.Forbidden)
	}
}

//endregion
