package clovis.client

import clovis.backend.core.JsonSerializer
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Performs an HTTP request to the [Client].
 *
 * @param url The URL of requested resource, starting at the API root, such that [Client.api] + [url] is the URL to the expected resource.
 * @param body The body of the request. If not `null`, it will be serialized to JSON via [JsonSerializer].
 */
suspend inline fun <reified Out> Client.request(
	method: HttpMethod,
	url: String,
	body: Any? = null,
	block: HttpRequestBuilder.() -> Unit = {}
): Out = http.request(api + url) {
	this.method = method

	if (body != null) {
		contentType(ContentType.Application.Json)
		this.body = body
	}

	block()
}

/**
 * Performs an HTTP GET request to the [Client].
 *
 * The parameters are documented in the general method, [request].
 *
 * See [the MDN documentation](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/GET).
 */
suspend inline fun <reified Out> Client.get(
	url: String,
	block: HttpRequestBuilder.() -> Unit = {},
) = request<Out>(HttpMethod.Get, url, body = null, block)

/**
 * Performs an HTTP POST request to the [Client].
 *
 * The parameters are documented in the general method, [request].
 *
 * Unlike [PUT requests][put], POST is not idempotent (executing the same request multiple times will create multiple objects).
 * See [the MDN documentation](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/POST).
 */
suspend inline fun <reified Out> Client.post(
	url: String,
	body: Any? = null,
	block: HttpRequestBuilder.() -> Unit = {},
) = request<Out>(HttpMethod.Post, url, body, block)

/**
 * Performs an HTTP PUT request to the [Client].
 *
 * The parameters are documented in the general method, [request].
 *
 * Unlike [POST requests][post], PUT is idempotent (executing the same request multiple times only has an effect once).
 * See [the MDN documentation](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/PUT).
 */
suspend inline fun <reified Out> Client.put(
	url: String,
	body: Any? = null,
	block: HttpRequestBuilder.() -> Unit = {},
) = request<Out>(HttpMethod.Put, url, body, block)

/**
 * Performs an HTTP DELETE request to the [Client].
 *
 * The parameters are documented in the general method, [request].
 *
 * See [the MDN documentation](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/DELETE).
 */
suspend inline fun <reified Out> Client.delete(
	url: String,
	body: Any? = null,
	block: HttpRequestBuilder.() -> Unit = {},
) = request<Out>(HttpMethod.Delete, url, body, block)

/**
 * Performs an HTTP PATCH request to the [Client].
 *
 * The parameters are documented in the general method, [request].
 *
 * Similar to [PUT requests][put], however PATCH allows partial updates.
 * See [the MDN documentation](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/PATCH).
 */
suspend inline fun <reified Out> Client.patch(
	url: String,
	body: Any? = null,
	block: HttpRequestBuilder.() -> Unit = {},
) = request<Out>(HttpMethod.Patch, url, body, block)
