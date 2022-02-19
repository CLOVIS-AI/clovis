package clovis.ui.modifier

import clovis.ui.dimensions.DensityPixel
import clovis.ui.dimensions.toCss
import clovis.ui.theme.color.Color
import clovis.ui.theme.color.toCss
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

actual fun Modifier.roundedBorder(
	width: DensityPixel,
	color: Color,
	radius: DensityPixel,
) = Modifier(this) { element ->
	Div(attrs = {
		style {
			borderRadius(radius.toCss())
			border {
				width(width.toCss())
				color(color.toCss())
			}
		}
	}) {
		element()
	}
}
