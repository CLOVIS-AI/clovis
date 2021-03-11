package clovis.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import clovis.core.api.User

@Suppress("FunctionName")
@Composable
fun UserView(user: User) {
	Column {
		Text("Name: ${user.profile.fullName}")
		Text("Email: ${user.profile.email}")
	}
}
