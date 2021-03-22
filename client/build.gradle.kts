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

				api(ktor("client-core"))
				api(ktor("client-serialization"))
				api(ktor("client-json"))
				api(ktor("client-logging"))
				api(ktor("client-auth"))

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
				api(ktor("client-cio"))
				api(ktor("client-json-jvm"))
				api(ktor("client-serialization-jvm"))
				api(ktor("client-logging-jvm"))
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
