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

				// Ktor
				api(Ktor.client.core)
				api(Ktor.client.serialization)
				api(Ktor.client.json)

				// KotlinX
				api(KotlinX.serialization.core)
				api(KotlinX.serialization.json)
			}
		}

		val jvmMain by getting {
			dependencies {
				api(Ktor.client.apache)
			}
		}
	}
}
