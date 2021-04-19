package clovis.server

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import clovis.server.api.pingRouting
import clovis.server.api.usersRouting
import clovis.server.db.testData
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

const val KtorAuth = "basic-authentication"

fun Application.mainModule() {
	install(ContentNegotiation) {
		json()
	}

	install(Authentication) {
		basic(name = KtorAuth) { // TODO: clean
			realm = "Test"
			validate { credentials ->
				if (credentials.name == "test" && credentials.password == "test")
					UserIdPrincipal(credentials.name)
				else
					null
			}
		}
	}

	routing {
		pingRouting()
		usersRouting()
	}
}

//region Utilities

typealias Request<T> = Either<RequestFailure, T>

inline fun <reified T> ApplicationCall.param(name: String): Request<T> {
	val data = parameters[name] ?: return Left(ParameterType(name, null, T::class.java))

	@Suppress("UNCHECKED_CAST")
	return when (T::class) {
		Integer::class -> data.toTOrEither(name) { it.toIntOrNull() } as Request<T>
		Long::class -> data.toTOrEither(name) { it.toLongOrNull() } as Request<T>
		String::class -> Right(data) as Request<T>
		else -> error("Cannot parse parameter type ${T::class}.")
	}
}

inline fun <reified T> String.toTOrEither(name: String, transform: (String) -> T?): Request<T> {
	val v = transform(this)

	return if (v != null)
		Right(v)
	else
		Left(ParameterType(name, this, T::class.java))
}

//endregion
