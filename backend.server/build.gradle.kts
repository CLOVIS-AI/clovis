plugins {
	kotlin("jvm")
}

dependencies {
	api(project(":core"))
	implementation(project(":core.logger"))
	api(project(":backend.core"))

	implementation(project(":core.database"))

	// Ktor
	api(Ktor.server.core)
	api(Ktor.server.netty)
	api(Ktor.features.serialization)

	// Authentication
	implementation("at.favre.lib:bcrypt:_")
	implementation(Ktor.features.auth)
	implementation(Ktor.features.authJwt)

	testImplementation(project(":core.test"))
}
