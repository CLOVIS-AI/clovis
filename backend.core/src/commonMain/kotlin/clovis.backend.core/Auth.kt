package clovis.backend.core

import kotlinx.serialization.Serializable

/**
 * Information required to create a new user, or login as an existing user.
 */
@Serializable
data class UserLoginInfo(
	val email: String,
	val password: String,
)
