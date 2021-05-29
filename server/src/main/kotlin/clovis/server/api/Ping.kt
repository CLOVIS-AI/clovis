package clovis.server.api

import arrow.core.computations.either
import clovis.core.api.User
import clovis.server.RequestFailure
import clovis.server.db.Users
import clovis.server.respondRequest
import clovis.server.utils.RegularAuth
import clovis.server.utils.routeParam
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.pingRouting() {
	route("/ping") {
		get("/anonymous/{id}") {
			val result = either<RequestFailure, User> {
				val id = !call.routeParam<Long>("id")
				!Users.withId(id)
			}

			call.respondRequest(result)
		}

		authenticate(RegularAuth) {
			get("/auth") {
				call.respond("Authentication successful")
			}
		}
	}
}
