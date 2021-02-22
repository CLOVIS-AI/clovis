package clovis.server

import clovis.core.profile.Profile
import clovis.core.users.User
import clovis.server.db.Profiles
import clovis.server.db.Users
import clovis.server.db.withDatabase
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.select

fun Route.pingRouting() {
	route("/ping") {
		get("{id}") {
			val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respondText(
				"Malformed id, expected an integer",
				status = HttpStatusCode.BadRequest
			)

			val result = withDatabase {
				val query = (Users innerJoin Profiles)
					.select { Users.id eq id }
					.single()

				val profile = Profile(query[Profiles.fullName], query[Profiles.email])
				User(query[Users.id].value, profile)
			}

			call.respond(result)
		}
	}
}

