plugins {
	kotlin("jvm")
}

dependencies {
	api(project(":core.dsl"))

	implementation(project(":utils.logger"))
	api(project(":utils.database"))
	testImplementation(project(":utils.test"))

	api(project(":remote.core"))
	api(project(":money.database"))

	// Ktor
	api(Ktor.server.core)
	api(Ktor.server.netty)
	api(Ktor.features.serialization)

	// Authentication
	implementation("at.favre.lib:bcrypt:_")
	api(Ktor.features.auth)
	implementation(Ktor.features.authJwt)
}
