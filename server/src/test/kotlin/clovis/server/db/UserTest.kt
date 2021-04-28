package clovis.server.db

import arrow.core.Either
import arrow.core.computations.either
import clovis.core.api.Profile
import clovis.core.api.User
import clovis.server.DatabaseException
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class UserTest {

	@Test
	fun `create new user`(): Unit = runBlocking {
		val email = "some random email${Math.random()}.fr"
		val fullName = "my full name"

		val user: Either<DatabaseException, Unit> = either {
			val id = Users.create(email, "some random hashed password", fullName).bind()
			val user = Users.withId(id).bind()

			assertEquals(User(id, Profile(fullName, email)), user)
		}
		assert(user.isRight()) { user }
	}

}
