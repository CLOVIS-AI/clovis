package clovis.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import clovis.client.Client
import clovis.client.users.getUser
import clovis.core.api.User
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val client = Client(Url("http://localhost:8000"))

typealias Setter<T> = (T) -> Unit
typealias OnClickEvent = () -> Unit

@Suppress("FunctionName")
@Composable
fun HomeScreen() {
	val scope = rememberCoroutineScope()
	val (user, setUser) = remember { mutableStateOf<User?>(null) }

	MaterialTheme {
		Column {
			Button(onClick = loadUserEvent(scope, setUser)) {
				Text("Load user 1")
			}
			Button(onClick = { setUser(null) }) {
				Text("Forget user")
			}
			if (user != null)
				UserView(user)
		}
	}
}

private fun loadUserEvent(
	scope: CoroutineScope,
	setUser: Setter<User?>
): OnClickEvent = { scope.launch { setUser(client.getUser(1)) } }
