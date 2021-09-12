plugins {
	kotlin("jvm")
	application
}

dependencies {
	api(project(":core"))
	implementation(project(":core.logger"))
	implementation(project(":backend.server"))
}

application {
	mainClass.set("clovis.backend.MainKt")
}
