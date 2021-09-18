package clovis.money.database

import clovis.core.Provider
import clovis.core.Result
import clovis.core.cache.Cache
import clovis.database.Database
import clovis.database.queries.SelectExpression.Companion.eq
import clovis.database.queries.UpdateExpression.Companion.set
import clovis.database.queries.insert
import clovis.database.queries.select
import clovis.database.schema.*
import clovis.database.utils.get
import clovis.money.Denomination
import clovis.money.DenominationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.*

private object Columns {
	val id = column("id", Type.Binary.UUID)
	val name = column("name", Type.Binary.Text)
	val symbol = column("symb", Type.Binary.Text)
	val symbolBeforeValue = column("sbfv", Type.Binary.Boolean)
}

class DbDenomination(
	override val name: String,
	override val symbol: String,
	override val symbolBeforeValue: Boolean
) : Denomination

/**
 * Database access for [Denomination].
 *
 * @see DatabaseDenominationCachedProvider
 */
class DatabaseDenominationProvider(private val database: Database) :
	Provider<DatabaseId<DbDenomination>, DbDenomination> {

	internal lateinit var denominations: Table

	internal suspend fun checkTables() {
		if (!::denominations.isInitialized)
			denominations = database.table(
				MoneyKeyspace, "denominations",
				Columns.id.partitionKey(),
				Columns.name,
				Columns.symbol,
				Columns.symbolBeforeValue,
			)
	}

	override suspend fun request(id: DatabaseId<DbDenomination>) = withContext(Dispatchers.IO) {
		checkTables()

		denominations.select(Columns.id eq id.uuid)
			.firstOrNull()
			?.let {
				Result.Success(
					id,
					DbDenomination(it[Columns.name], it[Columns.symbol], it[Columns.symbolBeforeValue])
				)
			}
			?: Result.NotFound(id, message = null)
	}
}

/**
 * Database cached access for [Denomination].
 *
 * @see DatabaseDenominationProvider
 */
class DatabaseDenominationCachedProvider(
	override val provider: DatabaseDenominationProvider,
	override val cache: Cache<DatabaseId<DbDenomination>, DbDenomination>
) : DenominationProvider<DatabaseId<DbDenomination>, DbDenomination> {

	override suspend fun create(name: String, symbol: String, symbolBeforeValue: Boolean) =
		withContext(Dispatchers.IO) {
			provider.checkTables()

			val id = UUID.randomUUID()

			provider.denominations.insert(
				Columns.id set id,
				Columns.name set name,
				Columns.symbol set symbol,
				Columns.symbolBeforeValue set symbolBeforeValue,
			)

			DatabaseId<DbDenomination>(id)
		}

}
