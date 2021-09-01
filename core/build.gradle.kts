@file:Suppress("UNUSED_VARIABLE")

plugins {
	kotlin("multiplatform")
	kotlin("plugin.serialization")
}

kotlin {
	jvm()
	js {
		browser {
			testTask {
				useKarma {
					useChromeHeadless()
				}
			}
		}
		binaries.executable()
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
				api("org.jetbrains.kotlinx:kotlinx-serialization-core:_")
				implementation("org.jetbrains.kotlinx:kotlinx-datetime:_")
			}
		}

		val commonTest by getting {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))

				implementation(project(":core-test"))
			}
		}

		val jvmTest by getting {
			dependencies {
				implementation(kotlin("test-junit"))
			}
		}

		val jsTest by getting {
			dependencies {
				implementation(kotlin("test-js"))
			}
		}
	}
}
