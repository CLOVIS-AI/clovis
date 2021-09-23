package clovis.client

import clovis.backend.core.JsonSerializer
import clovis.backend.core.UserLoginInfo
import clovis.client.Client.Anonymous
import clovis.client.Client.Anonymous.Companion.connect
import clovis.client.Client.Authenticated
import clovis.client.Client.Authenticated.Companion.createAccount
import clovis.client.Client.Authenticated.Companion.logIn
import clovis.client.Client.Authenticated.Companion.refreshTokens
import io.ktor.client.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

/**
 * Connection to the CLOVIS API.
 *
 * The connection can be either [Anonymous] or [Authenticated].
 */
sealed class Client(

	/**
	 * The URL this client is currently connected to.
	 *
	 * This URL should contain the protocol, and the host name, at the very least.
	 * If the API is not available at the server root, it can also be used to connect to a specific directory.
	 *
	 * Example values:
	 * - `"http://some.host.com"`
	 * - `"https://host.com"`
	 * - `"https://host.com/here"`
	 */
	val api: String,

	/**
	 * The Ktor [HttpClient] used to interact with the server.
	 */
	val http: HttpClient,
) {

	/**
	 * An anonymous connection to the CLOVIS API, that can be used to create an account, log in, etc.
	 *
	 * To create an [Anonymous] connection, use [connect].
	 */
	class Anonymous private constructor(api: String, client: HttpClient) : Client(api, client) {

		companion object {

			/**
			 * Connects to the CLOVIS API as an [Anonymous] client.
			 *
			 * @param api see [Client.api]
			 */
			fun connect(api: String) = Anonymous(api, createClient(accessToken = null))
		}
	}

	/**
	 * An authenticated connection to the CLOVIS API.
	 *
	 * To create an [Authenticated] connection, first create an [Anonymous] connection, then use either:
	 * - [createAccount] to create a new account,
	 * - [logIn] to log in as an existing account,
	 * - [refreshTokens] to log in with an existing refresh token.
	 *
	 * To neutralize an [Authenticated] connection, see [logOut].
	 */
	class Authenticated private constructor(api: String, client: HttpClient) : Client(api, client) {

		/**
		 * Disconnects an [Authenticated] client from the server, by deleting its refresh token.
		 * The access token is not affected.
		 *
		 * @see clovis.backend.core.Tokens
		 */
		suspend fun logOut(): String =
			post<String>("/users/logout").removeSurrounding("\"")

		companion object {

			/**
			 * Creates a new account.
			 *
			 * To log in to an account, see [logIn] and [refreshTokens].
			 */
			suspend fun Client.createAccount(email: String, password: String): Authenticated {
				val accessToken: String = post(
					"/users/create", body = UserLoginInfo(
						email = email,
						password = password
					)
				)

				return Authenticated(api, createClient(accessToken))
			}

			/**
			 * Logs in an existing account.
			 *
			 * To create an account, see [createAccount].
			 * To connect with a token, see [refreshTokens].
			 */
			suspend fun Client.logIn(email: String, password: String): Authenticated {
				val accessToken: String = put(
					"/users/login", body = UserLoginInfo(
						email = email,
						password = password,
					)
				)

				return Authenticated(api, createClient(accessToken))
			}

			/**
			 * Logs in an existing account, using the stored token.
			 *
			 * This method can also be used to get new access tokens.
			 *
			 * To create an account, see [createAccount].
			 * To log in with an email and a password, see [logIn].
			 */
			suspend fun Client.refreshTokens(): Authenticated {
				val accessToken = refreshTokens(this)

				return Authenticated(api, createClient(accessToken))
			}
		}
	}
}

private fun createClient(accessToken: String?) = HttpClient {

	install(JsonFeature) {
		serializer = KotlinxSerializer(JsonSerializer)
	}

	if (accessToken != null) {
		install(Auth) {
			bearer {
				loadTokens {
					BearerTokens(accessToken, accessToken)
				}
			}
		}
	}

}

internal expect suspend fun refreshTokens(client: Client): String
