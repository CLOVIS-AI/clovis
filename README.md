# CLOVIS: A Personal Organizer

> **Note.**
> The development is currently in its very early stages.
> This README represents the goals of the project, not the current functionalities.

CLOVIS is an open source multiplatform software suite that combines monetary information, task management, etc. CLOVIS
is built so each module is as independent as possible from the others, so you can build your own tools with just the
parts you want.

## Introduction

CLOVIS is a collection of libraries that deal with multiple aspects of day-to-day life.

- Libraries to interface with popular implementations,
- Libraries to interface with the CLOVIS implementation,
- A canonical DSL to use any implementation.
- A canonical UI to use any implementation.

CLOVIS is built around the concept of `Provider`: an object responsible for querying data from some other API. The
project is organized to allow easy implementation of your own `Provider`s.

<!-- TODO: Example of the DSL -->

## Project structure

### Core modules

For most cases, you will want to depend on the `core.dsl` module. The `core.dsl` module contains all important API
interfaces, with no implementations. This lets you use the full DSL by simply adding a dependency on the implementations
you want to use.

The core modules are:

- `core.primitives`: The basic objects used by all CLOVIS implementations (`Provider`, `Cache`…).
- `core.dsl`: The CLOVIS DSL itself (all API interfaces, but no implementations).

Utils modules are provided to encapsulate common features that must be used in multiple parts of the project:

- `utils.test`: A small module for multiplatform tests, compatible with coroutines.
- `utils.logger`: A light wrapper around platform logging utilities (`slf4j` on the JVM, `console` in JS).
- `utils.database`: A custom Apache Cassandra DSL, based on the DataStax driver.
  See [the README](utils.database/README.md).

The `remote` modules allow to the use `Provider`s from another computer. This allows, for example, to implement
all `Provider`s on the backend, but use them transparently on the client, as if they were local.

- `remote.core`: Common utilities for the remote provider protocol.
- `remote.server`: Server-side implementation of the remote provider protocol.
- `remote.client`: Client-side implementation of the remote provider protocol.

### Implementations

List of contacts:

- `contacts.core`: Declaration of the Contacts API
- `contacts.database`: CLOVIS implementation of the Contacts API
- `contacts.server`: Server-side implementation of the remote provider protocol, specialized for the Contacts API
- `contacts.client`: Client-side implementation of the remote provider protocol, specialized for the Contacts API

Financial tracking:

- `money.core`: Declaration of the Money API
- `money.database`: CLOVIS implementation of the Money API
- `money.server`: Server-side implementation of the remote provider protocol, specialized for the Money API
- `money.client`: Client-side implementation of the remote provider protocol, specialized for the Money API

### Apps

#### Client-side

The apps are built with Jetpack Compose UI, Compose for Desktop and Compose for Web.

The apps are split into the following modules:

- `app.components`: Multiplatform UI components for all core API objects.
- `app`: Desktop and JS apps.

#### Server-side

The `backend` module contains the sources for a Kotlin/JVM backend.
