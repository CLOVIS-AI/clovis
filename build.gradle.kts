group = "braindot"
version = "1.0-SNAPSHOT"

subprojects {
	group = rootProject.group
	version = rootProject.version
}

plugins {
	val kotlin = "1.5.21"

	// These plugins must be declared at the top-level, even if they are not used here.
	kotlin("multiplatform") version kotlin apply false
	kotlin("jvm") version kotlin apply false
	kotlin("plugin.serialization") version kotlin apply false
}

buildscript {
	repositories {
		google()
		mavenCentral()
	}
}

allprojects {
	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions {
			jvmTarget = "1.8"
		}
	}
}
