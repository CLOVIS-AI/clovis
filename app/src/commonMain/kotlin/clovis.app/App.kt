package clovis.app

import androidx.compose.runtime.*
import clovis.ui.composables.Button
import clovis.ui.composables.PasswordInput
import clovis.ui.composables.Text
import clovis.ui.composables.TextInput
import clovis.ui.layout.Column

@Composable
fun App() {
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	Column {
		TextInput(email, onChange = { email = it })
		PasswordInput(password, onChange = { password = it })

		Button(
			onClick = {}
		) {
			Text("Login")
		}
	}
}
