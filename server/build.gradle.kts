plugins {
	kotlin("jvm") version "1.4.21"
	id("application")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib"))

	fun ktor(name: String) = "io.ktor:ktor-$name:1.5.1"
	implementation(ktor("server-core"))
	implementation(ktor("server-netty"))
	implementation(ktor("serialization"))

	fun kotlinx(name: String, version: String) = "org.jetbrains.kotlinx:kotlinx-$name:$version"
	implementation(kotlinx("serialization-core", "1.1.0-RC"))

	implementation("ch.qos.logback:logback-classic:1.2.3")

	implementation(project(":core"))
}

application {
	mainClass.set("clovis.server.ServerKt")
}
