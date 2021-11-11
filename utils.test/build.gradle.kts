@file:Suppress("UNUSED_VARIABLE")

plugins {
	kotlin("multiplatform")
}

kotlin {
	jvm()
	js {
		browser()
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				api(project(":core.primitives"))

				api(kotlin("test-common"))
				api(kotlin("test-annotations-common"))
			}
		}

		val jvmMain by getting {
			dependencies {
				api(kotlin("test-junit"))
			}
		}

		val jsMain by getting {
			dependencies {
				api(kotlin("test-js"))
			}
		}
	}
}
