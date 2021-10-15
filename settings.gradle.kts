rootProject.name = "CLOVIS"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}
}

plugins {
	id("de.fayard.refreshVersions") version "0.23.0"
}

include(
	"core",
	"core.primitives",
	"core.test",
	"core.database",
	"core.logger",

	"contacts",
	"contacts.client",
	"contacts.server",
	"contacts.database",

	"money",
	"money.database",

	"app",
	"app.components",

	"backend",
	"backend.core",
	"backend.server",
	"backend.client",
)
