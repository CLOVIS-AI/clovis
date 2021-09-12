plugins {
	kotlin("jvm")
}

dependencies {
	api(project(":core"))
	implementation(project(":core.logger"))
	implementation(project(":backend.core"))
}
