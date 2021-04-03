package clovis.server

import arrow.core.computations.either
import clovis.core.api.User
import clovis.server.db.Users
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.pingRouting() {
	route("/ping") {
		get("/anonymous/{id}") {
			val result = either<RequestFailure, User> {
				val id = !call.param<Long>("id")
				!Users.withId(id)
			}

			call.respondRequest(result)
		}

		authenticate(KtorAuth) {
			get("/auth") {
				call.respond("Authentication successful")
			}
		}
	}
}

