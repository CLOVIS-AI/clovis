package clovis.server.db

import org.jetbrains.exposed.dao.id.IntIdTable

object Profiles : IntIdTable() {
	val email = varchar("email", 50).uniqueIndex()
	val fullName = varchar("fullName", 50)
}
