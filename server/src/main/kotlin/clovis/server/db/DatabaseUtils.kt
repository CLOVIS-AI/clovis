package clovis.server.db

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import clovis.server.DatabaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

private var createdTables = false
suspend fun ensureTablesExist() = withContext(Dispatchers.IO) {
	if (!createdTables)
		SchemaUtils.create(
			Users,
			Profiles,
		)

	createdTables = true
}

suspend inline fun <T> withDatabase(crossinline statement: suspend Transaction.() -> T): Either<DatabaseException, T> =
	try {
		val result = newSuspendedTransaction(Dispatchers.IO, db = dbConnection, transactionIsolation = null) {
			ensureTablesExist()

			statement()
		}

		Right(result)
	} catch (e: ExposedSQLException) {
		val failure = when {
			e.message?.contains("MySQLIntegrityConstraintViolationException") == true -> DatabaseException.ConstraintViolation(
				e
			)
			else -> DatabaseException.Unknown(e)
		}
		Left(failure)
	}
