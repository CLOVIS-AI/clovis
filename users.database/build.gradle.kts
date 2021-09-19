plugins {
	kotlin("jvm")
}

dependencies {
	api(project(":core.database"))
	api(project(":core.logger"))
	api(project(":money"))

	testImplementation(project(":core.test"))
}
