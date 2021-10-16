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
	"core.dsl",
	"core.primitives",

	"utils.test",
	"utils.database",
	"utils.logger",

	"contacts",
	"contacts.client",
	"contacts.server",
	"contacts.database",

	"money",
	"money.database",

	"app",
	"app.components",

	"remote",
	"remote.core",
	"remote.server",
	"remote.client",
)
