plugins {
	kotlin("jvm")
	application
}

dependencies {
	api(project(":core.dsl"))

	implementation(project(":utils.logger"))

	implementation(project(":remote.server"))
	implementation(project(":money.server"))

	implementation(Ktor.features.auth)
	implementation(Ktor.features.authJwt)
}

application {
	mainClass.set("clovis.backend.MainKt")
}
