package clovis.backend.core

import kotlinx.serialization.json.Json

/**
 * JSON serializer used by client-server communication.
 */
val JsonSerializer = Json {
	useArrayPolymorphism = false
	encodeDefaults = false
}
