package clovis.database.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Coroutine-aware [lazy] implementation.
 */
class SuspendLazy<T : Any>(private val builder: suspend () -> T) {

	private val lock = Mutex()
	private lateinit var data: T

	suspend fun get(): T {
		if (::data.isInitialized)
			return data

		lock.withLock {
			if (!::data.isInitialized)
				data = builder()

			return data
		}
	}

}

fun <T : Any> suspendLazy(init: suspend () -> T) = SuspendLazy(init)
