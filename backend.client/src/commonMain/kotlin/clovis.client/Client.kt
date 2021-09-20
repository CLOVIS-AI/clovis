package clovis.client

import clovis.client.Client.Anonymous
import clovis.client.Client.Authenticated
import io.ktor.client.*

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
	 */
	class Anonymous private constructor(api: String, client: HttpClient) : Client(api, client)

	/**
	 * An authenticated connection to the CLOVIS API.
	 */
	class Authenticated private constructor(api: String, client: HttpClient) : Client(api, client)

}
