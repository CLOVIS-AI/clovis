package clovis.server.db

import org.jetbrains.exposed.sql.Database

private fun env(name: String) =
	System.getenv(name) ?: error("Missing environment variable $name, see README.md")

private val databaseHost = env("DATABASE_HOST")
private val databasePort = env("DATABASE_PORT")
private val databaseName = env("DATABASE_NAME")
private val databaseUser = env("DATABASE_USER")
private val databasePassword = env("DATABASE_PASSWORD")

val dbConnection = Database.connect(
	url = "jdbc:mysql://$databaseHost:$databasePort/$databaseName",
	driver = "com.mysql.jdbc.Driver",
	user = databaseUser,
	password = databasePassword
)
