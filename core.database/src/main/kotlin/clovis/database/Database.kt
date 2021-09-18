package clovis.database

import clovis.database.Database.Companion.connect
import clovis.database.utils.await
import clovis.logger.info
import clovis.logger.logger
import clovis.logger.trace
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.core.cql.Statement
import org.intellij.lang.annotations.Language

@Language("CassandraQL")
private const val CREATE_KEYSPACE = """
	CREATE KEYSPACE IF NOT EXISTS clovis
	WITH REPLICATION = { 'class':'SimpleStrategy', 'replication_factor':'1' }
"""

@Language("CassandraQL")
private const val CONNECT_TO_KEYSPACE = """
	USE clovis;
"""

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
		.executeAsync(query.also { log.trace { "executing: $it" } })
		.await()

	suspend fun execute(statement: Statement<*>): AsyncResultSet = session
		.executeAsync(statement.also { log.trace { "executing: $it" } })
		.await()

	companion object {
		private val log = logger(this)

		/**
		 * Connects to the database.
		 *
		 * To override the default connection values, create a `application.conf` file in the project's resources;
		 * see [the driver documentation](https://docs.datastax.com/en/developer/java-driver/4.13/manual/core/configuration/reference/).
		 */
		suspend fun connect(): Database {
			val session = CqlSession.builder()
				.buildAsync()
				.await()

			//region Keyspace
			// "Detected a keyspace change at runtime": this is safe, because no other threads are using
			// the keyspace before the end of this function.
			session.executeAsync(CREATE_KEYSPACE).await()
			session.executeAsync(CONNECT_TO_KEYSPACE).await()
			//endregion

			return Database(session)
		}

	}
}
