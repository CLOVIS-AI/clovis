group = "braindot"
version = "1.0-SNAPSHOT"

subprojects {
	group = rootProject.group
	version = rootProject.version
}

plugins {
	// These plugins must be declared at the top-level, even if they are not used here.
	kotlin("multiplatform") apply false
	kotlin("jvm") apply false
	kotlin("plugin.serialization") apply false
}

buildscript {
	repositories {
		google()
		mavenCentral()
	}
}

allprojects {
	repositories {
		google()
		mavenCentral()
	}

	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions {
			jvmTarget = "1.8"
		}
	}
}
