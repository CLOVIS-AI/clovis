package clovis.server

import io.ktor.http.*
import io.ktor.util.*
import kotlin.reflect.KProperty

/**
 * Delegate operator for Ktor request parameters.
 *
 * This extension enables the delegation syntax for request parameters:
 * ```kotlin
 * val value by call.parameters                    // New syntax
 * val value = call.parameters.getOrFail("value")  // Syntax without this extension
 * ```
 */
operator fun Parameters.getValue(thisRef: Any?, property: KProperty<*>): String = getOrFail(property.name)
