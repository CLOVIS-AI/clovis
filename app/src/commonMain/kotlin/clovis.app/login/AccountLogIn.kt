package clovis.app.login

import androidx.compose.runtime.*
import clovis.app.client
import clovis.client.Client.Authenticated.Companion.logIn
import clovis.ui.composables.Button
import clovis.ui.composables.PasswordInput
import clovis.ui.composables.Text
import clovis.ui.composables.TextInput
import clovis.ui.layout.Column
import kotlinx.coroutines.launch

@Composable
fun AccountLogIn() {
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	val scope = rememberCoroutineScope()

	Column {
		Text("Email address")
		TextInput(email, onChange = { email = it })

		Text("Password")
		PasswordInput(password, onChange = { password = it })

		Button(
			onClick = {
				scope.launch {
					client = client.logIn(email, password)
				}
			}
		) {
			Text("Log in")
		}
	}
}
