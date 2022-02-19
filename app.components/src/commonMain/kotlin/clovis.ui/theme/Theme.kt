package clovis.ui.theme

import androidx.compose.runtime.compositionLocalOf
import clovis.ui.theme.color.*

/**
 * The overall UI theme of the app.
 */
data class Theme(

	/**
	 * Colors used by the app.
	 */
	val palette: Palette,
)

val LocalTheme = compositionLocalOf {
	Theme(
		Palette(
			primary = Variants(
				highlight = Shades(
					regular = Color.purple500,
					light = Color.purple600,
					dark = Color.purple400,

					onRegular = Color.slate50,
					onLight = Color.slate50,
					onDark = Color.slate50,
				),
				surface = Shades(
					regular = Color.purple800,
					light = Color.purple900,
					dark = Color.purple700,

					onRegular = Color.slate50,
					onLight = Color.slate50,
					onDark = Color.slate50,
				)
			),

			secondary = Variants(
				highlight = Shades(
					regular = Color.cyan600,
					light = Color.cyan500,
					dark = Color.cyan700,

					onRegular = Color.slate50,
					onLight = Color.slate50,
					onDark = Color.slate50,
				),
				surface = Shades(
					regular = Color.cyan900,
					light = Color.cyan800,
					dark = Color.cyan700,

					onRegular = Color.slate50,
					onLight = Color.slate50,
					onDark = Color.slate50,
				)
			),

			tertiary = emptyList(),

			error = Variants(
				highlight = Shades(
					regular = Color.red700,
					light = Color.red600,
					dark = Color.red800,

					onRegular = Color.red50,
					onLight = Color.red50,
					onDark = Color.red50,
				),
				surface = Shades(
					regular = Color.red100,
					light = Color.red50,
					dark = Color.red200,

					onRegular = Color.neutral900,
					onLight = Color.neutral900,
					onDark = Color.neutral900,
				)
			),

			background = Variants(
				highlight = Shades(
					regular = Color.slate100,
					light = Color.slate50,
					dark = Color.slate200,

					onRegular = Color.slate900,
					onLight = Color.slate900,
					onDark = Color.slate900,
				),
				surface = Shades(
					regular = Color.slate100,
					light = Color.slate50,
					dark = Color.slate200,

					onRegular = Color.slate900,
					onLight = Color.slate900,
					onDark = Color.slate900,
				)
			)
		)
	)
}
