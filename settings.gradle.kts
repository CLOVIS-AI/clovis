rootProject.name = "CLOVIS"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}
}

plugins {
	id("de.fayard.refreshVersions") version "0.21.0"
}

include(
	"core",
	"core.primitives",
	"core.test",
	"core.database",
	"core.logger",

	"money",
	"money.database",

	"app",
	"app.components",

	"backend",
	"backend.core",
)
