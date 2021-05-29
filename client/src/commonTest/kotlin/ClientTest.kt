package clovis.client

import clovis.client.auth.AnonymousAccess
import clovis.client.auth.ClientAuthentication
import clovis.client.auth.PasswordAccess
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun runTestWithClient(auth: ClientAuthentication = AnonymousAccess, block: suspend Client.() -> Unit) = runTest {
	Client(Url("http://localhost:8000"), auth).use { it.block() }
}

class ClientTest {

	@Test
	fun testPingAuth() = runTestWithClient(auth = PasswordAccess("test", "test")) {
		val request = get<String>("$url/ping/auth")

		assertTrue(request is Success)
		assertEquals("Authentication successful", request.result)
	}

	@Test
	fun testPingNoAuth() = runTestWithClient(auth = AnonymousAccess) {
		val request = get<String>("$url/ping/auth")

		assertTrue(request is HttpFailure)
		assertEquals(HttpStatusCode.Unauthorized, request.code)
	}

}
