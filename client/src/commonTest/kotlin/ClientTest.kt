package clovis.client

import io.ktor.http.*
import io.ktor.utils.io.core.*

fun runTestWithClient(block: suspend Client.() -> Unit) = runTest {
	Client(Url("http://localhost:8000")).use { it.block() }
}
