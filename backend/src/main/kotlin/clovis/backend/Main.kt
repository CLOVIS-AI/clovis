package clovis.backend

import clovis.backend.core.JsonSerializer
import clovis.database.Database
import clovis.logger.info
import clovis.logger.logger
import clovis.logger.trace
import clovis.money.server.remoteDenominations
import clovis.server.Authenticator
import clovis.server.UserPrincipal
import clovis.server.UserProviders
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope

private object Main

private val log = logger(Main)

lateinit var database: Database

suspend fun main(args: Array<String>) {
	log.trace { "Starting up 'main'" }

	log.info { "Connecting to the database…" }
	database = Database.connect()

	EngineMain.main(args)
}

fun Application.start() {
	log.info("Starting HTTP module")

	install(ContentNegotiation) {
		json(JsonSerializer)
	}

	val authenticator = Authenticator(database, CoroutineScope(coroutineContext))
	install(Authentication) {
		jwt {
			realm = "access-token"
			verifier(authenticator.verifier)
			validate { refreshToken ->
				try {
					val user = authenticator.checkAccessToken(refreshToken.payload)
					UserPrincipal(user)
				} catch (e: Exception) {
					log.info("Auth: access token refused because of: ${e.stackTraceToString()}")
					null
				}
			}
		}
	}

	val userProviders = UserProviders()

	log.trace("Defining routes")
	routing {
		get("/front/index.html") {
			log.warn("call")
			call.respondText("Hello world!")
		}

		route("/api") {
			userRoutes(authenticator)
		}

		route("/remote") {
			authenticate {
				remoteDenominations(userProviders)
			}
		}
	}
}
