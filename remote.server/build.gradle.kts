plugins {
	kotlin("jvm")
}

dependencies {
	api(project(":core.dsl"))

	implementation(project(":utils.logger"))
	api(project(":utils.database"))
	testImplementation(project(":utils.test"))

	api(project(":remote.core"))

	// Ktor
	api(Ktor.server.core)
	api(Ktor.server.netty)
	api(Ktor.features.serialization)

	// Authentication
	implementation("at.favre.lib:bcrypt:_")
	implementation(Ktor.features.auth)
	implementation(Ktor.features.authJwt)
}
