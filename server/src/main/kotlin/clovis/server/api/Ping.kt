package clovis.server.api

import arrow.core.computations.either
import clovis.core.api.User
import clovis.server.DatabaseFailure.Companion.asRequest
import clovis.server.RequestFailure
import clovis.server.db.tables.Users
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
				!Users.withId(id).asRequest()
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
