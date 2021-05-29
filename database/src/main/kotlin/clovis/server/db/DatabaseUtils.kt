package clovis.server.db

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import clovis.server.db.tables.Profiles
import clovis.server.db.tables.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

private var createdTables = false
internal suspend fun ensureTablesExist() = withContext(Dispatchers.IO) {
	if (!createdTables)
		SchemaUtils.create(
			Users,
			Profiles,
		)

	createdTables = true
}

internal suspend inline fun <T> withDatabase(crossinline statement: suspend Transaction.() -> T): Either<DatabaseProblem, T> =
	try {
		val result = newSuspendedTransaction(Dispatchers.IO, db = dbConnection, transactionIsolation = null) {
			ensureTablesExist()

			statement()
		}

		Right(result)
	} catch (e: ExposedSQLException) {
		val failure = when {
			e.message?.contains("MySQLIntegrityConstraintViolationException") == true -> DatabaseProblem.ConstraintViolation(
				e
			)
			else -> DatabaseProblem.Unknown(e)
		}
		Left(failure)
	}
