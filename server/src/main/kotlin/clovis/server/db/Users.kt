package clovis.server.db

import clovis.core.api.Profile
import clovis.core.api.User
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select

object Users : LongIdTable() {
	val hashedPassword = varchar("hashed_password", 50)
	val profile = optReference("profile_id", Profiles)

	suspend fun withId(id: Long) = withDatabase {
		val query = (Users innerJoin Profiles)
			.select { Users.id eq id }
			.single()

		val profile = Profile(query[Profiles.fullName], query[Profiles.email])
		User(query[Users.id].value, profile)
	}

	suspend fun create(email: String, hashedPassword: String, fullName: String) = withDatabase {
		Users.insertAndGetId { user ->
			user[this.hashedPassword] = hashedPassword
			user[profile] = Profiles.insertAndGetId { profile ->
				profile[this.email] = email
				profile[this.fullName] = fullName
			}
		}.value
	}
}
