package clovis.backend

import clovis.backend.core.JsonSerializer
import clovis.logger.logger
import clovis.logger.trace
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*

private object Main

private val log = logger(Main)

fun main(args: Array<String>) {
	log.trace { "Starting up 'main'" }

	EngineMain.main(args)
}

fun Application.start() {
	log.trace("Starting module")

	install(ContentNegotiation) {
		json(JsonSerializer)
	}

	log.trace("Defining routes")
	routing {
		get("/front/index.html") {
			log.warn("call")
			call.respondText("Hello world!")
		}
	}
}
