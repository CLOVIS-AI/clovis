package clovis.database.queries

import clovis.database.schema.Column
import clovis.database.schema.Table
import org.intellij.lang.annotations.Language

/**
 * Deletes values from a [Table] (CQL `DELETE`).
 *
 * Simple example:
 * ```kotlin
 * table.delete(Columns.id eq 1)
 * ```
 *
 * Remove the name of a specific user:
 * ```kotlin
 * table.delete(
 *     Columns.id eq 1,
 *     Columns.name,
 * )
 * ```
 *
 * @param where A filter expression, used to specify which row·s should be modified.
 * See [select] and [SelectExpression].
 * @param columns The columns in which values should be deleted, in the modified row·s.
 * If no columns are specified, all columns are impacted.
 * @param onlyIf An optional filter expression that allows to cancel the deletion if non-key values of the row·s do not
 * match the filter expression.
 * @param options Optional additional options.
 * @see insert
 * @see select
 * @see update
 */
suspend fun Table.delete(
	where: SelectExpression,
	vararg columns: Column<*>,
	onlyIf: SelectExpression? = null,
	options: DeleteOptions.() -> Unit = {},
) {
	@Language("CassandraQL")
	var query = """
		delete ${columnExpressions(columns.asList())}
			from $qualifiedName
			where ${where.encodedValue}
	""".trimIndent()

	if (onlyIf != null)
		query += "\n\tif ${onlyIf.encodedValue}"

	val optionsDsl = DeleteOptions()
	optionsDsl.options()

	if (optionsDsl.failIfNotExists)
		query += "\n\tif exists"

	database.execute(query)
}

private fun columnExpressions(columns: List<Column<*>>) =
	if (columns.isEmpty()) ""
	else columns.joinToString(separator = ", ") { it.name }

class DeleteOptions {
	var failIfNotExists = false
}
