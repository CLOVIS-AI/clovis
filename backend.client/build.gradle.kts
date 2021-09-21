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
				api(project(":backend.core"))

				// Ktor
				api(Ktor.client.core)
				api(Ktor.client.serialization)
				api(Ktor.client.json)
			}
		}

		val jvmMain by getting {
			dependencies {
				api(Ktor.client.apache)
			}
		}
	}
}
