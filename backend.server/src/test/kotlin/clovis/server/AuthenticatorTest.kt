package clovis.server

import clovis.database.Database
import clovis.test.runTest
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class AuthenticatorTest {

	@Test
	fun createUser() = runTest {
		val database = Database.connect()
		val auth = Authenticator(database)

		val userId = UUID.randomUUID()

		val email = "test-$userId@clovis.dev"
		val password = "123456"

		println("Creating a user")
		val id = auth.createUser(email, password)

		println("Finding the created user")
		val id2 = auth.findUserByEmail(email)

		assertEquals(id, id2)
	}

	@Test
	fun login() = runTest {
		val database = Database.connect()
		val auth = Authenticator(database)

		val userId = UUID.randomUUID()

		val email = "test-$userId@clovis.dev"
		val password = "123456"

		println("Creating a user")
		val id = auth.createUser(email, password)

		println("Logging in as the user")
		val id2 = auth.login(email, password)

		assertEquals(id, id2)
	}

}
