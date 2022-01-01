package clovis.ui.theme.color

/**
 * Color representation, with transparency.
 */
data class Color(

	/**
	 * Opacity.
	 *
	 * 0 is fully transparent, 255 is not transparent at all.
	 */
	val alpha: UByte,

	val red: UByte,

	val green: UByte,

	val blue: UByte,
)
