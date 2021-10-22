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
	/*
	 * Core APIs, used by most modules
	 */
	"core.dsl",
	"core.primitives",

	/*
	 * Utilitarian libraries internally used to abstract over often used features
	 */
	"utils.test",
	"utils.database",
	"utils.logger",

	/*
	 * Implementations of remote providers to communicate with CLOVIS servers
	 */
	"remote.core",
	"remote.server",
	"remote.client",

	"contacts",
	"contacts.client",
	"contacts.server",
	"contacts.database",

	"money",
	"money.database",

	/*
	 * The CLOVIS app
	 */
	"app",
	"app.components",

	/*
	 * The CLOVIS server
	 */
	"backend",
)
