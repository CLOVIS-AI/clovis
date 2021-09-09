package clovis.money

import clovis.core.CachedProvider
import clovis.core.Id

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
	fun toString(sum: Long, includeSymbol: Boolean = true): String = when {
		includeSymbol && symbolBeforeValue -> "$symbol$sum"
		includeSymbol && !symbolBeforeValue -> "$sum$symbol"
		else -> "$sum"
	}
}

interface DenominationProvider<I : Id<D>, D : Denomination> : CachedProvider<I, D> {

	/**
	 * Creates a new [Denomination] and stores it.
	 * @return The ID of the created denomination.
	 */
	suspend fun create(name: String, symbol: String, symbolBeforeValue: Boolean): I

}
