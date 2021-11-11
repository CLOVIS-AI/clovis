package clovis.server

import clovis.database.Database
import clovis.test.runTest
import com.auth0.jwt.JWT
import kotlinx.coroutines.CoroutineScope
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFails

class AuthenticatorTest {

	@Test
	fun createUser() = runTest {
		val database = Database.connect()
		val auth = Authenticator(database, CoroutineScope(coroutineContext))

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
		val auth = Authenticator(database, CoroutineScope(coroutineContext))

		val userId = UUID.randomUUID()

		val email = "test-$userId@clovis.dev"
		val password = "123456"

		println("Creating a user")
		val id = auth.createUser(email, password)

		println("Logging in as the user")
		val id2 = auth.login(email, password)

		assertEquals(id, id2)
	}

	@Test
	fun tokens() = runTest {
		val database = Database.connect()
		val auth = Authenticator(database, CoroutineScope(coroutineContext))

		val userId = UUID.randomUUID()

		val email = "test-$userId@clovis.dev"
		val password = "123456"

		println("Creating a user")
		val user = auth.createUser(email, password)

		println("Generating tokens")
		val accessToken = auth.createAccessToken(user)
		val refreshToken = auth.createRefreshToken(user)

		println("Checking tokens")
		auth.checkAccessToken(JWT.decode(accessToken))
		auth.checkRefreshToken(JWT.decode(refreshToken))

		assertFails { auth.checkRefreshToken(JWT.decode(accessToken)) }
		assertFails { auth.checkAccessToken(JWT.decode(refreshToken)) }
	}

}
