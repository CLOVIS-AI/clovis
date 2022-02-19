package clovis.ui.modifier

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import clovis.ui.dimensions.DensityPixel
import clovis.ui.dimensions.toFoundation
import clovis.ui.theme.color.Color
import clovis.ui.theme.color.toFoundation

actual fun Modifier.roundedBorder(
	width: DensityPixel,
	color: Color,
	radius: DensityPixel,
) = Modifier(
	this,
	androidx.compose.ui.Modifier.border(
		width.toFoundation(),
		color.toFoundation(),
		RoundedCornerShape(radius.toFoundation())
	)
)
