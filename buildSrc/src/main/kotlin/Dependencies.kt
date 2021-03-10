fun ktor(name: String) =
	"io.ktor:ktor-$name:${Version.ktor}"

fun kotlinxSerialization(name: String) =
	"org.jetbrains.kotlinx:kotlinx-serialization-$name:${Version.serialization}"

fun logback(name: String) =
	"ch.qos.logback:logback-$name:${Version.logback}"

fun exposed(name: String) =
	"org.jetbrains.exposed:exposed-$name:${Version.exposed}"

fun mysqlConnector(name: String) =
	"mysql:mysql-connector-$name:${Version.mysqlConnector}"

fun arrow(name: String) = "io.arrow-kt:arrow-$name:${Version.arrow}"
