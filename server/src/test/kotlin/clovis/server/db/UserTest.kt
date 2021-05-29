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

	@Test
	fun `duplicate users`() = runBlocking {
		val email = "duplicate.fr"
		val fullName = "my full name"

		suspend fun createUser(): Either<DatabaseException, Unit> = either {
			val id = Users.create(email, "some random hashed password", fullName).bind()
			val user = Users.withId(id).bind()

			assertEquals(User(id, Profile(fullName, email)), user)
		}

		val user1 = createUser()
		val user2 = createUser()

		user1.mapLeft { assert(it is DatabaseException.ConstraintViolation) { "If the user1 fails, it should fail because of a constraint violation: $it" } }

		user2.fold(
			{ assert(it is DatabaseException.ConstraintViolation) { "Expected to fail with a ConstraintViolation, failed with: $it" } },
			{ assert(false) { "The user should have failed BEFORE now: $it" } }
		)
	}

}
