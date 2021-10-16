package clovis.database.utils

import clovis.database.schema.Column
import clovis.database.schema.Type
import com.datastax.oss.driver.api.core.cql.Row

/**
 * Gets the value of the [column], decoded via the given [type].
 */
fun <T> Row.get(column: String, type: Type<T>): T {
	val value = get(column, type.codec)
	requireNotNull(value) { "Expected a $type, found '$value'.\nThe available table names are: $columnDefinitions" }

	return value
}

/**
 * Gets the value of the [column].
 */
operator fun <T> Row.get(column: Column<T>): T = get(column.name, column.type)
