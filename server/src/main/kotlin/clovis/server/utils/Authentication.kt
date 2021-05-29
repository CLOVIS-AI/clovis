package clovis.server.utils

import arrow.core.Left
import arrow.core.Right
import clovis.server.InvalidAuthentication
import io.ktor.application.*
import io.ktor.auth.*

const val RegularAuth = "basic-authentication"

fun Authentication.Configuration.configure() {
	basic(name = RegularAuth) { // TODO: clean
		realm = "Basic authentication"
		validate { credentials ->
			if (credentials.name == "test" && credentials.password == "test") // TODO: check database
				UserIdPrincipal(credentials.name)
			else
				null
		}
	}
}

inline fun <reified T : Principal> ApplicationCall.credentialsFor(realm: String): Request<T> {
	val principal = authentication.principal<T>() ?: return Left(InvalidAuthentication(realm))

	return Right(principal)
}

fun ApplicationCall.credentialsForRegular(): Request<UserIdPrincipal> = credentialsFor(RegularAuth)
