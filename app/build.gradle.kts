@file:Suppress("UNUSED_VARIABLE")

plugins {
	kotlin("multiplatform")
	id("org.jetbrains.compose")
}

kotlin {
	jvm()
	js {
		browser()
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				api(project(":core"))
			}
		}
	}
}
