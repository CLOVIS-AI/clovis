package clovis.money.database

import clovis.core.Progress
import clovis.core.Ref
import clovis.core.cache.Cache
import clovis.database.Database
import clovis.database.queries.SelectExpression.Companion.eq
import clovis.database.queries.UpdateExpression.Companion.set
import clovis.database.queries.insert
import clovis.database.queries.select
import clovis.database.schema.*
import clovis.database.utils.get
import clovis.money.Denomination
import clovis.money.DenominationCreator
import clovis.money.DenominationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import java.util.*

private object Columns {
	val id = column("id", Type.Binary.UUID)
	val name = column("name", Type.Binary.Text)
	val symbol = column("symb", Type.Binary.Text)
	val symbolBeforeValue = column("sbfv", Type.Binary.Boolean)
}

data class DbDenominationRef(internal val id: UUID, override val provider: DatabaseDenominationProvider) :
	Ref<DbDenominationRef, Denomination> {
	override fun directRequest(): Flow<Progress<DbDenominationRef, Denomination>> = flow {
		provider.checkTables()

		val result = provider.denominations.select(Columns.id eq id)
			.firstOrNull()
			?.let {
				Progress.Success(
					this@DbDenominationRef,
					Denomination(
						name = it[Columns.name],
						symbol = it[Columns.symbol],
						symbolBeforeValue = it[Columns.symbolBeforeValue],
					)
				)
			}
			?: Progress.NotFound(this@DbDenominationRef, message = null)

		emit(result)
	}
}

class DatabaseDenominationProvider(
	private val database: Database,
	override val cache: Cache<DbDenominationRef, Denomination>
) : DenominationProvider<DbDenominationRef> {

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

	override val creator = object : DenominationCreator<DbDenominationRef> {
		override suspend fun create(name: String, symbol: String, symbolBeforeValue: Boolean): DbDenominationRef {
			checkTables()

			val id = UUID.randomUUID()

			denominations.insert(
				Columns.id set id,
				Columns.name set name,
				Columns.symbol set symbol,
				Columns.symbolBeforeValue set symbolBeforeValue,
			)

			return DbDenominationRef(id, this@DatabaseDenominationProvider)
		}
	}

}
