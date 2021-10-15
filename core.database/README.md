# CLOVIS : Cassandra Database DSL

This module contains a custom DSL for Apache Cassandra and Kotlin. The implementation is based on
the [DataStax](https://www.datastax.com/) drivers, and heavily uses Kotlin Coroutines.

This DSL is able to perform simple migrations:

- Create a table that didn't exist before,
- Edit table options,
- Add a column that didn't exist previously,
- Etc.

It is on purpose that this DSL cannot perform more complicated migrations. For example, it will never remove a column (
but it will ignore any existing column that isn't part of the Kotlin schema). The goal is to ensure that the DSL cannot
lead to data loss. If you want to perform breaking schema changes, you will have to do them manually.

## Usage

Most methods of the DSL are suspending and must be run in an appropriate context. The examples in this document ignore
this, see the [Kotlin documentation](https://kotlinlang.org/docs/coroutines-overview.html).

### Schema

First, you need to acquire a `Database` object. The configuration of the contact points is done via
the `application.conf` file (see
the [DataStax documentation](https://docs.datastax.com/en/developer/java-driver/4.6/manual/core/configuration/)).

```kotlin
import clovis.database.Database

val database = Database.connect()
```

You can then define your columns and tables:

```kotlin
import clovis.database.schema.*

val id = column("id", Type.Binary.UUID)
val email = column("email", Type.Binary.Text)
val name = column("name", Type.Binary.Text)

val peopleById = database.table(
	"some_keyspace", "people_by_id",
	id.partitionKey(),
	email,
	name,
)

// By specifying which column is the partition/clustering key in 
// table declaration (and not in column declaration), you can use the same
// columns in different tables (manual denormalization).
val peopleByEmail = database.table(
	"some_keyspace", "people_by_email",
	email.partitionKey(),
	id,
	name,
)

// To use compounds keys, just use .partitionKey or .clusteringKey multiple times.
val peopleByIdAndEmail = database.table(
	"some_keyspace", "people",
	id.partitionKey(),
	email.partitionKey(),
	name,
)
```

### Requests

Examples of the `INSERT` syntax:

```kotlin
val newId = UUID.randomUUID()
peopleById.insert(
	id set newId,
	email set "my_user_email@domain.com",
	name set "My User",
)
```

Examples of the `SELECT` syntax:

```kotlin
val results: Flow<Row> = peopleById.select(
	id eq newId, // search filters ('WHERE …')
	id, name,    // selected columns ('SELECT …')
)

// This is a SELECT by id, there should never be multiple results
val result: Row? = results.firstOrNull()

if (result != null) {
	// Get the values of each column
	// Note that the variables are automatically typed based 
	// on the column's type
	val selectedId = result[id]     // automatically java.util.UUID
	val selectedName = result[name] // automatically kotlin.String
}
```

To use multiple search filters, you can use the `and` filter:

```kotlin
peopleByIdAndEmail.select(
	and(
		id eq newId,
		email isOneOf listOf("my_email@gmail.com", "my_email@microsoft.com"),
	),
	id, name,
)
```

To select all rows from a table, you must explicitly provide no filters (because this should not be used often, for
performance reasons):

```kotlin
peopleById.select(
	where = null, // no filters
	id, name,     // selected columns
).collect { row ->
	println("User ${row[id]}: ${row[name]}")
}
```

The `UPDATE` syntax uses the filters from `SELECT` and the assignments from `INSERT`:

```kotlin
peopleByIdAndEmail.update(
	id eq newId,            // filter   (same syntax as SELECT)
	name set "My New Name", // aignment (same syntax as INSERT)
)
```

The `DELETE` syntax is very similar to the `UPDATE` syntax:

```kotlin
// Delete all columns
peopleById.delete(
	id eq newId,
)

// Only delete the email and the name
peopleById.delete(
	id eq newId,
	email, name,
)
```

All requests accept additional options as an optional lambda, for example with `INSERT`:

```kotlin
peopleById.insert(
	id set newId,
	name set "My User",
) {
	secondsToLive = 60
	ignoreIfAlreadyExists = true
}
```

### Architecture example

Here is a full example that shows how we recommend you architecture your software:

```kotlin
private object Columns {
	val id = column("id", Type.Binary.UUID)
	val username = column("user", Type.Binary.Text)
	val fullName = column("display", Type.Binary.Text)
}

class Users(private val database: Database, scope: CoroutineScope) {

	// Ensure the migration only happens once
	private val byId = scope.async {
		database.table(
			"your_keyspace", "users_by_id",
			id.partitionKey(),
			username,
			fullName,
		)
	}

	suspend fun Database.allUsers(): Flow<UUID> = byId.await()
		.select(
			where = null,
			Columns.id,
		)

	suspend fun Database.userById(id: UUID): Flow<Pair<String, String>> = byId.await()
		.select(
			Columns.id eq id,
			Columns.username, Columns.fullName,
		).map { it[Columns.username] to it[Columns.fullName] }

	suspend fun Database.createUser(username: String, fullName: String): UUID {
		val id = UUID.randomUUID()
		byId.await().insert(
			Columns.id set id,
			Columns.username set username,
			Columns.fullName set fullName,
		)
		return id
	}
}
```
