package clovis.ui.modifier

import clovis.ui.dimensions.DensityPixel
import clovis.ui.theme.color.Color

expect fun Modifier.roundedBorder(width: DensityPixel, color: Color, radius: DensityPixel): Modifier
