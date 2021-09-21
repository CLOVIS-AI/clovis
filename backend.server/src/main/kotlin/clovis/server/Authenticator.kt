package clovis.server

import at.favre.lib.crypto.bcrypt.BCrypt
import clovis.database.Database
import clovis.database.queries.SelectExpression.Companion.eq
import clovis.database.queries.UpdateExpression.Companion.set
import clovis.database.queries.insert
import clovis.database.queries.select
import clovis.database.schema.*
import clovis.database.utils.get
import clovis.database.utils.suspendLazy
import clovis.logger.WithLogger
import clovis.logger.error
import clovis.logger.info
import kotlinx.coroutines.flow.firstOrNull
import java.util.*

private const val AuthKeyspace = "clovis_users"

private object Columns {
	val id = column("id", Type.Binary.UUID)
	val email = column("email", Type.Binary.Text)
	val password = column("pass", Type.Binary.Text)
}

class Authenticator(
	private val database: Database,
) {

	private val users = suspendLazy {
		database.table(
			AuthKeyspace, "users",
			Columns.id.partitionKey(),
			Columns.email,
			Columns.password,
		)
	}

	private val usersByEmail = suspendLazy {
		users.get().manualView(
			"users_by_email",
			Columns.email.partitionKey(),
			Columns.id,
		)
	}

	suspend fun findUserByEmail(email: String): User? = usersByEmail.get().select(
		Columns.email eq email,
		Columns.id
	).firstOrNull()?.let { row -> User(row[Columns.id]) }

	/**
	 * Creates a new [User].
	 *
	 * The function either successfully returns a [User], or fails with an exception.
	 */
	suspend fun createUser(email: String, password: String): User {
		log.info { "Creating user '$email'" }

		check(findUserByEmail(email) == null) { "This email address is not available." }

		val id = UUID.randomUUID()

		users.get().insert(
			Columns.id set id,
			Columns.email set email,
			Columns.password set hash(password),
		)

		usersByEmail.get().insert(
			Columns.id set id,
			Columns.email set email,
		)

		return User(id)
	}

	/**
	 * Logins with user credentials.
	 *
	 * The function either successfully returns a [User], or fails with an exception.
	 */
	suspend fun login(email: String, password: String): User {
		val usersTable = users.get()
		val usersByEmailTable = usersByEmail.get()

		val user = findUserByEmail(email)
		checkNotNull(user) {
			log.info { "Login attempt by '$email' was refused because that email address doesn't match any known." }
			"The provided credentials are incorrect."
		}

		val userData = usersTable.select(Columns.id eq user.id, Columns.password)
			.firstOrNull()
			?.let { it[Columns.password] }
		checkNotNull(userData) {
			log.error { "The user ${user.id} can be found from their email ($email) in ${usersByEmailTable.qualifiedName}, but not in the general table ${usersTable.qualifiedName}." }
			"Internal error. Please contact the administrator and provide them your email address."
		}

		check(checkHash(password, userData)) {
			log.info { "Login attempt by '$email' was refused because their password is incorrect." }
			"The provided credentials are incorrect."
		}

		return User(user.id)
	}

	private fun hash(clearText: String): String {
		return BCrypt.withDefaults()
			.hashToString(12, clearText.toCharArray())
	}

	private fun checkHash(clearText: String, hash: String) = BCrypt.verifyer()
		.verify(
			clearText.toCharArray(),
			hash.toCharArray()
		).verified

	companion object : WithLogger()
}
