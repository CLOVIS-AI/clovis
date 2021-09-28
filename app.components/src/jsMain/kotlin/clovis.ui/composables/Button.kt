package clovis.ui.composables

import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.dom.Button as DomButton

@Composable
internal actual fun ButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
) {
	//TODO apply modifier
	DomButton(attrs = {
		this.onClick { onClick() }

		style {
			display(DisplayStyle.Block)
		}
	}) {
		contents()
	}
}
