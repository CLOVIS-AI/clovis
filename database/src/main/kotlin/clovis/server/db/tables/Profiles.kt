package clovis.server.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Profiles : IntIdTable() {
	val email = varchar("email", 50).uniqueIndex()
	val fullName = varchar("name_full", 50)
}
