package clovis.core.cache

import clovis.core.Identifiable
import clovis.core.Provider
import clovis.core.Result
import clovis.test.runTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class MemoryCacheTest {

	//region Test provider
	@JvmInline
	private value class IntId(val value: Int) : clovis.core.Id
	private class Id(override val id: IntId) : Identifiable<IntId>

	private val provider = object : Provider<IntId, Id> {
		override suspend fun request(id: IntId): Result<IntId, Id> {
			return if (id.value > 0) Result.Success(Id(id))
			else Result.NotFound(id, "Negative ids are not allowed: $id")
		}
	}

	private val job = SupervisorJob()
	private fun testCache() = MemoryCache(DirectCache(provider), CoroutineScope(job))
	//endregion

	@Test
	fun get() = runTest {
		val cache = testCache()

		val result = cache[IntId(1)]
			.onEach { println(it) }
			.first { it !is Result.Loading }

		assertIs<Result.Success<IntId, Id>>(result)
		assertEquals(1, result.id.value)
		assertEquals(1, result.value.id.value)
	}

	@Test
	fun getError() = runTest {
		val cache = testCache()

		val result = cache[IntId(-1)]
			.onEach { println(it) }
			.first { it !is Result.Loading }

		assertIs<Result.NotFound<IntId>>(result)
		assertEquals(-1, result.id.value)
	}

	@Test
	fun update() = runTest {
		val cache = testCache()

		cache.update(Id(IntId(1)))

		val result = cache[IntId(1)]
			.onEach { println(it) }
			.first { it !is Result.Loading }

		assertIs<Result.Success<IntId, Id>>(result)
		assertEquals(1, result.id.value)
		assertEquals(1, result.value.id.value)
	}

}
