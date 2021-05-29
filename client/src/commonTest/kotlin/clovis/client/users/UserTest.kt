package clovis.client.users

import clovis.client.HttpFailure
import clovis.client.RequestResult
import clovis.client.Success
import clovis.client.runTestWithClient
import clovis.core.api.Profile
import clovis.core.api.User
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserTest {

	@Test
	fun testPing() = runTestWithClient {
		val expected = User(1, Profile("Mel Caller", "mel.caller@email.com"))
		val actual: RequestResult<User> = getUser(1)

		assertTrue(actual is Success)
		assertEquals(expected, actual.result)
	}

	@Test
	fun getMeUnauthenticated() = runTestWithClient {
		val actual = getMe()

		assertTrue(actual is HttpFailure)
		assertEquals(HttpStatusCode.Unauthorized, actual.code)
	}

}
