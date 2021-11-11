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
	private val intProvider = object : Provider<Int> {
		override fun directRequest(ref: Ref<Int>): Flow<Progress<Int>> = flow {
			require(ref is IntRef) { "Illegal reference type: $ref" }

			if (ref.id > 0) emit(Progress.Success(ref, ref.id))
			if (ref.id == 0) emit(Progress.Success(ref, Random.nextInt()))
			else emit(Progress.NotFound(ref, "Negative ids are not allowed: ${ref.id}"))
		}

		override val cache: Cache<Int> = DirectCache<Int>()
			.cachedInMemory(CoroutineScope(job))

		override fun decodeRef(encoded: String): Ref<Int> = IntRef(encoded.toInt())
	}

	private inner class IntRef(val id: Int) : Ref<Int> {
		override val provider: Provider<Int>
			get() = intProvider

		override fun hashCode() = id
		override fun equals(other: Any?) = (other as? IntRef)?.id == id
		override fun toString() = "Ref on $id"
		override fun encodeRef(): String = id.toString()
	}
	//endregion

	@Test
	fun get() = runTest {
		val cache = intProvider.cache

		val result = cache[IntRef(1)]
			.onEach { println(it) }
			.first { it !is Progress.Loading }

		assertIs<Progress.Success<Int>>(result)
		assertEquals(1, (result.ref as IntRef).id)
		assertEquals(1, result.value)
	}

	@Test
	fun getError() = runTest {
		val cache = intProvider.cache

		val result = cache[IntRef(-1)]
			.onEach { println(it) }
			.first { it !is Progress.Loading }

		assertIs<Progress.NotFound<Int>>(result)
		assertEquals(-1, (result.ref as IntRef).id)
	}

	@Test
	fun update() = runTest {
		val cache = intProvider.cache

		cache.update(IntRef(1), 1)

		val result = cache[IntRef(1)]
			.onEach { println(it) }
			.first { it !is Progress.Loading }

		assertIs<Progress.Success<Int>>(result)
		assertEquals(1, (result.ref as IntRef).id)
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
				.filterIsInstance<Progress.Success<Int>>()
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
