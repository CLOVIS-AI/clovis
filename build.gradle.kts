group = "braindot"
version = "1.0-SNAPSHOT"

subprojects {
	group = rootProject.group
	version = rootProject.version
}

plugins {
	val kotlinVersion = "1.4.31"

	// These plugins must be declared at the top-level, even if they are not used here.
	kotlin("multiplatform") version kotlinVersion apply false
	kotlin("jvm") version kotlinVersion apply false
	kotlin("plugin.serialization") version kotlinVersion apply false
}
