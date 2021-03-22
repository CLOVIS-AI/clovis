plugins {
	kotlin("multiplatform")
}

repositories {
	mavenCentral()
}

kotlin {

	jvm()

	@Suppress("UNUSED_VARIABLE")
	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation(kotlin("stdlib-common"))
				implementation(kotlinxSerialization("core"))

				api(ktorClient("core"))
				api(ktorClient("serialization"))
				api(ktorClient("json"))
				api(ktorClient("logging"))
				api(ktorClient("auth"))

				api(project(":core"))
			}
		}
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}

		val jvmMain by getting {
			dependencies {
				api(ktorClient("cio"))
				api(ktorClient("json-jvm"))
				api(ktorClient("serialization-jvm"))
				api(ktorClient("logging-jvm"))
			}
		}
		val jvmTest by getting {
			dependencies {
				implementation(kotlin("test"))
				implementation(kotlin("test-junit"))
			}
		}
	}
}
