rootProject.name = "clovis"

include("core")
include("server")
include("client")
include("compose")

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}
}
