package clovis.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.promise

private val testScope = CoroutineScope(SupervisorJob())
actual fun runTest(block: suspend CoroutineScope.() -> Unit): dynamic = testScope.promise { block() }
