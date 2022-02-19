package clovis.ui.composables.buttons

import androidx.compose.runtime.Composable
import clovis.ui.dimensions.dp
import clovis.ui.modifier.Modifier
import clovis.ui.modifier.roundedBorder
import clovis.ui.theme.LocalTheme
import org.jetbrains.compose.web.dom.Button as DomButton

/**
 * Implementation for [DomButton].
 */
@Composable
internal actual fun ButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
) = modifier.apply {
	DomButton(attrs = {
		this.onClick { onClick() }
	}) {
		contents()
	}
}

/**
 * Implementation for [EmphasizedButton].
 */
@Composable
internal actual fun EmphasizedButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
) = ButtonImpl(onClick,
               modifier.roundedBorder(1.dp, LocalTheme.current.palette.primary.highlight.light, radius = 1.dp),
               contents)
