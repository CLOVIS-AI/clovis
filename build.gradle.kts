group = "braindot"
version = "1.0-SNAPSHOT"

subprojects {
	group = rootProject.group
	version = rootProject.version
}

plugins {
	// These plugins must be declared at the top-level, even if they are not used here.
	kotlin("multiplatform") version "1.4.30" apply false
	kotlin("jvm") version "1.4.30" apply false
	kotlin("plugin.serialization") version "1.4.30" apply false
}
