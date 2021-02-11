package clovis.client

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import io.ktor.utils.io.core.*

class Client(val url: Url) : Closeable {

	internal val client = HttpClient {
		install(JsonFeature) {
			serializer = KotlinxSerializer()
		}
	}

	/**
	 * Closes this client.
	 * @see HttpClient.close
	 */
	override fun close() = client.close()
}
