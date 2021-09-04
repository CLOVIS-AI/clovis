package clovis.money.database

import clovis.core.CachedProvider
import clovis.core.Provider
import clovis.core.Result
import clovis.core.cache.Cache
import clovis.database.Database
import clovis.money.Denomination
import com.datastax.oss.driver.api.mapper.annotations.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

@Entity
@CqlName("denominations")
internal data class DbDenomination(
	@PartitionKey val id: UUID?,
	override val name: String,
	override val symbol: String,
	override val symbolBeforeValue: Boolean
) : Denomination

@Dao
internal interface DbDenominationDao {

	@Select
	fun get(id: UUID): DbDenomination?

}

/**
 * Database access for [Denomination].
 *
 * @see DatabaseDenominationCachedProvider
 */
class DatabaseDenominationProvider(database: Database) : Provider<DatabaseId<Denomination>, Denomination> {

	private val mapper = DbMoneyMapperBuilder(database.session)
		.build()
		.denominations()

	override suspend fun request(id: DatabaseId<Denomination>) = withContext(Dispatchers.IO) {
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
	override val cache: Cache<DatabaseId<Denomination>, Denomination>
) : CachedProvider<DatabaseId<Denomination>, Denomination> {

}
