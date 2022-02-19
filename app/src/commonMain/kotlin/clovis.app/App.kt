package clovis.app

import androidx.compose.runtime.*
import clovis.app.login.AccountCreation
import clovis.app.login.AccountLogIn
import clovis.client.Client
import clovis.ui.composables.Text
import clovis.ui.composables.buttons.Button
import clovis.ui.layout.Column
import clovis.ui.layout.Row
import kotlinx.coroutines.launch

val defaultClient = Client.Anonymous.connect("http://localhost:8000/api")
var client by mutableStateOf<Client>(defaultClient)

@Composable
fun App() {
	when (val client = client) {
		is Client.Anonymous -> {
			Row {
				AccountCreation()
				AccountLogIn()
			}
		}
		is Client.Authenticated -> {
			val scope = rememberCoroutineScope()

			Column {
				Row {
					Text("Hi!")
					Button(onClick = { scope.launch { client.logOut(); clovis.app.client = defaultClient } }) {
						Text("Log out")
					}
				}
			}
		}
	}
}
