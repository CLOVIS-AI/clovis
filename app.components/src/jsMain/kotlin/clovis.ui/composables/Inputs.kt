package clovis.ui.composables

import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.PasswordInput as DomPasswordInput
import org.jetbrains.compose.web.dom.TextInput as DomTextInput

@Composable
internal actual fun TextInputImpl(
	text: String,
	onChange: (String) -> Unit,
	modifier: Modifier,
	allowMultiline: Boolean,
	allowResize: Boolean
) {
	//TODO modifier
	if (!allowMultiline && !allowResize) {
		DomTextInput(value = text) {
			onInput { onChange(it.value) }

			style {
				display(DisplayStyle.Block)
			}
		}
	} else {
		TextArea(value = text) {
			onInput { onChange(it.value) }

			rows(5)
			style {
				display(DisplayStyle.Block)
			}
		}
	}
}

@Composable
internal actual fun PasswordInputImpl(
	password: String,
	onChange: (String) -> Unit,
	modifier: Modifier
) {
	//TODO modifier
	DomPasswordInput(value = password) {
		onInput { onChange(it.value) }

		style {
			display(DisplayStyle.Block)
		}
	}
}
