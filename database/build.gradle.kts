plugins {
	kotlin("jvm")
	id("net.saliman.properties") version Version.gradleProperties

	id("jacoco")
	id("de.jansauer.printcoverage") version Version.printCoverage
}

dependencies {
	implementation(kotlin("stdlib"))
	testImplementation(kotlin("test"))
	testImplementation(kotlin("test-junit"))

	implementation(exposed("core"))
	implementation(exposed("dao"))
	implementation(exposed("jdbc"))

	implementation(mysqlConnector("java"))

	implementation(arrow("core"))
	implementation(arrow("syntax"))

	implementation(project(":core"))
}

//region Test coverage

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		xml.isEnabled = true
		csv.isEnabled = false
		html.isEnabled = true
	}
}

//endregion
//region Database settings

fun JavaForkOptions.requireDatabaseSettings() {
	fun property(name: String) = name to project.ext.get(name).toString()

	environment(
		property("DATABASE_HOST"),
		property("DATABASE_PORT"),
		property("DATABASE_NAME"),
		property("DATABASE_USER"),
		property("DATABASE_PASSWORD")
	)
}

tasks.named<Test>("test") {
	requireDatabaseSettings()
}

//endregion
