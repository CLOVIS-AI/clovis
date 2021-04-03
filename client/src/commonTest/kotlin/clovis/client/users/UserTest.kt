package clovis.client.users

import clovis.client.RequestResult
import clovis.client.Success
import clovis.client.assertIsSuccess
import clovis.client.runTestWithClient
import clovis.core.api.Profile
import clovis.core.api.User
import kotlin.test.Test
import kotlin.test.assertEquals

class UserTest {

	@Test
	fun testPing() = runTestWithClient {
		val expected = User(1, Profile("Mel Caller", "mel.caller@email.com"))
		val actual: RequestResult<User> = getUser(1)

		assertIsSuccess(actual)
		actual as Success<User>
		assertEquals(expected, actual.result)
	}

}
