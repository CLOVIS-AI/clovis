package clovis.core.api

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
	val fullName: String,
	val email: String,
)
