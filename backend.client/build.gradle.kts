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
				implementation(Ktor.client.core)
				implementation(Ktor.client.serialization)
				implementation(Ktor.client.json)
				implementation(Ktor.client.logging)
				implementation(Ktor.client.auth)
			}
		}

		val jvmMain by getting {
			dependencies {
				api(Ktor.client.apache)
			}
		}
	}
}
