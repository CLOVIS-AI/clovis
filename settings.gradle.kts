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

	"users.client",
	"users.server",
	"users.database",

	"money",
	"money.database",

	"app",
	"app.components",

	"backend",
	"backend.server",
	"backend.client",
)
