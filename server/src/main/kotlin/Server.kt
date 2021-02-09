package clovis.server

import clovis.core.profile.Profile
import clovis.core.users.User

fun main() {
	val user = User(1, Profile("Mel Caller", "mel.caller@email.com"))

	println("Hello World, ${user.profile.fullName}!")
}
