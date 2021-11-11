package clovis.server

import clovis.core.Provider
import clovis.core.cache.DirectCache
import clovis.core.cache.cachedInMemory
import clovis.database.Database
import clovis.database.queries.SelectExpression.Companion.and
import clovis.database.queries.SelectExpression.Companion.eq
import clovis.database.queries.select
import clovis.database.schema.*
import clovis.database.utils.get
import clovis.money.Denomination
import clovis.money.database.DatabaseDenominationProvider
import io.ktor.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.toList

/**
 * Manage local [Provider] implementations based on user credentials.
 */
class UserProviders(
	private val database: Database,
	private val scope: CoroutineScope,
) {

	private val providersByUserBuilder = scope.async {
		database.table(
			"clovis_remote_providers", "remote_providers_by_user",
			Columns.userId.partitionKey(),
			Columns.providerId.clusteringKey(),
			Columns.providerType,
		)
	}

	/**
	 * Finds a provider with a given [id] belonging to a given [user].
	 *
	 * The provider is searched in the database.
	 * This is a server-side only method (searches for persisted providers).
	 */
	suspend fun findProvider(user: User, id: String): Provider<*> {
		val providersByUser = providersByUserBuilder.await()

		// 1. Does this user have a provider with this ID?
		val matchingProviderTypes = providersByUser.select(
			and(
				Columns.userId eq user.id,
				Columns.providerId eq id,
			),
			Columns.providerType,
			Columns.accountOrigin,
		).toList()
		check(matchingProviderTypes.isNotEmpty()) { "No provider could be found with the provided ID for this user." }
		check(matchingProviderTypes.size < 2) { "Multiple providers were found with the provided ID for this user, this shouldn't be possible. Found ${matchingProviderTypes.size} providers." }
		val uniqueRow = matchingProviderTypes.first()
		val providerType = uniqueRow[Columns.providerType]

		// 2. Find the exact provider type and instantiate it
		return when (val accountOrigin = uniqueRow[Columns.accountOrigin]) {
			ACCOUNT_ORIGIN_CLOVIS -> when (providerType) {
				Denomination.PROVIDER_TYPE_ID ->
					DatabaseDenominationProvider(database, DirectCache<Denomination>().cachedInMemory(scope), scope)
				else ->
					error("The requested provider has an unrecognized provider type: ‘$providerType’")
			}
			else -> error("The requested provider has an unrecognized account type: ‘$accountOrigin’")
		}
	}

	private object Columns {
		val userId = column("uid", Type.Binary.UUID)
		val providerId = column("pid", Type.Binary.Text)
		val providerType = column("ptype", Type.Binary.Text)
		val accountOrigin = column("origin", Type.Binary.Text)
	}

	companion object {
		private const val ACCOUNT_ORIGIN_CLOVIS = "CLOVIS"
	}
}

fun ApplicationCall.providerId(): String = parameters["providerId"]
	?: error("The parameter 'providerId' is missing. Without it, it is not possible to execute remote provider operations.")

inline fun <reified T : Provider<*>> Provider<*>.ensureIs(): T {
	if (this is T)
		return this
	else error("The requested provider doesn't have the type ${T::class}: ${this::class}")
}
