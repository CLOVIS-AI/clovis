package clovis.ui.theme.color

import org.jetbrains.compose.web.css.rgba

fun Color.toCss() = rgba(
	red.toFloat() / 255,
	green.toFloat() / 255,
	blue.toFloat() / 255,
	alpha.toFloat() / 255,
)
