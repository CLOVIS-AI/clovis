rootProject.name = "CLOVIS"

plugins {
	id("de.fayard.refreshVersions") version "0.11.0"
}

include(
	"api:core",
	"api:lib",
	"money:core",
)
