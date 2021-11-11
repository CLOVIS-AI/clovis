package clovis.backend

import clovis.backend.core.Tokens
import clovis.backend.core.UserLoginInfo
import clovis.server.Authenticator
import clovis.server.User
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.date.*
import kotlin.time.ExperimentalTime

private const val REFRESH_COOKIE_NAME = "REFRESH-TOKEN"

@OptIn(ExperimentalTime::class)
fun ApplicationCall.setRefreshTokenCookie(value: String) {
	val options = HashMap<String, String?>()

	// The cookie cannot be used by other websites using the API
	options["SameSite"] = "Secure"

	// The cookie cannot be read from JavaScript
	options["HttpOnly"] = null

	// The cookie can only be sent through HTTPS
	if (!application.developmentMode)
		options["Secure"] = null

	response.cookies.append(
		Cookie(
			REFRESH_COOKIE_NAME,
			value = value,
			expires = GMTDate() + Tokens.refreshTokenLifetime.inWholeMilliseconds,
			path = "/",
			extensions = options,
		)
	)
}

fun Route.userRoutes(authenticator: Authenticator) = route("/users") {

	suspend fun ApplicationCall.respondCredentials(user: User) {
		val accessToken = authenticator.createAccessToken(user)
		val refreshToken = authenticator.createRefreshToken(user)

		setRefreshTokenCookie(refreshToken)
		respondText(accessToken)
	}

	post("/create") {
		val info = call.receive<UserLoginInfo>()

		val user = authenticator.createUser(info.email, info.password)

		call.respondCredentials(user)
	}

	put("/login") {
		val info = call.receive<UserLoginInfo>()

		val user = authenticator.login(info.email, info.password)

		call.respondCredentials(user)
	}

	post("/logout") {
		call.setRefreshTokenCookie("CLEARED")
		call.respondText("You are no longer logged in.")
	}

	put("/refresh") {
		val refreshToken = call.request.cookies[REFRESH_COOKIE_NAME]
			?: error("To get a new access token, the client should provide a valid refresh token as the $REFRESH_COOKIE_NAME cookie.")

		val user = authenticator.checkRefreshToken(refreshToken)

		call.respondCredentials(user)
	}

}
