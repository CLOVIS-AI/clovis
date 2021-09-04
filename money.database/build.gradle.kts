plugins {
	kotlin("jvm")
	kotlin("kapt")
}

dependencies {
	api(project(":core:database"))
	api(project(":money"))

	implementation("com.datastax.oss:java-driver-mapper-runtime:_")
	kapt("com.datastax.oss:java-driver-mapper-processor:_")
}
