package clovis.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.mainModule() {
	install(ContentNegotiation) {
		json()
	}

	routing {
		pingRouting()
	}
}
