package clovis.ui.composables.buttons

import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier
import clovis.ui.theme.LocalTheme
import clovis.ui.theme.color.toCss
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.dom.Button as DomButton

/**
 * Implementation for [PrimaryActionButton].
 */
@Composable
internal actual fun PrimaryActionButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
) = modifier.apply {
	val background = LocalTheme.current.palette.primary.surface.regular
	val text = LocalTheme.current.palette.primary.surface.onRegular

	DomButton(attrs = {
		style {
			backgroundColor(background.toCss())
			color(text.toCss())
		}
		onClick { onClick() }
	}) {
		contents()
	}
}

/**
 * Implementation for [SecondaryActionButton].
 */
@Composable
internal actual fun SecondaryActionButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
) = modifier.apply {
	val background = LocalTheme.current.palette.primary.surface.light
	val text = LocalTheme.current.palette.primary.surface.onLight

	DomButton(attrs = {
		style {
			backgroundColor(background.toCss())
			color(text.toCss())
		}
		onClick { onClick() }
	}) {
		contents()
	}
}
