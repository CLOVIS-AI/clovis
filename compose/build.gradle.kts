import org.jetbrains.compose.compose

plugins {
	kotlin("jvm")
	id("org.jetbrains.compose") version "0.3.2"
}

repositories {
	jcenter()
	maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
	implementation(compose.desktop.currentOs)
}

compose.desktop {
	application {
		mainClass = "clovis.compose.MainKt"
	}
}
