plugins {
	kotlin("jvm")
}

dependencies {
	api(project(":core"))
	implementation(project(":core.logger"))

	// Kotlin Coroutines
	api(KotlinX.coroutines.core)

	// Ktor
	api(Ktor.server.core)
	api(Ktor.server.netty)
}
