package clovis.database.utils

import clovis.database.schema.Column
import clovis.database.schema.Type
import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.oss.driver.api.core.type.reflect.GenericType

/**
 * Gets the value of the [column], decoded via the given [type].
 */
fun <T> Row.get(column: String, type: Type<T>): T {
	val value = get(column, GenericType.STRING)
	requireNotNull(value) { "Expected a $type, found '$value'.\nThe available table names are: $columnDefinitions" }

	return type.decode(value)
}

/**
 * Gets the value of the [column].
 */
fun <T> Row.get(column: Column<T>): T = get(column.name, column.type)
