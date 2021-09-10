plugins {
	kotlin("jvm")
}

dependencies {
	implementation(project(":core.logger"))

	// Kotlin Coroutines
	api("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")

	// Apache Cassandra drivers, by DataStax
	api("com.datastax.oss:java-driver-core:_")
}
