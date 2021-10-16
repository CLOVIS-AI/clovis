package clovis.money

/**
 * The movement of [Money] from a [source] to a [destination].
 */
interface Transaction {
	val source: Wallet
	val destination: Wallet

	/**
	 * The difference of money in the [source] wallet.
	 */
	val input: Money

	/**
	 * The difference of money in the [destination] wallet.
	 */
	val output: Money
}
