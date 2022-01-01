package clovis.ui.theme.color

/**
 * Variants of colors that are used in different parts of the UI.
 */
data class Variants(

	/**
	 * Shades used to highlight parts of the UI.
	 */
	val highlight: Shades,

	/**
	 * Less saturated shades than the [highlight], used for less important areas, such as surface colors.
	 *
	 * These shades will be lighter than the [highlight] in a light theme, and darker than the [highlight] in a dark theme.
	 */
	val surface: Shades,
)
