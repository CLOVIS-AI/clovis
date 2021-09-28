package clovis.database.schema

import clovis.database.Database
import clovis.database.queries.UpdateExpression.Companion.set
import clovis.database.queries.insert
import clovis.database.queries.select
import clovis.database.utils.get
import clovis.database.utils.toFlow
import clovis.logger.info
import clovis.logger.logger
import clovis.logger.trace
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.intellij.lang.annotations.Language
import java.util.*

private object Migration

private val log = logger(Migration)

private val createdKeyspaces = HashSet<String>()
private val keyspaceLock = Semaphore(1)

private suspend fun Database.createKeyspaceIfNecessary(keyspace: String) {
	keyspaceLock.withPermit {
		if (keyspace in createdKeyspaces)
			return
	}

	@Language("CassandraQL")
	val createKeyspace = """
		create keyspace if not exists $keyspace
		with replication = { 'class':'SimpleStrategy', 'replication_factor':'1' }
	""".trimIndent()

	log.info { "Creating keyspace $keyspace…" }
	execute(createKeyspace)

	keyspaceLock.withPermit {
		createdKeyspaces += keyspace
	}
}

internal suspend fun Database.migrateTable(table: Table) {
	createKeyspaceIfNecessary(table.keyspace)

	@Language("CassandraQL")
	val describeTable = """
		describe table ${table.keyspace}.${table.name}
	""".trimIndent()

	log.trace { "Analyzing the current state of the table '${table.qualifiedName}'" }
	val tableDescription = try {
		execute(describeTable)
			.toFlow()
			.first()
	} catch (e: Throwable) {
		if (e.message?.contains("not found") == true) {
			log.trace { "The table doesn't exist yet." }
			createTable(table)
			return // If the table was created, there is nothing new to check
		} else throw RuntimeException(e)
	}
	// If we're here, the table exists but in incorrect state

	log.trace { "Checking keyspace and table name…" }
	val actualKeyspace = tableDescription.get("keyspace_name", Type.Binary.Text)
	val actualType = tableDescription.get("type", Type.Binary.Text)
	val actualName = tableDescription.get("name", Type.Binary.Text)
	check(actualKeyspace == table.keyspace) { "Expected keyspace ${table.keyspace}, but found $actualKeyspace" }
	check(actualType == "table") { "Expected type 'table', but found '$actualType'" }
	check(actualName == table.name) { "Expected name ${table.name}, but found $actualName" }

	log.trace { "Parsing table declaration…" }
	val current = parseTableDeclaration(tableDescription.get("create_statement", Type.Binary.Text), this)

	applyDiff(current, table)
}

private suspend fun Database.applyDiff(current: Table, target: Table) {
	log.info { "Updating table ${target.qualifiedName}…" }

	var query = ""

	for (column in target.columns) {
		val currentColumn = current.columns.find { it.name == column.name }

		if (currentColumn == null) {
			log.trace { "The column $column is new." }
			check(column is RegularColumn) { "The system is currently not able to create new columns of the type ${column::class}" }
			query += "\n\tADD ${column.name} ${column.type.type}"
		} else if (currentColumn != column) {
			log.trace { "The column $column has been modified (previous version: $currentColumn)." }
			query += "\n\tALTER ${column.name} TYPE ${column.type.type}"
		} else {
			log.trace { "The column $column is identical to the current version." }
		}
	}

	for ((optionName, newValue) in target.options) {
		val currentValue = current.options[optionName]

		if (currentValue != newValue) {
			log.trace { "The option $optionName has been modified ($currentValue -> $newValue)" }
			query += "\n\tWITH $optionName = $newValue"
		} else {
			log.trace { "The option $optionName is identical to the current value." }
		}
	}

	if (query.isNotBlank()) {
		execute("ALTER TABLE ${target.qualifiedName} $query")
	} else {
		log.trace { "No column or option modifications were detected." }
	}
}

private fun parseTableDeclaration(declaration: String, database: Database): Table {
	log.trace { "Declaration to parse: $declaration" }

	val scanner = Scanner(declaration)
	scanner.skip("CREATE TABLE")
	val (keyspace, table) = scanner.next().split(".")
	log.trace { "The table is $table, in keyspace $keyspace" }

	scanner.next("\\(")
	val columns = ArrayList<Column<*>>()
	while (true) {
		val columnName = scanner.next()
		if (columnName == ")") break
		val type = scanner.next().removeSuffix(",")

		val column = RegularColumn(columnName, Type.fromCqlName(type))

		if (scanner.hasNext("PRIMARY")) {
			scanner.next().also { check(it == "PRIMARY") { "Expected 'PRIMARY', found '$it'" } }
			scanner.next().also { check(it.startsWith("KEY")) { "Expected 'KEY', found '$it'" } }
			columns += PartitionKey(column)
		} else {
			columns += column
		}
	}
	log.trace { "Parsed columns: $columns" }

	val options = HashMap<String, String>()
	while (scanner.hasNext()) {
		val startLine = scanner.next()
		check(startLine == "WITH" || startLine == "AND") { "Expected 'WITH' or 'AND', found '$startLine'" }

		val optionName = scanner.next()

		val equal = scanner.next()
		check(equal == "=") { "Expected '=', found '$equal'" }

		var value = scanner.next().removeSuffix(";")
		if (value.startsWith('{'))
			while (!value.endsWith('}'))
				value += " " + scanner.next().removeSuffix(";")
		if (value.startsWith("'"))
			while (!value.endsWith("'"))
				value += " " + scanner.next().removeSuffix(";")

		log.trace { "Parsed option '$optionName': $value" }
		options[optionName] = value
	}

	scanner.close()

	return Table(
		name = table,
		keyspace = keyspace,
		columns = columns,
		options = options,
		database = database,
	)
}

private suspend fun Database.createTable(table: Table) {
	createKeyspaceIfNecessary(table.keyspace)

	log.info { "Creating table ${table.qualifiedName}…" }

	@Language("CassandraQL")
	val createTableTemplate = """
		create table ${table.qualifiedName} (
			<cols>,
			PRIMARY KEY ( <ids> )
		)
	""".trimIndent()

	val columns = table.columns
		.joinToString(separator = ",\n    ") { "${it.name} ${it.type.type}" }

	val partitionKeys = table.columns
		.filterIsInstance<PartitionKey<*>>()
		.joinToString(separator = ", ") { it.name }
	val clusteringKeys = table.columns
		.filterIsInstance<ClusteringKey<*>>()
		.joinToString(separator = ", ") { it.name }
	val primaryKeys = listOf(partitionKeys, clusteringKeys)
		.filter { it.isNotBlank() }
		.joinToString(separator = ", ") { "($it)" }

	val createTable = createTableTemplate
		.replace("<cols>", columns)
		.replace("<ids>", primaryKeys)

	execute(createTable)
}

suspend fun Database.drop(table: Table) {
	createKeyspaceIfNecessary(table.keyspace)

	@Language("CassandraQL")
	val dropTable = """
		drop table ${table.keyspace}.${table.name}
	""".trimIndent()

	execute(dropTable)
}

internal suspend fun Database.migrateManualView(view: Table, source: Table) {
	log.trace { "Checking columns of manual view ${view.qualifiedName} (taking its data from ${source.qualifiedName})…" }
	for (column in view.columns) {
		val sourceColumn =
			source.columns.find { it.name == column.name } // check with the name, because the type (therefore ==) may be different
		requireNotNull(sourceColumn) { "The column '${column.name}' from the view '${view.qualifiedName}' doesn't exist in the source table '${source.qualifiedName}'" }
		require(column.type == sourceColumn.type) { "The column '${column.name}' from the view '${view.qualifiedName}' has type ${column.type}, whereas the source table column '${source.qualifiedName}' has type ${sourceColumn.type}" }
	}

	migrateTable(view)

	log.trace { "Migrating all data from the source table ${source.qualifiedName} to the manual view ${view.qualifiedName}…" }
	coroutineScope {
		source.select(where = null, *view.columns.toTypedArray())
			.collect { row ->
				launch { // run all INSERTs in parallel
					val values = Array(view.columns.size) {
						@Suppress("UNCHECKED_CAST") // Column<*> should always be a valid Column<Any?>
						val column = view.columns[it] as Column<Any?>
						column set row[column]
					}

					view.insert(*values)
				}
			}
	}
}
