@file:Suppress("UNUSED_VARIABLE")

plugins {
	kotlin("multiplatform")
	id("org.jetbrains.compose")
}

kotlin {
	jvm()
	js {
		browser()
		binaries.executable()
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				api(project(":core"))
				api(project(":app.components"))
			}
		}
	}
}

compose.desktop {
	application {
		mainClass = "clovis.app.AppJvmKt"
	}
}
