package clovis.core

import clovis.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClovisTest {

	private class TestService(clovis: Clovis) : Service(clovis) {
		override val providers: Set<Provider<*>>
			get() = emptySet()
	}

	@Test
	fun empty() {
		val clovis = Clovis()

		assertEquals(emptySet(), clovis.services.value)
		assertEquals(emptySet(), clovis.providers.toSet())
	}

	@Test
	fun registration() = runTest {
		val clovis = Clovis()

		val service = TestService(clovis)
		service.start()

		assertTrue(service.running)
		assertEquals(setOf(service), clovis.services.value)

		service.stop()

		assertFalse(service.running)
		assertEquals(emptySet(), clovis.services.value)
	}

}
