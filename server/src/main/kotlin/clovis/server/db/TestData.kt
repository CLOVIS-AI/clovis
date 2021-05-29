package clovis.server.db

import arrow.core.Either
import arrow.core.computations.either
import clovis.server.DatabaseException
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insertAndGetId

@Suppress("UNUSED_VARIABLE")
suspend fun testData(): Either<DatabaseException, Unit> = either {
	withDatabase {
		Users.deleteAll()
		Profiles.deleteAll()

		val melCaller = Users.insertAndGetId { user ->
			user[id] = 1L
			user[hashedPassword] = "THIS IS A FAKE HASHED PASSWORD."
			user[profile] = Profiles.insertAndGetId { profile ->
				profile[id] = 1
				profile[email] = "mel.caller@email.com"
				profile[fullName] = "Mel Caller"
			}
		}
	}.bind()

	val test = Users.create("test", "FAKE PASSWORD", "test") //TODO: real hashed password
		.bind()
}
