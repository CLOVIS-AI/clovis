package clovis.server

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class PingTest {

	@Test
	fun testPing() {
		withTestApplication({ mainModule() }) {
			handleRequest(HttpMethod.Get, "/ping/anonymous/1").apply {
				//language=JSON
				assertEquals(
					"""{"id":1,"profile":{"fullName":"Mel Caller","email":"mel.caller@email.com"}}""",
					response.content
				)
				assertEquals(HttpStatusCode.OK, response.status())
			}
		}
	}

}
