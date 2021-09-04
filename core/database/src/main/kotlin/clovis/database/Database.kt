package clovis.database

import clovis.database.Database.Companion.connect
import clovis.database.utils.await
import com.datastax.oss.driver.api.core.CqlIdentifier
import com.datastax.oss.driver.api.core.CqlSession

/**
 * Represents a connection to the database.
 *
 * Instantiated either via the constructor, or the [connect] factory.
 * The [disconnect] function should be called, to free resources.
 */
class Database(
	val session: CqlSession
) {

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
		suspend fun connect(): Database {
			val session = CqlSession.builder()
				.withKeyspace(CqlIdentifier.fromCql("clovis"))
				.buildAsync()
				.await()

			return Database(session)
		}
	}
}
