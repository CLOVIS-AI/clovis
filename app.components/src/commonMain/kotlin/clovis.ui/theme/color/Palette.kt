package clovis.ui.theme.color

/**
 * An overall color theme.
 *
 * A [Palette] is composed of a [primary] color, a [secondary] color, an [error] color, and different backgrounds.
 */
data class Palette(

	/**
	 * The primary color is visible all over the app.
	 */
	val primary: Variants,

	/**
	 * The secondary color is optional, and should be used sparingly to accent and highlight parts of the UI.
	 *
	 * For example, good usages are:
	 * - Floating action buttons
	 * - Selection controls (sliders, switches)
	 * - Selected text
	 * - Progress bars
	 * - Links, headlines
	 */
	val secondary: Variants,

	/**
	 * Tertiary colors can be used when more colors are needed.
	 *
	 * @see tertiary
	 */
	val tertiary: List<Variants>,

	/**
	 * The color used to express failure.
	 */
	val error: Variants,

	/**
	 * Background colors.
	 */
	val background: Variants,
)

/**
 * Utility function to access tertiary colors.
 *
 * The objective is for the developer to be able to write an app without knowing how many [Palette.tertiary] colors are available.
 * Instead, the developer can request a specific [index], and this function will choose one of the available colors.
 * If no tertiary colors are available, this method will default to the [Palette.secondary] color.
 */
fun Palette.tertiary(index: Int) = tertiary.getOrElse(index % tertiary.size) { secondary }
