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
		val jvmMain by getting {
			dependencies {
				implementation("org.slf4j:slf4j-api:_")
				implementation("org.slf4j:slf4j-simple:_")
			}
		}
	}
}
