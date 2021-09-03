plugins {
	kotlin("jvm")
}

dependencies {
	api(project(":core"))

	// Apache Cassandra drivers, by DataStax
	api("com.datastax.oss:java-driver-core:_")
	api("com.datastax.oss:java-driver-query-builder:_")
	api("com.datastax.oss:java-driver-mapper-runtime:_")
}
