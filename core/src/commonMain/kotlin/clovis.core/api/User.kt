package clovis.core.api

import kotlinx.serialization.Serializable

@Serializable
data class User(
	val id: Long,
	val profile: Profile,
)
