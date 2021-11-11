package clovis.money

/**
 * A monetary sum.
 *
 * @property amount The monetary sum, always stored as an integer.
 * Use the [denomination] property to display the value to the user.
 * @property denomination The denomination this sum should be interpreted as.
 */
data class Money(
	val amount: Long,
	val denomination: Denomination,
) {

	/**
	 * Converts this amount to a [String], using [denomination].[toString][Denomination.toString].
	 */
	fun toString(includeSymbol: Boolean) = denomination.toString(amount, includeSymbol)
	override fun toString() = toString(true)
}
