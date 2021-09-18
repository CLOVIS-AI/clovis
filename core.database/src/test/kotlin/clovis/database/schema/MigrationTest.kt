package clovis.database.schema

import clovis.database.Database
import clovis.database.TestKeyspace
import clovis.test.runTest
import org.junit.Test

class MigrationTest {

	private val id = column("id", Type.Binary.UUID)
	private val name = column("name", Type.Binary.Text)
	private val symbol = column("symb", Type.Binary.Text)

	@Test
	fun test() = runTest {
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

}
