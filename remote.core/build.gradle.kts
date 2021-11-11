@file:Suppress("UNUSED_VARIABLE")

plugins {
	kotlin("multiplatform")
	kotlin("plugin.serialization")
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

				api(project(":utils.logger"))

				// KotlinX
				api(KotlinX.serialization.core)
				api(KotlinX.serialization.json)
			}
		}
	}
}
