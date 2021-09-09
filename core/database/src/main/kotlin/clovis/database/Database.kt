package clovis.database

import clovis.database.Database.Companion.connect
import clovis.database.utils.await
import clovis.logger.info
import clovis.logger.logger
import com.datastax.oss.driver.api.core.CqlSession
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
	val session: CqlSession,
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

	companion object {
		private val log = logger(this)

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

		// In the future, when 'connect' gets fancy arguments, connectTest will keep the defaults used by tests
		suspend fun connectLocal() = connect()
	}
}
