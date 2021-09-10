rootProject.name = "CLOVIS"

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
)
