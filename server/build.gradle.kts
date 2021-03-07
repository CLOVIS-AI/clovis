plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("application")
	id("net.saliman.properties") version "1.5.1"

	id("jacoco")
	id("de.jansauer.printcoverage") version "2.0.0"
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

	fun ktor(name: String) = "io.ktor:ktor-$name:1.5.1"
	implementation(ktor("server-core"))
	implementation(ktor("server-netty"))
	implementation(ktor("serialization"))
	testImplementation(ktor("server-tests"))

	fun kotlinx(name: String, version: String) = "org.jetbrains.kotlinx:kotlinx-$name:$version"
	implementation(kotlinx("serialization-core", "1.1.0-RC"))

	implementation("ch.qos.logback:logback-classic:1.2.3")

	fun exposed(name: String) = "org.jetbrains.exposed:exposed-$name:0.29.1"
	implementation(exposed("core"))
	implementation(exposed("dao"))
	implementation(exposed("jdbc"))

	implementation("mysql:mysql-connector-java:5.1.48")

	fun arrow(name: String) = "io.arrow-kt:arrow-$name:0.11.0"
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
