package clovis.core.cache

import clovis.core.Progress
import clovis.core.Provider
import clovis.core.Ref
import clovis.test.runTest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

class MemoryCacheTest {

	//region Test provider
	private val job = SupervisorJob()
	private val intProvider = object : Provider<IntRef, Int> {
		override val cache: Cache<IntRef, Int> = DirectCache<IntRef, Int>()
			.cachedInMemory(CoroutineScope(job))
	}

	private inner class IntRef(val id: Int) : Ref<IntRef, Int> {
		override fun directRequest(): Flow<Progress<IntRef, Int>> = flow {
			if (id > 0) emit(Progress.Success(this@IntRef, id))
			if (id == 0) emit(Progress.Success(this@IntRef, Random.nextInt()))
			else emit(Progress.NotFound(this@IntRef, "Negative ids are not allowed: $id"))
		}

		override val provider: Provider<IntRef, Int>
			get() = intProvider

		override fun hashCode() = id
		override fun equals(other: Any?) = (other as? IntRef)?.id == id
		override fun toString() = "Ref on $id"
	}
	//endregion

	@Test
	fun get() = runTest {
		val cache = intProvider.cache

		val result = cache[IntRef(1)]
			.onEach { println(it) }
			.first { it !is Progress.Loading }

		assertIs<Progress.Success<IntRef, Int>>(result)
		assertEquals(1, result.ref.id)
		assertEquals(1, result.value)
	}

	@Test
	fun getError() = runTest {
		val cache = intProvider.cache

		val result = cache[IntRef(-1)]
			.onEach { println(it) }
			.first { it !is Progress.Loading }

		assertIs<Progress.NotFound<IntRef, Int>>(result)
		assertEquals(-1, result.ref.id)
	}

	@Test
	fun update() = runTest {
		val cache = intProvider.cache

		cache.update(IntRef(1), 1)

		val result = cache[IntRef(1)]
			.onEach { println(it) }
			.first { it !is Progress.Loading }

		assertIs<Progress.Success<IntRef, Int>>(result)
		assertEquals(1, result.ref.id)
		assertEquals(1, result.value)
	}

	@Test
	fun expire() = runTest {
		val cache = intProvider.cache

		val witness = MutableStateFlow(0)

		/*
		 * Start a job in the background, that will subscribe to the
		 * cache and update 'witness' everytime a new value arrives.
		 *
		 * The reference '0' means "generate a random integer".
		 */
		val backgroundJob = Job()
		CoroutineScope(backgroundJob).launch {
			delay(10)
			cache[IntRef(0)]
				.onEach { println(it) }
				.filterIsInstance<Progress.Success<IntRef, Int>>()
				.collect {
					witness.value = it.value
				}
		}

		// Because of the delay in the background job, this should be the default value of the witness
		// (checking the initialization of the witness)
		assertEquals(0, witness.value.also { println(" * Initial witness value: $it") })

		// Delay, then the background job should have found its first value
		delay(50)
		val newValue = witness.value.also { println(" * First witness value: $it") }
		assertNotEquals(0, newValue)

		// Query again, we should get the same value (if the cache actually caches queries)
		delay(10)
		assertEquals(newValue, witness.value.also { println(" * Cached first witness value: $it") })

		// If we expire the cache, it should query a new value, thus finding a new (different) random value
		cache.expire(IntRef(0))
		delay(10)
		assertNotEquals(newValue, witness.value.also { println(" * Witness value after expiration: $it") })

		job.cancel()
	}

}
