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
				api(project(":core.logger"))

				// KotlinX
				api(KotlinX.serialization.core)
				api(KotlinX.serialization.json)
			}
		}
	}
}
