plugins {
	kotlin("jvm")
}

dependencies {
	implementation(project(":utils.logger"))
	testImplementation(project(":utils.test"))

	// Kotlin Coroutines
	api(KotlinX.coroutines.core)

	// Apache Cassandra drivers, by DataStax
	api("com.datastax.oss:java-driver-core:_")
}
