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

	//region Requests

	internal suspend inline fun <reified T> request(
		method: HttpMethod,
		urlString: String,
		block: RequestBuilderDSL = {}
	): RequestResult<T> {
		println("Requesting $method $urlString")
		return try {
			val response: T = client.request(urlString) {
				this.method = method
				block()
			}
			Success(response)
		} catch (e: ClientRequestException) {
			HttpFailure(e.response.status, e.response.readText(), e)
		}
	}

	internal suspend inline fun <reified T> get(
		urlString: String,
		block: RequestBuilderDSL = {}
	) = request<T>(HttpMethod.Get, urlString, block)

	//endregion
}
