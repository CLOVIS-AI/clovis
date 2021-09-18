package clovis.database.queries

import clovis.database.schema.Column
import clovis.database.schema.Table
import org.intellij.lang.annotations.Language

private fun columnExpressions(columns: List<Column<*>>) =
	if (columns.isEmpty()) ""
	else columns.joinToString(separator = ", ") { it.name }

class DeleteOptions {
	var failIfNotExists = false
}

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
