package clovis.database.queries

import clovis.database.schema.Column

sealed interface Expression

sealed class UpdateExpression<T>(val column: Column<T>) : Expression {

	internal abstract val encodedValue: String

	class Assignment<T>(column: Column<T>, private val value: T) : UpdateExpression<T>(column) {
		override val encodedValue get() = column.type.encode(value)
	}

	companion object {
		infix fun <T> Column<T>.set(value: T) = Assignment(this, value)
	}

}

sealed class SelectExpression : Expression {

	internal abstract val encodedValue: String

	class Equals<T>(private val column: Column<T>, private val value: T) : SelectExpression() {
		override val encodedValue get() = "${column.name} = ${column.type.encode(value)}"
	}

	class Contains<T>(private val column: Column<T>, private val values: List<T>) : SelectExpression() {
		override val encodedValue
			get() = "${column.name} in (${
				values.joinToString(separator = ", ") {
					column.type.encode(
						it
					)
				}
			})"
	}

	class And(private val expressions: List<SelectExpression>) : SelectExpression() {
		override val encodedValue get() = expressions.joinToString(separator = "\n\tand ") { it.encodedValue }
	}

	companion object {
		infix fun <T> Column<T>.eq(value: T) = Equals(this, value)
		infix fun <T> Column<T>.isOneOf(value: List<T>) = Contains(this, value)
		fun and(vararg expressions: SelectExpression) = And(expressions.asList())
	}
}

class OrderExpression<T>(val column: Column<T>, val order: Order) : Expression {

	enum class Order(val cqlName: String) {
		ASCENDING("asc"),
		DESCENDING("desc"),
	}

	companion object {
		fun <T> Column<T>.ascending() = OrderExpression(this, Order.ASCENDING)
		fun <T> Column<T>.descending() = OrderExpression(this, Order.DESCENDING)
	}
}
