package clovis.database.schema

import clovis.database.Database
import clovis.database.utils.asStringLiteral

data class Table(
	val database: Database,
	val name: String,
	val keyspace: String,
	val columns: List<Column<*>>,
	val options: Map<String, String> = emptyMap(),
) {
	val qualifiedName = "$keyspace.$name"
}

//region DSL

/**
 * Declares a table, and attempts to create/update it in the current [Database].
 */
suspend fun Database.table(
	keyspace: String,
	name: String,
	vararg columns: Column<*>,
	options: TableOptionsDsl.() -> Unit = {}
): Table {
	val optionsDsl = TableOptionsDsl()
	optionsDsl.options()

	return Table(
		name = name,
		keyspace = keyspace,
		columns = columns.toList(),
		options = optionsDsl.values,
		database = this,
	).also { migrateTable(it) }
}

//endregion
//region Additional settings

class TableOptionsDsl {
	internal val values = HashMap<String, String>()

	/**
	 * Sets a generic option with a [name] and a [value].
	 *
	 * Use this function if the option you need to set an option that doesn't have a typesafe function.
	 */
	fun genericOption(name: String, value: String) {
		values[name] = value
	}

	/**
	 * A free-form, human-readable comment.
	 */
	fun comment(text: String) = genericOption("comment", text.asStringLiteral())

	/**
	 * Create a Change Data Capture log on the table.
	 */
	fun changeDataCapture(create: Boolean = true) = genericOption("cdc", create.toString())

	/**
	 * Time to wait before garbage-collecting tombstones.
	 */
	fun garbageCollectGrace(seconds: Int = 864_000) = genericOption("gc_grace_seconds", seconds.toString())

	/**
	 * The target probability of false positive of the sstable bloom filters.
	 */
	fun bloomFilterChance(probabilityOfFalsePositives: Double = 0.00075) =
		genericOption("bloom_filter_fp_chance", probabilityOfFalsePositives.toString())

	/**
	 * Default expiration time in seconds.
	 */
	fun defaultTimeToLive(seconds: Int = 0) = genericOption("default_time_to_live", seconds.toString())

	/**
	 * Time before Cassandra flushes the memtables to disk.
	 */
	fun memtableFlushPeriod(milliseconds: Int = 0) =
		genericOption("memtable_flush_period_in_ms", milliseconds.toString())

}

//endregion
