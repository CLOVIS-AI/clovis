package clovis.server

import clovis.core.profile.Profile
import clovis.core.users.User
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.pingRouting() {
	route("/ping") {
		get("{id}") {
			val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
				"Malformed id, expected an integer",
				status = HttpStatusCode.BadRequest
			)

			if (id != 1)
				return@get call.respondText(
					"The ID should be 1, found $id",
					status = HttpStatusCode.BadRequest
				)

			call.respond(User(1, Profile("Mel Caller", "mel.caller@email.com")))
		}
	}
}

