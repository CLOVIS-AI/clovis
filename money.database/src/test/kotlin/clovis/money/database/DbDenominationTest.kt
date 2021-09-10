package clovis.money.database

import clovis.core.cache.cached
import clovis.core.cache.getOrThrow
import clovis.database.Database
import clovis.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DbDenominationTest {

	@Test
	fun createAndGet() = runTest {
		val database = Database.connect()

		val provider = DatabaseDenominationProvider(database)
		val denominations = DatabaseDenominationCachedProvider(provider, provider.cached())

		val id = denominations.create("Euro", "€", symbolBeforeValue = false)
		val euro = denominations.cache.getOrThrow(id)

		assertEquals("Euro", euro.name)
		assertEquals("€", euro.symbol)
		assertEquals(false, euro.symbolBeforeValue)
	}

}
