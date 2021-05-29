package clovis.server.api

import clovis.server.utils.RegularAuth
import clovis.server.utils.credentialsForRegular
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*

fun Route.usersRouting() {
	route("/users/") {
		post("/create") {
			//TODO: create a user from their info
		}

		authenticate(RegularAuth) {
			get("/me") {
				val principal = call.credentialsForRegular()

				//TODO: get my personal info
			}

			post("/me") {
				//TODO: edit my personal info
			}
		}
	}
}
