plugins {
	kotlin("jvm")
}

dependencies {
	implementation(project(":core.logger"))

	// Apache Cassandra drivers, by DataStax
	api("com.datastax.oss:java-driver-core:_")
}
