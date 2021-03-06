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
				api(project(":core.dsl"))

				api(compose.runtime)
			}
		}

		val jvmMain by getting {
			dependencies {
				api(compose.desktop.currentOs)

				api(compose.foundation)
				api(compose.material)

				implementation(compose.preview)
			}
		}

		val jsMain by getting {
			dependencies {
				api(compose.web.core)
			}
		}
	}
}
