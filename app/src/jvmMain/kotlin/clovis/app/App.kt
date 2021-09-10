package clovis.app

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
	Window(
		onCloseRequest = ::exitApplication,
		title = "CLOVIS Assistant",
		state = rememberWindowState(width = 300.dp, height = 300.dp)
	) {
		MaterialTheme {
			Text("Hello world!")
		}
	}
}
