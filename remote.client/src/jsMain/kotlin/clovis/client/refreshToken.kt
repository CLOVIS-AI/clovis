package clovis.client

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.INCLUDE
import org.w3c.fetch.RequestCredentials
import org.w3c.fetch.RequestInit

internal actual suspend fun refreshTokens(client: Client): String {
	val response = window.fetch(
		"${client.api}/users/refreshToken",
		RequestInit(
			method = "POST",
			credentials = RequestCredentials.INCLUDE
		)
	).await()

	return when {
		response.ok -> response.text().await()
		else -> error("The token refresh has failed: $response")
	}
}
