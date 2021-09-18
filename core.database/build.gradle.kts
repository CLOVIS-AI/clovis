plugins {
	kotlin("jvm")
}

dependencies {
	implementation(project(":core.logger"))
	testImplementation(project(":core.test"))

	// Kotlin Coroutines
	api(KotlinX.coroutines.core)

	// Apache Cassandra drivers, by DataStax
	api("com.datastax.oss:java-driver-core:_")
}
