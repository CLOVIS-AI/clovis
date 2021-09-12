plugins {
	kotlin("jvm")
}

dependencies {
	api(project(":core"))
	implementation(project(":core.logger"))

	// Kotlin Coroutines
	api(KotlinX.coroutines.core)

	// Serialization
	implementation(KotlinX.serialization.json)

	// Ktor
	api(Ktor.server.core)
	api(Ktor.server.netty)
	api(Ktor.features.serialization)
}
