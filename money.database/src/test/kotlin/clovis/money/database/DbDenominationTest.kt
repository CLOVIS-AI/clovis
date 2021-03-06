package clovis.money.database

import clovis.core.cache.DirectCache
import clovis.core.firstResultOrThrow
import clovis.core.request
import clovis.database.Database
import clovis.money.Denomination
import clovis.test.runTest
import kotlinx.coroutines.CoroutineScope
import kotlin.test.Test
import kotlin.test.assertEquals

class DbDenominationTest {

	@Test
	fun createAndGet() = runTest {
		val database = Database.connect()

		val denominations = DatabaseDenominationProvider(database, DirectCache(), CoroutineScope(coroutineContext))

		val ref = denominations.creator.create("Euro", "€", symbolBeforeValue = false)
		val euro: Denomination = ref.request().firstResultOrThrow()

		assertEquals("Euro", euro.name)
		assertEquals("€", euro.symbol)
		assertEquals(false, euro.symbolBeforeValue)
	}

}
