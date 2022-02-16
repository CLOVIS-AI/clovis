package clovis.database

import clovis.database.Database.Companion.connect
import clovis.database.utils.await
import clovis.logger.WithLogger
import clovis.logger.info
import clovis.logger.trace
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.config.DriverConfigLoader
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.core.cql.Statement
import com.datastax.oss.driver.internal.core.config.typesafe.DefaultDriverConfigLoader
import java.io.File

/**
 * Represents a connection to the database.
 *
 * Instantiated either via the constructor, or the [connect] factory.
 * The [disconnect] function should be called, to free resources.
 */
class Database(
	private val session: CqlSession,
) {
	init {
		log.info { "Connected." }
	}

	/**
	 * Closes the connection to the database, and frees resources used by the driver.
	 *
	 * @see CqlSession.closeAsync
	 */
	@Suppress("MemberVisibilityCanBePrivate")
	suspend fun disconnect() {
		session.closeAsync().await()
	}

	suspend fun execute(query: String): AsyncResultSet = session
		.executeAsync(query.also { log.trace { "executing: \n\t$it" } })
		.await()

	suspend fun execute(statement: Statement<*>): AsyncResultSet = session
		.executeAsync(statement.also { log.trace { "executing: \n\t$it" } })
		.await()

	companion object : WithLogger() {

		/**
		 * Connects to the database.
		 *
		 * To override the default connection values, create a `application.conf` file in the project's resources;
		 * see [the driver documentation](https://docs.datastax.com/en/developer/java-driver/4.13/manual/core/configuration/reference/).
		 */
		suspend fun connect(): Database {
			val bonusConfiguration = File("/etc/application.conf")
			val defaultConfiguration = DefaultDriverConfigLoader()

			val config = when {
				bonusConfiguration.isFile && bonusConfiguration.canRead() -> {
					log.trace { "The file ${bonusConfiguration.absolutePath} overrides the default configuration." }
					DriverConfigLoader.fromFile(bonusConfiguration) + defaultConfiguration
				}
				else -> {
					log.info { "To override the default configuration, create the file ${bonusConfiguration.absolutePath}." }
					defaultConfiguration
				}
			}

			val session = CqlSession.builder()
				.withConfigLoader(config)
				.buildAsync()
				.await()

			return Database(session)
		}

	}
}

/**
 * Combines the [DriverConfigLoader] configuration.
 *
 * The receiver (`this`) has priority over the [other] configuration.
 */
private operator fun DriverConfigLoader.plus(other: DriverConfigLoader) = DriverConfigLoader.compose(this, other)
