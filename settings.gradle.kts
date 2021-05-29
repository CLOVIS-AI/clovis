rootProject.name = "The CLOVIS Assistant"

include("core")
include("database")
include("server")
include("client")
include("compose")
include("compose:common")
include("compose:android")
include("compose:desktop")

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}
}
