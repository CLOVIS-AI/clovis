plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("application")
	id("net.saliman.properties") version Version.gradleProperties

	id("jacoco")
	id("de.jansauer.printcoverage") version Version.printCoverage
}

//region Dependencies

repositories {
	mavenCentral()
	jcenter()
}

dependencies {
	implementation(kotlin("stdlib"))
	testImplementation(kotlin("test"))
	testImplementation(kotlin("test-junit"))

	implementation(ktor("server-core"))
	implementation(ktor("server-netty"))
	implementation(ktor("serialization"))
	testImplementation(ktor("server-tests"))

	implementation(kotlinxSerialization("core"))

	implementation(logback("classic"))

	implementation(exposed("core"))
	implementation(exposed("dao"))
	implementation(exposed("jdbc"))

	implementation(mysqlConnector("java"))

	implementation(arrow("core"))
	implementation(arrow("syntax"))

	implementation(project(":core"))
}

//endregion
//region Server execution, tests

application {
	mainClass.set("clovis.server.ServerKt")
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		xml.isEnabled = true
		csv.isEnabled = false
		html.isEnabled = true
	}
}

//endregion
//region Environment variables

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

tasks.named<JavaExec>("run") {
	requireDatabaseSettings()
}

tasks.named<Test>("test") {
	requireDatabaseSettings()
}

//endregion
//region Kotlin configuration

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

//endregion
