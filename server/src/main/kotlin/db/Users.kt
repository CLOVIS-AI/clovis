package clovis.server.db

import org.jetbrains.exposed.dao.id.LongIdTable

object Users : LongIdTable() {
	val profile = optReference("profile_id", Profiles)
}
