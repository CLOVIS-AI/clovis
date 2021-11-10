package clovis.money

import clovis.core.Provider
import clovis.core.ProviderFeature
import clovis.core.Ref
import kotlinx.serialization.Serializable

/**
 * The way a monetary sum should be displayed the user.
 *
 * Monetary sums are always stored as integers (see [Money]), even for denominations that are displayed with cents or similar.
 * Instead, this interface stores the rules for displaying a monetary sum.
 */
@Serializable
data class Denomination(
	val name: String,
	val symbol: String,
	val symbolBeforeValue: Boolean,
) {

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

/**
 * The object responsible for querying [Denomination]s.
 */
interface DenominationProvider : Provider<Denomination> {
	val creator: DenominationCreator?

	companion object {
		const val CREATOR_FEATURE = "CREATOR"
	}
}

/**
 * A [DenominationProvider] that can [create] new [Denomination]s.
 */
interface DenominationCreator : ProviderFeature {

	/**
	 * Creates a new [Denomination].
	 */
	suspend fun create(name: String, symbol: String, symbolBeforeValue: Boolean): Ref<Denomination>

}
