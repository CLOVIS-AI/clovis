package clovis.database.queries

import clovis.database.queries.OrderExpression.Companion.ascending
import clovis.database.queries.OrderExpression.Companion.descending
import clovis.database.queries.OrderExpression.Order
import clovis.database.queries.SelectExpression.*
import clovis.database.queries.SelectExpression.Companion.and
import clovis.database.queries.SelectExpression.Companion.eq
import clovis.database.queries.SelectExpression.Companion.isOneOf
import clovis.database.queries.UpdateExpression.Assignment
import clovis.database.queries.UpdateExpression.Companion.set
import clovis.database.schema.Column

/**
 * Different kinds of expressions that can be used in database queries.
 */
sealed interface Expression

/**
 * The action of updating a field.
 *
 * | CQL equivalent | Implementation class | DSL operator |
 * |---|---|---|
 * |   | [Assignment] | [set] |
 *
 * @property column The column edited by this update.
 */
sealed class UpdateExpression<T>(val column: Column<T>) : Expression {

	internal abstract val encodedValue: String

	/**
	 * Assigns a [value] to a [column].
	 *
	 * DSL operator: [set]
	 */
	class Assignment<T>(column: Column<T>, private val value: T) : UpdateExpression<T>(column) {
		override val encodedValue get() = column.type.encode(value)
	}

	companion object {

		/**
		 * DSL operator for [Assignment].
		 *
		 * Example usage:
		 * ```kotlin
		 * someTable.insert(
		 *     Columns.id set 1,
		 *     Columns.name set "My name",
		 * )
		 * ```
		 */
		infix fun <T> Column<T>.set(value: T) = Assignment(this, value)

	}
}

/**
 * A filter on a specific value.
 *
 * | CQL equivalent | Implementation class | DSL operator |
 * |---|---|---|
 * | `=` | [Equals] | [eq] |
 * | `IN` | [Contains] | [isOneOf] |
 * | `,` | [And] | [and] |
 */
sealed class SelectExpression : Expression {

	internal abstract val encodedValue: String

	/**
	 * Tests that a [column] currently has a [value].
	 *
	 * DSL operator: [eq]
	 */
	class Equals<T>(private val column: Column<T>, private val value: T) : SelectExpression() {
		override val encodedValue get() = "${column.name} = ${column.type.encode(value)}"
	}

	/**
	 * Tests that a [column] currently has one of the [values].
	 *
	 * DSL operator: [isOneOf]
	 */
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

	/**
	 * Combines multiple [expressions] and ensures they are all verified.
	 */
	class And(private val expressions: List<SelectExpression>) : SelectExpression() {
		override val encodedValue get() = expressions.joinToString(separator = "\n\tand ") { it.encodedValue }
	}

	companion object {

		/**
		 * DSL operator for [Equals].
		 *
		 * Example usage:
		 * ```kotlin
		 * table.select(
		 *     Columns.id eq 1,
		 *     Columns.id, Columns.name,
		 * )
		 * ```
		 */
		infix fun <T> Column<T>.eq(value: T) = Equals(this, value)

		/**
		 * DSL operator for [Contains].
		 *
		 * Example usage:
		 * ```kotlin
		 * table.select(
		 *     Columns.id isOneOf listOf(1, 2, 3, 4),
		 *     Columns.id, Columns.name,
		 * )
		 * ```
		 */
		infix fun <T> Column<T>.isOneOf(value: List<T>) = Contains(this, value)

		/**
		 * DSL operator for [And].
		 *
		 * Example usage:
		 * ```kotlin
		 * table.select(
		 *     and(
		 *         Columns.id eq 5,
		 *         Columns.name eq "My name",
		 *     ),
		 *     Columns.id, Columns.name,
		 * )
		 * ```
		 */
		fun and(vararg expressions: SelectExpression) = And(expressions.asList())
	}
}

/**
 * The request of a specific sorting order.
 *
 * | CQL equivalent | Enumeration order | DSL operator |
 * |---|---|---|
 * | `ASC` | [Order.ASCENDING] | [ascending] |
 * | `DESC` | [Order.DESCENDING] | [descending] |
 *
 * @property column The column effected by this expression.
 * @property order The order in which the results should be sorted in, using the values from the [column].
 */
class OrderExpression<T>(val column: Column<T>, val order: Order) : Expression {

	enum class Order(val cqlName: String) {
		ASCENDING("asc"),
		DESCENDING("desc"),
	}

	companion object {

		/**
		 * DSL operator for [Order.ASCENDING].
		 *
		 * Example usage:
		 * ```kotlin
		 * table.select(
		 *     Columns.id eq 1,
		 * ) {
		 *     orderBy = Columns.name.ascending()
		 * }
		 * ```
		 */
		fun <T> Column<T>.ascending() = OrderExpression(this, Order.ASCENDING)

		/**
		 * DSL operator for [Order.DESCENDING].
		 *
		 * Example usage:
		 * ```kotlin
		 * table.select(
		 *     Columns.id eq 1,
		 * ) {
		 *     orderBy = Columns.name.descending()
		 * }
		 * ```
		 */
		fun <T> Column<T>.descending() = OrderExpression(this, Order.DESCENDING)
	}
}
