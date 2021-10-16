package clovis.database.queries

import clovis.database.schema.Table
import clovis.database.schema.Type
import org.intellij.lang.annotations.Language
import java.time.Instant

/**
 * Modifies [values] from a [Table] (CQL `UPDATE`).
 *
 * @param where A filter expression, used to specify which row·s should be modified.
 * See [select] and [SelectExpression].
 * @param values The modifications that should be applied to the selected row·s.
 * See [insert] and [UpdateExpression].
 * @param onlyIf An optional filter expression that allows to cancel the update if non-key values of the rows do not match the filter expression.
 * @param options Optional additional options.
 * @see select
 * @see insert
 * @see delete
 */
suspend fun Table.update(
	where: SelectExpression,
	vararg values: UpdateExpression<*>,
	onlyIf: SelectExpression? = null,
	options: UpdateOptions.() -> Unit = {}
) {
	@Language("CassandraQL")
	var query = """
		update $qualifiedName
	""".trimIndent()

	val optionsDsl = UpdateOptions()
	optionsDsl.options()

	if (optionsDsl.secondsToLive != null)
		query += "\n\tusing ttl ${Type.Number.Int.encode(optionsDsl.secondsToLive!!)}"

	if (optionsDsl.timestamp != null)
		query += "\n\tusing timestamp ${Type.Dates.Timestamp.encode(optionsDsl.timestamp!!)}"

	query += "\n\tset " + values.joinToString(", ") { "${it.column.name} = ${it.encodedValue}" }

	query += "\n\twhere " + where.encodedValue

	if (onlyIf != null)
		query += "\n\tif ${onlyIf.encodedValue}"

	if (optionsDsl.failIfDoesntExist)
		query += "\n\tif exists"

	if (optionsDsl.failIfAlreadyExists)
		query += "\n\tif not exists"

	database.execute(query)
}

class UpdateOptions {
	var secondsToLive: Int? = null
	var timestamp: Instant? = null

	var failIfDoesntExist = false
	var failIfAlreadyExists = false
}
