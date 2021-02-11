package clovis.core.profile

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
	val fullName: String,
	val email: String,
)
