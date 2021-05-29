package clovis.server

import clovis.server.api.pingRouting
import clovis.server.api.usersRouting
import clovis.server.db.testData
import clovis.server.utils.configure
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
	if ("--test-data" in args) {
		runBlocking {
			testData().fold(
				{ it.exception.printStackTrace() },
				{ println(it) }
			)
		}

		return
	}

	EngineMain.main(args)
}

fun Application.mainModule() {
	install(ContentNegotiation) {
		json()
	}

	install(Authentication) {
		configure()
	}

	routing {
		pingRouting()
		usersRouting()
	}
}
