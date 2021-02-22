package clovis.server.db

import clovis.server.dbConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync

private var createdTables = false
suspend fun ensureTablesExist() = withContext(Dispatchers.IO) {
	if (!createdTables)
		SchemaUtils.create(
			Users,
			Profiles,
		)

	createdTables = true
}

suspend inline fun <T> withDatabaseAsync(crossinline statement: Transaction.() -> T) =
	suspendedTransactionAsync(Dispatchers.IO, db = dbConnection, transactionIsolation = null) {
		ensureTablesExist()

		statement()
	}

suspend inline fun <T> withDatabase(crossinline statement: Transaction.() -> T) =
	withDatabaseAsync(statement).await()
