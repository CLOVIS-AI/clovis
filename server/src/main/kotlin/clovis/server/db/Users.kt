package clovis.server.db

import clovis.core.api.Profile
import clovis.core.api.User
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.select

object Users : LongIdTable() {
	val profile = optReference("profile_id", Profiles)

	suspend fun withId(id: Long) = withDatabase {
		val query = (Users innerJoin Profiles)
			.select { Users.id eq id }
			.single()

		val profile = Profile(query[Profiles.fullName], query[Profiles.email])
		User(query[Users.id].value, profile)
	}
}
