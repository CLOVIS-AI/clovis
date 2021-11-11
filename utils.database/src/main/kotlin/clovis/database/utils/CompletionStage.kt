package clovis.database.utils

import java.util.concurrent.CompletionStage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Suspends until the [CompletionStage] finishes.
 *
 * This is a simple wrapper using [suspendCoroutine] to convert the callback-centric API of the database to Kotlin's
 * suspending world.
 */
internal suspend fun <T> CompletionStage<T>.await() = suspendCoroutine<T> { continuation ->
	this.thenApply { continuation.resume(it) }
		.exceptionally { continuation.resumeWithException(it) }
}
