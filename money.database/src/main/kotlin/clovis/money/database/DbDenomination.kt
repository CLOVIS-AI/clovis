package clovis.money.database

import clovis.core.Provider
import clovis.core.Result
import clovis.core.cache.Cache
import clovis.database.Database
import clovis.database.utils.updateTable
import clovis.money.Denomination
import clovis.money.DenominationProvider
import com.datastax.oss.driver.api.mapper.annotations.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Language
import java.util.*

private const val dbDenominationName = "denominations"

@Entity
@CqlName(dbDenominationName)
data class DbDenomination(
	@PartitionKey val id: UUID?,
	override val name: String,
	override val symbol: String,
	override val symbolBeforeValue: Boolean
) : Denomination {
	companion object {
		@Language("CassandraQL")
		internal val migrations = mapOf(
			0 to """
				create table $dbDenominationName (
				id					UUID,
				name				TEXT,
				symbol				TEXT,
				symbol_before_value	BOOLEAN,
				PRIMARY KEY ( id ),
			)
			""".trimIndent(),
		)
	}
}

@Dao
internal interface DbDenominationDao {

	@Select
	fun get(id: UUID): DbDenomination?

	@Update
	fun save(denomination: DbDenomination)

}

/**
 * Database access for [Denomination].
 *
 * @see DatabaseDenominationCachedProvider
 */
class DatabaseDenominationProvider(private val database: Database) :
	Provider<DatabaseId<DbDenomination>, DbDenomination> {

	internal val mapper by lazy {
		DbMoneyMapperBuilder(database.session)
			.build()
			.denominations()
	}

	internal suspend fun checkTables() {
		database.updateTable(dbDenominationName, DbDenomination.migrations)
	}

	override suspend fun request(id: DatabaseId<DbDenomination>) = withContext(Dispatchers.IO) {
		checkTables()

		mapper.get(id.uuid)
			?.let { Result.Success(id, it) }
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

			provider.mapper.save(
				DbDenomination(
					id,
					name,
					symbol,
					symbolBeforeValue
				)
			)

			DatabaseId<DbDenomination>(id)
		}

}
