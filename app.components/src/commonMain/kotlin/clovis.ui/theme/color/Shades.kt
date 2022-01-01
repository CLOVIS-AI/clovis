package clovis.ui.theme.color

/**
 * Color shades.
 *
 * Each color should have a lighter and darker shade, which may be used to break out color repetition.
 */
data class Shades(
	/**
	 * The normal shader of this color.
	 */
	val regular: Color,

	/**
	 * A lighter version of this color, that can be used as an accent to break repetition.
	 */
	val light: Color,

	/**
	 * A darker version of this color, that can be used as an accent to break repetition.
	 */
	val dark: Color,

	//region 'On' shades
	/**
	 * The color of text and other elements positioned on the [regular] shade.
	 */
	val onRegular: Color,

	/**
	 * The color of text and other elements positioned on the [light] shade.
	 */
	val onLight: Color,

	/**
	 * The color of text and other elements positioned on the [dark] shade.
	 */
	val onDark: Color,
	//endregion
)
