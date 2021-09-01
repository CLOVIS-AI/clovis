package clovis.money

/**
 * The way a monetary sum should be displayed the user.
 *
 * Monetary sums are always stored as integers (see [Money]), even for denominations that are displayed with cents or similar.
 * Instead, this interface stores the rules for displaying a monetary sum.
 */
interface Denomination {

	val name: String
	val symbol: String

	val symbolBeforeValue: Boolean

	/**
	 * Converts the provided [sum] as a [String].
	 *
	 * @param sum The amount of money that should be displayed
	 * @param includeSymbol `true` if the symbol should be included
	 *
	 * @see Money.toString
	 */
	fun toString(sum: Long, includeSymbol: Boolean = true): String
}
