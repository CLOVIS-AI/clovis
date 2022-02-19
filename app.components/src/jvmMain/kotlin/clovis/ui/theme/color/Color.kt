@file:JvmName("ColorJvm")

package clovis.ui.theme.color

import androidx.compose.ui.graphics.Color as AndroidColor

fun Color.toFoundation() = AndroidColor(
	red.toInt(), green.toInt(), blue.toInt(), alpha.toInt()
)
