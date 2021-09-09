plugins {
	kotlin("jvm")
	kotlin("kapt")
}

dependencies {
	api(project(":core:database"))
	api(project(":core.logger"))
	api(project(":money"))

	implementation("com.datastax.oss:java-driver-mapper-runtime:_")
	kapt("com.datastax.oss:java-driver-mapper-processor:_")
}
