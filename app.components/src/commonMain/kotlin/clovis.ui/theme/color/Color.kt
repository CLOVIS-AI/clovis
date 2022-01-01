package clovis.ui.theme.color

/**
 * Color representation, with transparency.
 */
data class Color(
	val red: UByte,

	val green: UByte,

	val blue: UByte,

	/**
	 * Opacity.
	 *
	 * 0 is fully transparent, 255 is not transparent at all.
	 */
	val alpha: UByte = 255u,
) {

	companion object
}

fun Color.Companion.fromHex(hex: String): Color {
	val withoutHash = hex.removePrefix("#")

	require(withoutHash.length == 6) { "An hexadecimal color representation should container 6 digits, but ${withoutHash.length} digits were found: '$withoutHash'" }

	val red = withoutHash.substring(0, 2).toUByte(radix = 16)
	val green = withoutHash.substring(2, 4).toUByte(radix = 16)
	val blue = withoutHash.substring(4, 6).toUByte(radix = 16)

	return Color(red, green, blue)
}
