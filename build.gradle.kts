group = "braindot"
version = "1.0-SNAPSHOT"

subprojects {
	group = rootProject.group
	version = rootProject.version
}

plugins {
	// These plugins must be declared at the top-level, even if they are not used here.
	kotlin("multiplatform") version Version.kotlin apply false
	kotlin("jvm") version Version.kotlin apply false
	kotlin("plugin.serialization") version Version.kotlin apply false
}

buildscript {
	repositories {
		google()
		mavenCentral()
		jcenter()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}

	dependencies {
		classpath("org.jetbrains.compose:compose-gradle-plugin:0.3.2")
		classpath("com.android.tools.build:gradle:4.1.2")
		classpath(kotlin("gradle-plugin", version = Version.kotlin))
	}
}

allprojects {
	repositories {
		google()
		mavenCentral()
		jcenter()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}
}
