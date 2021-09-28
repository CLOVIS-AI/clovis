plugins {
	kotlin("jvm")
	application
}

dependencies {
	api(project(":core"))
	implementation(project(":core.logger"))
	implementation(project(":backend.server"))

	implementation(Ktor.features.auth)
	implementation(Ktor.features.authJwt)
}

application {
	mainClass.set("clovis.backend.MainKt")
}
