package clovis.compose

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize

fun main() {
	Window(title = "Compose for Desktop", size = IntSize(300, 300)) {
		HomeScreen()
	}
}
