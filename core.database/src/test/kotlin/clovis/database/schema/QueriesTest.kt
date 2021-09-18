package clovis.database.schema

import clovis.database.Database
import clovis.database.TestKeyspace
import clovis.database.queries.SelectExpression.Companion.eq
import clovis.database.queries.SelectExpression.Companion.isOneOf
import clovis.database.queries.UpdateExpression.Companion.set
import clovis.database.queries.delete
import clovis.database.queries.insert
import clovis.database.queries.select
import clovis.database.queries.update
import clovis.database.utils.get
import clovis.test.runTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class QueriesTest {

	private val id = column("id", Type.Binary.UUID)
	private val name = column("name", Type.Binary.Text)

	@Test
	fun insertAndSelect() = runTest {
		val database = Database.connect()
		val table = database.table(
			TestKeyspace, "queries_insert",
			id.partitionKey(),
			name,
		) {
			defaultTimeToLive(120)
		}

		val uuid = UUID.randomUUID()

		println("Inserting some data")
		table.insert(
			id set uuid,
			name set "Hello world",
		) {
			ignoreIfAlreadyExists = true
			secondsToLive = 60
		}

		println("Querying data")
		val results = table.select(
			id eq uuid,
			id, name,
		).first()
		assertEquals(uuid, results[id])
		assertEquals("Hello world", results[name])

		println("Querying data with a list")
		table.select(
			id isOneOf listOf(uuid, UUID.randomUUID()),
			id, name,
		) {
			limit = 10
		}
	}

	@Test
	fun delete() = runTest {
		val database = Database.connect()
		val table = database.table(
			TestKeyspace, "queries_delete",
			id.partitionKey(),
			name,
		) {
			defaultTimeToLive(3600)
		}

		val uuid = UUID.randomUUID()

		println("Inserting some data")
		table.insert(
			id set uuid,
			name set "Hello world",
		)

		println("Removing the data")
		table.delete(id eq uuid)

		println("Checking results")
		val results = table.select(id eq uuid).toList()
		assertEquals(0, results.size, "Expected 0 results, found ${results.joinToString { it.formattedContents }}")
	}

	@Test
	fun deleteIfSecondaryKeyMatches() = runTest {
		val database = Database.connect()
		val table = database.table(
			TestKeyspace, "queries_delete_secondary",
			id.partitionKey(),
			name,
		) {
			defaultTimeToLive(3600)
		}

		val uuid1 = UUID.randomUUID()
		val uuid2 = UUID.randomUUID()

		println("Inserting some data")
		table.insert(
			id set uuid1,
			name set "Hello world",
		)
		table.insert(
			id set uuid2,
			name set "Something else",
		)

		println("Checking insert")
		val results1 = table.select(id isOneOf listOf(uuid1, uuid2))
			.toList()
		assertEquals(
			2,
			results1.size,
			"There should be 2 results, found: ${results1.joinToString { it.formattedContents }}"
		)

		println("Removing the data")
		table.delete(
			id eq uuid1,
			onlyIf = name eq "Hello world",
		)

		println("Checking results")
		val results2 = table.select(id isOneOf listOf(uuid1, uuid2))
			.toList()
		assertEquals(
			1,
			results2.size,
			"There should be 1 result, found: ${results2.joinToString { it.formattedContents }}"
		)
	}

	@Test
	fun update() = runTest {
		val database = Database.connect()
		val table = database.table(
			TestKeyspace, "queries_update",
			id.partitionKey(),
			name,
		) {
			defaultTimeToLive(3600)
		}

		val uuid = UUID.randomUUID()

		println("Adding data")
		table.insert(
			id set uuid,
		)

		println("Editing data with INSERT")
		table.insert(
			id set uuid,
			name set "Hello World 1",
		)

		println("Editing data with UPDATE")
		table.update(
			id eq uuid,
			name set "Hello World 2",
			onlyIf = name eq "Hello World 1"
		)

		println("Checking results")
		val results = table.select(id eq uuid).toList()
		assertEquals(1, results.size, "Found: ${results.joinToString { it.formattedContents }}")
	}

}
