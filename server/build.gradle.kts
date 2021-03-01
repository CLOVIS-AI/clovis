plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("application")

	id("jacoco")
	id("de.jansauer.printcoverage") version "2.0.0"
}

repositories {
	mavenCentral()
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

	implementation(project(":core"))
}

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
