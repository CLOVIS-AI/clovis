package clovis.server.utils

import io.ktor.auth.*

const val KtorAuth = "basic-authentication"

fun Authentication.Configuration.configure() {
	basic(name = KtorAuth) { // TODO: clean
		realm = "Basic authentication"
		validate { credentials ->
			if (credentials.name == "test" && credentials.password == "test") // TODO: check database
				UserIdPrincipal(credentials.name)
			else
				null
		}
	}
}
