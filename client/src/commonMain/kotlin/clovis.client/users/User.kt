package clovis.client.users

import clovis.client.Client
import clovis.core.api.User

suspend fun Client.getUser(id: Long) =
	get<User>("$url/ping/anonymous/$id")
