package clovis.server

import clovis.core.Provider
import io.ktor.application.*

/**
 * Manage local [Provider] implementations based on user credentials.
 */
class UserProviders {

	suspend fun findProvider(user: User, id: String): Provider<*> {
		TODO()
	}

}

fun ApplicationCall.providerId(): String = parameters["providerId"]
	?: error("The parameter 'providerId' is missing. Without it, it is not possible to execute remote provider operations.")

inline fun <reified T : Provider<*>> Provider<*>.ensureIs(): T {
	if (this is T)
		return this
	else error("The requested provider doesn't have the type ${T::class}: ${this::class}")
}
