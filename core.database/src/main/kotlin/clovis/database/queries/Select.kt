package clovis.database.queries

import clovis.database.schema.Column
import clovis.database.schema.Table
import clovis.database.schema.Type
import clovis.database.utils.toFlow
import com.datastax.oss.driver.api.core.cql.Row
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language

class SelectOptions {
	var allowFiltering = false
	var limit: Int? = null
	var orderBy: OrderExpression<*>? = null
}

private fun columnExpressions(columns: List<Column<*>>) =
	if (columns.isEmpty()) "*"
	else columns.joinToString(separator = ", ") { it.name }

suspend fun Table.select(
	where: SelectExpression,
	vararg columns: Column<*>,
	options: SelectOptions.() -> Unit = {}
): Flow<Row> {
	@Language("CassandraQL")
	var query = """
		select ${columnExpressions(columns.asList())}
			from $qualifiedName
			where ${where.encodedValue}
	""".trimIndent()

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
