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
				api(project(":remote.client"))

				api(project(":money.core"))
			}
		}

		val commonTest by getting {
			dependencies {
				implementation(project(":utils.test"))
			}
		}
	}
}
