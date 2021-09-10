package clovis.test

import kotlinx.coroutines.CoroutineScope

// Implementation of the workaround mentioned here:
// https://youtrack.jetbrains.com/issue/KT-22228
expect fun runTest(block: suspend CoroutineScope.() -> Unit)
