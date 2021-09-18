package clovis.database.queries

import clovis.database.schema.Table
import clovis.database.schema.Type
import org.intellij.lang.annotations.Language
import java.time.Instant

class InsertOptions {
	var ignoreIfAlreadyExists = false
	var secondsToLive: Int? = null
	var timestamp: Instant? = null
}

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
