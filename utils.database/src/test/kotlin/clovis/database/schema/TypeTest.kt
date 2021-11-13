package clovis.database.schema

import clovis.database.Database
import clovis.database.TestKeyspace
import clovis.database.queries.SelectExpression.Companion.eq
import clovis.database.queries.UpdateExpression.Companion.add
import clovis.database.queries.UpdateExpression.Companion.remove
import clovis.database.queries.UpdateExpression.Companion.set
import clovis.database.queries.insert
import clovis.database.queries.select
import clovis.database.queries.update
import clovis.database.utils.get
import clovis.test.runTest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TypeTest {

	@Test
	fun set() = runTest {
		val database = Database.connect()

		println("Creating the table…")
		val id = column("id", Type.Binary.UUID)
		val username = column("name", Type.Binary.Text)
		val labels = column("labels", Type.Collections.Set(Type.Binary.Text))

		val table = database.table(
			TestKeyspace, "types_set",
			id.partitionKey(),
			username,
			labels,
		) {
			defaultTimeToLive(3600)
		}

		println("Inserting data")
		val first = UUID.randomUUID()
		table.insert(
			id set first,
			username set "first",
			labels set setOf("first", "premier", "1"),
		)

		val second = UUID.randomUUID()
		table.insert(
			id set second,
			username set "second",
			labels set setOf("2"),
		)

		println("Editing data")
		table.update(
			id eq second,
			labels add setOf("second"),
		)

		table.update(
			id eq second,
			labels add setOf("second"),
		)

		println("Querying data")
		val firstResult = table.select(id eq first).first()
		val secondResult = table.select(id eq second).first()

		assertEquals(setOf("first", "premier", "1"), firstResult[labels])
		assertEquals(setOf("second", "2"), secondResult[labels])
	}

	@Test
	fun map() = runTest {
		val database = Database.connect()

		println("Creating the table…")
		val id = column("id", Type.Binary.UUID)
		val labels = column("labels", Type.Collections.Map(Type.Binary.Text, Type.Binary.Text))

		val table = database.table(
			TestKeyspace, "types_map",
			id.partitionKey(),
			labels,
		) {
			defaultTimeToLive(3600)
		}

		println("Inserting data…")
		val uuid = UUID.randomUUID()
		table.update(
			id eq uuid,
			labels add mapOf("yes" to "yes", "no" to "no"),
		)

		println("Editing data…")
		coroutineScope {
			launch {
				table.update(
					id eq uuid,
					labels add mapOf("no" to "other"),
				)
			}

			launch {
				table.update(
					id eq uuid,
					labels remove setOf("yes")
				)
			}
		}

		println("Checking results…")
		assertEquals(mapOf("no" to "other"), table.select(id eq uuid, labels).first()[labels])
	}
}
