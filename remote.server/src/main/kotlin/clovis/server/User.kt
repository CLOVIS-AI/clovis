package clovis.server

import io.ktor.application.*
import io.ktor.auth.*
import java.util.*

/**
 * Representation of a CLOVIS [User].
 *
 * The existence of this object should be accepted by other classes depending on this module as validity of the user account.
 */
class User internal constructor(internal val id: UUID) {
	override fun hashCode() = id.hashCode()
	override fun equals(other: Any?) = (other as? User)?.id == id
}

//region Ktor principal

class UserPrincipal(val user: User) : Principal

fun ApplicationCall.currentUser(): User {
	val principal = principal<UserPrincipal>() ?: error("To execute this query, you must be logged in.")
	return principal.user
}

//endregion
