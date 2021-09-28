plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
}

dependencies {
	api(project(":core"))
	api(project(":backend.server"))
	implementation(project(":core.logger"))

	// Kotlin Coroutines
	api(KotlinX.coroutines.core)

	// Serialization
	implementation(KotlinX.serialization.json)
}
