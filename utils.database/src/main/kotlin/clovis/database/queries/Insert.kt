package clovis.database.queries

import clovis.database.schema.Table
import clovis.database.schema.Type
import org.intellij.lang.annotations.Language
import java.time.Instant

/**
 * Inserts some [values] into a [Table] (CQL `INSERT`).
 *
 * Simple insertion example:
 * ```kotlin
 * table.insert(
 *     Columns.id set 1,
 *     Columns.name set "My name",
 * )
 * ```
 *
 * Using options example:
 * ```kotlin
 * table.insert(
 *     Columns.id set 1,
 *     Columns.name set "My Name",
 * ) {
 *     secondsToLive = 3600
 * }
 * ```
 *
 * @param values The values inserted in the table. See [UpdateExpression.Assignment]. To use other [UpdateExpression] implementations, see [update].
 * @param options Optional request options.
 * @see update
 * @see delete
 * @see select
 */
suspend fun Table.insert(vararg values: UpdateExpression.Assignment<*>, options: InsertOptions.() -> Unit = {}) {
	@Language("CassandraQL")
	var query = """
		insert into $qualifiedName (<cols>)
			values (<vals>)
	""".trimIndent()

	val cols = values.map { it.column.name }
	val vals = values.map { it.encodedValue }

	val optionsDsl = InsertOptions()
	optionsDsl.options()

	if (optionsDsl.ignoreIfAlreadyExists)
		query += "\n\tif not exists"

	if (optionsDsl.secondsToLive != null)
		query += "\n\tusing ttl ${Type.Number.Int.encode(optionsDsl.secondsToLive!!)}"

	if (optionsDsl.timestamp != null)
		query += "\n\tusing timestamp ${Type.Dates.Timestamp.encode(optionsDsl.timestamp!!)}"

	database.execute(
		query
			.replace("<cols>", cols.joinToString(separator = ", "))
			.replace("<vals>", vals.joinToString(separator = ", "))
	)
}

class InsertOptions {
	var ignoreIfAlreadyExists = false
	var secondsToLive: Int? = null
	var timestamp: Instant? = null
}
