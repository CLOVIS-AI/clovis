package clovis.server.db

import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId

@Suppress("UNUSED_VARIABLE")
suspend fun testData() = withDatabase {

	Users.deleteAll()
	Profiles.deleteAll()

	val melCaller = Users.insert { user ->
		user[id] = 1L
		user[profile] = Profiles.insertAndGetId { profile ->
			profile[id] = 1
			profile[email] = "mel.caller@email.com"
			profile[fullName] = "Mel Caller"
		}
	}

}
