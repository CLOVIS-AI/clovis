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
				api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")

				implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:_")

				implementation("org.jetbrains.kotlinx:kotlinx-datetime:_")
			}
		}
	}
}
