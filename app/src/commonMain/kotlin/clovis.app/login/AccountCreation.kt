package clovis.app.login

import androidx.compose.runtime.*
import clovis.app.client
import clovis.client.Client.Authenticated.Companion.createAccount
import clovis.ui.composables.PasswordInput
import clovis.ui.composables.Text
import clovis.ui.composables.TextInput
import clovis.ui.composables.buttons.Button
import clovis.ui.layout.Column
import kotlinx.coroutines.launch

@Composable
fun AccountCreation() {
	var email by remember { mutableStateOf("") }
	var password1 by remember { mutableStateOf("") }
	var password2 by remember { mutableStateOf("") }

	val scope = rememberCoroutineScope()

	Column {
		Text("Email address")
		TextInput(email, onChange = { email = it })

		Text("Password")
		PasswordInput(password1, onChange = { password1 = it })

		Text("Password (confirmation)")
		PasswordInput(password2, onChange = { password2 = it })

		if (password1 != password2)
			Text("Both passwords are different!")

		Button(
			onClick = {
				if (password1 == password2) {
					scope.launch {
						client = client.createAccount(email, password1)
					}
				}
			}
		) {
			Text("Create the account")
		}
	}
}
