package clovis.backend.core

import clovis.backend.core.Tokens.accessTokenLifetime
import clovis.backend.core.Tokens.refreshTokenLifetime
import kotlin.time.Duration

/**
 * This documentation page explains the Token system used for authentication towards the CLOVIS system.
 *
 * There are two different kinds of tokens: *Access tokens* and *Refresh tokens*.
 *
 * ### Access tokens
 *
 * The possession to an Access Token proves the user's authentication.
 * Access tokens are only valid a short length of time (a dozen minutes, see [accessTokenLifetime]).
 *
 * ### Refresh tokens
 *
 * Refresh tokens do not allow the user to do any operation, but request a new Access Token.
 * These are valid for longer periods of time (see [refreshTokenLifetime]), and their validity can be extended as long as they haven't expired.
 */
@OptIn(kotlin.time.ExperimentalTime::class)
object Tokens {

	val accessTokenLifetime = Duration.minutes(20)

	val refreshTokenLifetime = Duration.days(1)

}
