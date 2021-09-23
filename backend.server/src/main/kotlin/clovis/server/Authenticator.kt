package clovis.server

import at.favre.lib.crypto.bcrypt.BCrypt
import clovis.backend.core.Tokens
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
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import kotlinx.coroutines.flow.firstOrNull
import java.time.Instant
import java.util.*
import kotlin.time.toJavaDuration

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

	//region Account access

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

	//endregion
	//region Tokens

	private val secretKey = System.getenv("clovis_jwt_secret")
		?: error("The environment variable 'clovis_jwt_secret' is missing.")
	private val algorithm = Algorithm.HMAC256(secretKey)
	private val verifier = JWT
		.require(algorithm)
		.withIssuer(CLOVIS_ISSUER)
		.build()

	private fun createToken(user: User, config: (JWTCreator.Builder) -> JWTCreator.Builder): String =
		JWT.create()
			.withIssuer(CLOVIS_ISSUER)
			.withClaim("userId", user.id.toString())
			.let(config)
			.sign(algorithm)

	/**
	 * Generate an Access Token for [user].
	 *
	 * @see Tokens
	 */
	@OptIn(kotlin.time.ExperimentalTime::class)
	fun createAccessToken(user: User): String = createToken(user) { builder ->
		builder
			.withClaim("type", "access")
			.withExpiresAt(Date.from(Instant.now() + Tokens.accessTokenLifetime.toJavaDuration()))
	}

	/**
	 * Generate a Refresh Token for [user].
	 *
	 * @see Tokens
	 */
	@OptIn(kotlin.time.ExperimentalTime::class)
	fun createRefreshToken(user: User): String = createToken(user) { builder ->
		builder
			.withClaim("type", "refresh")
			.withExpiresAt(Date.from(Instant.now() + Tokens.refreshTokenLifetime.toJavaDuration()))
	}

	private fun checkToken(token: Payload): User {
		check(token.issuer == CLOVIS_ISSUER) { "The token isn't identified by the correct issuer." }

		val now = Date.from(Instant.now())
		check(token.expiresAt >= now) { "The token has expired." }

		val userId = token.getClaim("userId").asString()
		requireNotNull(userId) { "The token doesn't have a 'userId' claim." }
		return User(UUID.fromString(userId))
	}

	/**
	 * Checks the validity of a [token].
	 *
	 * If [token] is a valid access token, a [User] is returned. Otherwise, the method fails with an exception.
	 * @see Tokens
	 */
	fun checkAccessToken(token: Payload): User {
		// For performance reasons, the access token should be checked without database access

		check(token.getClaim("type").asString() == "access") { "The token isn't an access token." }

		return checkToken(token)
	}

	/**
	 * Checks the validity of a [token]
	 *
	 * @see checkAccessToken
	 */
	fun checkAccessToken(token: String): User = checkAccessToken(JWT.decode(token))

	/**
	 * Checks the validity of a [token].
	 *
	 * If [token] is a valid refresh token, a [User] is returned. Otherwise, the method fails with an exception.
	 * @see Tokens
	 */
	fun checkRefreshToken(token: Payload): User {
		// Checking a refresh token should be rarer, so it is not really an issue if it is slow

		check(token.getClaim("type").asString() == "refresh") { "The token isn't a refresh token." }

		return checkToken(token)
	}

	/**
	 * Checks the validity of a [token].
	 *
	 * @see checkRefreshToken
	 */
	fun checkRefreshToken(token: String): User = checkRefreshToken(JWT.decode(token))

	//endregion
	//region Hashing

	private fun hash(clearText: String): String {
		return BCrypt.withDefaults()
			.hashToString(12, clearText.toCharArray())
	}

	private fun checkHash(clearText: String, hash: String) = BCrypt.verifyer()
		.verify(
			clearText.toCharArray(),
			hash.toCharArray()
		).verified

	//endregion

	companion object : WithLogger() {
		private const val CLOVIS_ISSUER = "braindot.clovis"
	}
}
