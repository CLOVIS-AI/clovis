package clovis.database.queries

import clovis.database.schema.Column
import clovis.database.schema.Table
import clovis.database.schema.Type
import clovis.database.utils.toFlow
import com.datastax.oss.driver.api.core.cql.Row
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language

/**
 * Selects data from a specified [Table] (CQL `SELECT`).
 * The data is filtered according to the [where] expression, and the requested [columns] are returned.
 *
 * Simple example:
 * ```kotlin
 * table.select(
 *     Columns.id eq 1,
 *     Columns.id, Columns.name,
 * )
 * ```
 *
 * Example with additional options:
 * ```kotlin
 * table.select(
 *     Columns.id isOneOf listOf(1, 2, 3, 4, 5, 6),
 *     Columns.id, Columns.name,
 * ) {
 *     limit = 3
 * }
 * ```
 *
 * Example with multiple filters:
 * ```kotlin
 * table.select(
 *     and(
 *         Columns.id eq 1,
 *         Columns.name eq "My user",
 *     ), Columns.id, Columns.name,
 * )
 * ```
 *
 * Query all data from the table:
 * ```kotlin
 * table.select(
 *     where = null,
 *     Columns.id, Columns.name,
 * )
 * ```
 *
 * @param where Which results should be included in the search?
 * For more information on filters, see [SelectExpression].
 * To use multiple filters, see [SelectExpression.and].
 * To query all data from the table, explicitly set this parameter to `null` (not recommended for performance reasons).
 * @param columns Which columns of the [Table] should be queried?
 * Only the specified columns are available in the returned [Flow] of [Row]s.
 * When no columns are specified, this method will query for all methods. For backward-compatibility and performance reasons, this is not recommended.
 * @param options Optional additional options.
 * @see insert
 * @see update
 * @see delete
 */
suspend fun Table.select(
	where: SelectExpression?,
	vararg columns: Column<*>,
	options: SelectOptions.() -> Unit = {}
): Flow<Row> {
	@Language("CassandraQL")
	var query = """
		select ${columnExpressions(columns.asList())}
			from $qualifiedName
	""".trimIndent()

	if (where != null)
		query += "\n\twhere ${where.encodedValue}"

	val optionsDsl = SelectOptions()
	optionsDsl.options()

	if (optionsDsl.orderBy != null)
		query += "\n\torder by ${optionsDsl.orderBy!!.column.name} ${optionsDsl.orderBy!!.order.cqlName}"

	if (optionsDsl.limit != null)
		query += "\n\tlimit ${Type.Number.Int.encode(optionsDsl.limit!!)}"

	if (optionsDsl.allowFiltering)
		query += "\n\tallow filtering"

	return database.execute(query).toFlow()
}

class SelectOptions {
	var allowFiltering = false
	var limit: Int? = null
	var orderBy: OrderExpression<*>? = null
}

private fun columnExpressions(columns: List<Column<*>>) =
	if (columns.isEmpty()) "*"
	else columns.joinToString(separator = ", ") { it.name }
