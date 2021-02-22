# CLOVIS Server

## Setup

The server requires a MySQL/MariaDB database.

You should create a `gradle-local.properties` file that looks like this:

```properties
# suppress inspection "UnusedProperty" for whole file
# Hostname & port of the server running the DB
DATABASE_HOST=localhost
DATABASE_PORT=12345

# Name of the database that should be used
DATABASE_NAME=clovis

# Credentials of the MySQL user
DATABASE_USER=username
DATABASE_PASSWORD=password
```
