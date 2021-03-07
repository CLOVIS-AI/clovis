package clovis.client.users

import clovis.client.Client
import clovis.core.api.User
import io.ktor.client.request.*

suspend fun Client.getUser(id: Long): User =
	client.get("$url/ping/$id")
