group = "braindot"
version = "1.0-SNAPSHOT"

subprojects {
	group = rootProject.group
	version = rootProject.version
}

plugins {
	// These plugins must be declared at the top-level, even if they are not used here.
	kotlin("multiplatform") version Version.kotlin apply false
	kotlin("jvm") version Version.kotlin apply false
	kotlin("plugin.serialization") version Version.kotlin apply false
}
