package clovis.ui.composables

import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.dom.P as DomParagraph
import org.jetbrains.compose.web.dom.Text as DomText

@Composable
internal actual fun TextImpl(
	text: String,
	modifier: Modifier,
) {
	//TODO handle the modifier
	DomParagraph(
		attrs = {
			style {
				display(DisplayStyle.Block)
			}
		}
	) {
		DomText(text)
	}
}
