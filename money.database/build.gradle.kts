plugins {
	kotlin("jvm")
}

dependencies {
	api(project(":utils.database"))
	api(project(":utils.logger"))
	testImplementation(project(":utils.test"))

	api(project(":money"))
}
