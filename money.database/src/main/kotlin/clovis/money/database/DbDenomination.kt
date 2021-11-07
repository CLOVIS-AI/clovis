package clovis.money.database

import clovis.core.Progress
import clovis.core.Ref
import clovis.core.cache.Cache
import clovis.database.Database
import clovis.database.queries.SelectExpression.Companion.eq
import clovis.database.queries.UpdateExpression.Companion.set
import clovis.database.queries.insert
import clovis.database.queries.select
import clovis.database.schema.Type
import clovis.database.schema.column
import clovis.database.schema.partitionKey
import clovis.database.schema.table
import clovis.database.utils.get
import clovis.money.Denomination
import clovis.money.DenominationCreator
import clovis.money.DenominationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
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

data class DbDenominationRef(
	internal val id: UUID,
	override val provider: DatabaseDenominationProvider,
) : Ref<Denomination> {
	override fun encodeRef() = id.toString()
}

/**
 * Implementation of the [DenominationProvider] API using the CLOVIS Database.
 */
class DatabaseDenominationProvider(
	private val database: Database,
	override val cache: Cache<Denomination>,
	scope: CoroutineScope,
) : DenominationProvider {

	private val denominationsMigrator = scope.async {
		database.table(
			MoneyKeyspace, "denominations",
			Columns.id.partitionKey(),
			Columns.name,
			Columns.symbol,
			Columns.symbolBeforeValue,
		)
	}

	override fun directRequest(ref: Ref<Denomination>): Flow<Progress<Denomination>> = flow {
		require(ref is DbDenominationRef) { "Illegal reference type: $ref" }

		val denominations = denominationsMigrator.await()

		val result = denominations.select(Columns.id eq ref.id)
			.firstOrNull()
			?.let {
				Progress.Success(
					ref,
					Denomination(
						name = it[Columns.name],
						symbol = it[Columns.symbol],
						symbolBeforeValue = it[Columns.symbolBeforeValue],
					)
				)
			}
			?: Progress.NotFound(ref, message = null)

		emit(result)
	}

	override fun decodeRef(encoded: String) = DbDenominationRef(UUID.fromString(encoded), this)

	override val creator = object : DenominationCreator {
		override suspend fun create(name: String, symbol: String, symbolBeforeValue: Boolean): DbDenominationRef {
			val denominations = denominationsMigrator.await()

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
