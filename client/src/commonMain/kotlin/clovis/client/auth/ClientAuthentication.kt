package clovis.client.auth

import io.ktor.client.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*

/**
 * The way to authenticate to the server.
 */
sealed class ClientAuthentication {

	/**
	 * Installs the [Auth] feature.
	 */
	abstract fun HttpClientConfig<*>.install()

}

object AnonymousAccess : ClientAuthentication() {

	// Nothing to do: we're not authenticating to the server at all
	override fun HttpClientConfig<*>.install() {}
}

data class PasswordAccess(val username: String, val password: String) : ClientAuthentication() {
	override fun HttpClientConfig<*>.install() {
		install(Auth) {
			basic {
				this.username = this@PasswordAccess.username
				this.password = this@PasswordAccess.password
			}
		}
	}
}
