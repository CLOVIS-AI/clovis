package clovis.database.utils

import clovis.database.Database
import clovis.logger.info
import clovis.logger.logger
import clovis.logger.trace
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.intellij.lang.annotations.Language

@Language("CassandraQL")
private const val CREATE_TABLE = """
	create table if not exists clovis.tables (
		name	TEXT,
		version	INT,
		PRIMARY KEY ( name ),
	)
"""

@Language("CassandraQL")
private const val select = """
	select name, version
	from clovis.tables
	where name = '?'
"""

@Language("CassandraQL")
private const val updateVersion = """
	insert into clovis.tables(name, version) 
	values ('?', ?)
"""

private object TableUpdater

private val log = logger(TableUpdater)

private var createdTable = false
private val alreadyUpdated = ArrayList<String>()
private val lock = Semaphore(1)

suspend fun Database.updateTable(name: String, migrations: Map<Int, String>): Unit = lock.withPermit {
	require(migrations.isNotEmpty()) { "The migrations cannot be empty. At the very least, the first version should correspond to the table creation." }
	require(name.isNotBlank()) { "The name of the table cannot be blank: '$name'" }

	if (!createdTable) {
		log.info { "Creating the 'tables' table, to store the current versions of tables" }
		session.executeAsync(SimpleStatement.newInstance(CREATE_TABLE)).await()
		createdTable = true
	}

	if (name in alreadyUpdated) {
		log.trace { "Table '$name' has already been updated since the start of this process." }
		return@withPermit
	}

	log.trace { "Getting the current version of the '$name' table" }
	val currentVersion = session.executeAsync(select.replace("?", name))
		.await()
		.toFlow()
		.map { it.getInt("version") }
		.firstOrNull()
		?: -1
	log.info { "The current version of the table '$name' is $currentVersion" }

	val sortedMigrations = migrations.toSortedMap()
	sortedMigrations
		.filter { (version, _) -> version > currentVersion }
		.forEach { (version, query) ->
			log.trace { "Migrating table '$name' to version $version" }
			session.executeAsync(query).await()
		}

	val maxVersion = sortedMigrations.lastKey()
	if (maxVersion != currentVersion) {
		log.trace { "Recording that table '$name' has been updated to version $maxVersion" }
		session.executeAsync(updateVersion.replaceFirst("?", name).replaceFirst("?", maxVersion.toString())).await()
		alreadyUpdated += name

		log.info { "The table '$name' has successfully been updated to version $maxVersion" }
	} else {
		log.info { "The table '$name' is already up-to-date" }
	}
}
