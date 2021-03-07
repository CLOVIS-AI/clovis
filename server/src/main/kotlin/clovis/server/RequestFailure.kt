package clovis.server

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

class DatabaseException(private val exception: Throwable) : RequestFailure() {

	override suspend fun ApplicationCall.handle() {
		exception.printStackTrace()

		return respondText(
			"A database exception happened during handling",
			status = HttpStatusCode.InternalServerError
		)
	}

}

//endregion
