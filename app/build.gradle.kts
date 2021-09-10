@file:Suppress("UNUSED_VARIABLE")

plugins {
	kotlin("multiplatform")
	id("org.jetbrains.compose")
}

repositories {
	mavenCentral()
	maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	google()
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
				api(project(":core"))

				api(compose.runtime)
				api(compose.web.widgets)
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

compose.desktop {
	application {
		mainClass = "clovis.app.AppJvmKt"
	}
}
