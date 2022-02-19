package clovis.ui.dimensions

import kotlin.jvm.JvmInline

/**
 * A [Density-independent pixel][DensityPixel] is an imaginary measure that changes size depending on the screen resolution, so that objects are the same visual size no matter the screen they are displayed on.
 */
@JvmInline
value class DensityPixel(val value: Int)

/**
 * Converts a size to [DensityPixel].
 */
val Int.dp get() = DensityPixel(this)
