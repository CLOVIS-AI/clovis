package clovis.core.users

import clovis.core.profile.Profile
import kotlinx.serialization.Serializable

@Serializable
data class User(
	val id: Long,
	val profile: Profile,
)
