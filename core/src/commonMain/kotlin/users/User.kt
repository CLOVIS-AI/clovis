package clovis.core.users

import clovis.core.profile.Profile

data class User(
	val id: Long,
	val profile: Profile,
)
