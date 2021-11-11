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
				implementation(project(":utils.logger"))

				api(project(":money.core"))
			}
		}
	}
}
