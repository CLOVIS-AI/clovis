package clovis.client

import clovis.client.Client.Authenticated.Companion.createAccount
import clovis.client.Client.Authenticated.Companion.logIn
import clovis.logger.logger
import clovis.logger.warn
import kotlin.random.Random

private object ClientTestUtils
private val logger = logger(ClientTestUtils)

/**
 * The password used for accounts created as part of unit tests.
 *
 * Should **never** be used for any real account, as it is public knowledge.
 */
const val TEST_PASSWORD = "user-test-password"

/**
 * [Client.Anonymous] instance suitable for tests.
 *
 * This instance is automatically connected to the locally-running development build of the backend.
 *
 * @see authenticatedTestClient
 * @see TEST_PASSWORD
 */
fun anonymousTestClient() = Client.Anonymous.connect("http://localhost:8000")

/**
 * [Client.Authenticated] instance suitable for tests.
 *
 * If [email] is given, then this method will connect to an already existing account (or attempts to create it if it doesn't exist).
 * Otherwise, this method creates a new account everytime it is called.
 *
 * This instance is automatically connected to the locally-running development build of the backend.
 *
 * @see anonymousTestClient
 * @see TEST_PASSWORD
 */
suspend fun authenticatedTestClient(email: String? = null): Client.Authenticated {
	val testClient = anonymousTestClient()

	return when (email) {
		null -> testClient.createAccount("test-user-${Random.nextInt()}@test-email.com", TEST_PASSWORD)
		else -> {
			try {
				testClient.logIn(email, TEST_PASSWORD)
			} catch (e: Exception) {
				logger.warn { "authenticatedTestClient: requested to connect to the user '$email', but it failed; trying to create it now. Original error: $e" }
				testClient.createAccount(email, TEST_PASSWORD)
			}
		}
	}
}
