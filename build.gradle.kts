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

	// Project-wide plugins
	id("org.jetbrains.dokka")
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
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}

	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions {
			jvmTarget = "1.8"
		}
	}

	plugins.apply("org.jetbrains.dokka")
}
