package clovis.core.cache

import clovis.core.Identifiable
import clovis.core.Provider
import clovis.core.Result
import clovis.test.runTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class MemoryCacheTest {

	//region Test provider
	private class Id(override val id: Int) : Identifiable<Int>

	private val provider = object : Provider<Int, Id> {
		override suspend fun request(id: Int): Result<Int, Id> {
			delay(100)
			return if (id > 0) Result.Success(Id(id))
			else Result.NotFound(id, "Negative ids are not allowed: $id")
		}
	}

	private val job = SupervisorJob()
	private fun testCache() = MemoryCache(DirectCache(provider), CoroutineScope(job))
	//endregion

	@Test
	fun get() = runTest {
		val cache = testCache()

		val result = cache[1]
			.onEach { println(it) }
			.first { it !is Result.Loading }

		assertIs<Result.Success<Int, Id>>(result)
		assertEquals(1, result.id)
		assertEquals(1, result.value.id)
	}

	@Test
	fun getError() = runTest {
		val cache = testCache()

		val result = cache[-1]
			.onEach { println(it) }
			.first { it !is Result.Loading }

		assertIs<Result.NotFound<Int>>(result)
		assertEquals(-1, result.id)
	}

	@Test
	fun update() = runTest {
		val cache = testCache()

		cache.update(Id(1))

		val result = withTimeoutOrNull(50) {
			cache[1]
				.onEach { println(it) }
				.first { it !is Result.Loading }
		} ?: error("Couldn't get the value we wanted in time, 'update' didn't set the value correctly")

		assertIs<Result.Success<Int, Id>>(result)
		assertEquals(1, result.id)
		assertEquals(1, result.value.id)
	}

}
