plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
}

dependencies {
	api(project(":core.dsl"))
	implementation(project(":utils.logger"))

	api(project(":remote.server"))

	// Kotlin Coroutines
	api(KotlinX.coroutines.core)

	// Serialization
	implementation(KotlinX.serialization.json)
}
