package clovis.server.api

import clovis.server.KtorAuth
import io.ktor.auth.*
import io.ktor.routing.*

fun Route.usersRouting() {
	route("/users/") {
		post("/create") {
			//TODO: create a user from their info
		}

		authenticate(KtorAuth) {
			get("/me") {
				//TODO: get my personal info
			}

			post("/me") {
				//TODO: edit my personal info
			}
		}
	}
}
