plugins {
	kotlin("jvm") version "1.4.21"
	id("application")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib"))

	implementation(project(":core"))
}

application {
	mainClass.set("clovis.server.ServerKt")
}
