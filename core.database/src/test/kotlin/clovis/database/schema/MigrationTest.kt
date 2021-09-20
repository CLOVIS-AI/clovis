package clovis.database.schema

import clovis.database.Database
import clovis.database.TestKeyspace
import clovis.database.queries.SelectExpression.Companion.isOneOf
import clovis.database.queries.UpdateExpression.Companion.set
import clovis.database.queries.insert
import clovis.database.queries.select
import clovis.database.utils.get
import clovis.test.runTest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class MigrationTest {

	private val id = column("id", Type.Binary.UUID)
	private val name = column("name", Type.Binary.Text)
	private val symbol = column("symb", Type.Binary.Text)

	@Test
	fun table() = runTest {
		val database = Database.connect()
		val tableName = "migration_test_1"

		println("Creating a table with columns 'id' and 'name'")
		val denominations = database.table(
			TestKeyspace, tableName,
			id.partitionKey(),
			name,
		)

		println("Adding a column 'symb'")
		database.table(
			TestKeyspace, tableName,
			id.partitionKey(),
			name,
			symbol,
		)

		println("Adding an option")
		database.table(
			TestKeyspace, tableName,
			id.partitionKey(),
			name,
			symbol,
		) {
			comment("This version adds a comment, but doesn't touch the columns.")
		}

		println("Cleanup")
		database.drop(denominations)
	}

	@Test
	fun manualView() = runTest {
		val database = Database.connect()

		println("Creating source table")
		val denominations = database.table(
			TestKeyspace, "migration_test_2",
			id.partitionKey(),
			name,
			symbol,
		)

		val id1 = UUID.randomUUID()
		val id2 = UUID.randomUUID()

		println("Inserting dummy data")
		denominations.insert(
			id set id1,
			name set "Hello"
		)
		denominations.insert(
			id set id2,
			name set "World"
		)

		println("Creating manual view")
		val denominationsByName = denominations.manualView(
			"migration_test_2_by_name",
			name.partitionKey(),
			id,
		)

		println("Checking values")
		val results = denominationsByName.select(
			name isOneOf listOf("Hello", "World"),
			id, name,
		).map { it[name] to it[id] }
			.toList().toMap()

		assertEquals(id1, results["Hello"])
		assertEquals(id2, results["World"])

		println("Cleanup")
		database.drop(denominations)
		database.drop(denominationsByName)
	}

}
