package clovis.server

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
