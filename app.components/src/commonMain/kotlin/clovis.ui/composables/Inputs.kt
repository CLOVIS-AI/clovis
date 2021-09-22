package clovis.ui.composables

import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier

@Composable
fun TextInput(
	text: String,
	onChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	allowMultiline: Boolean = false,
	allowResize: Boolean = false,
) = TextInputImpl(text, onChange, modifier, allowMultiline, allowResize)

@Composable
internal expect fun TextInputImpl(
	text: String,
	onChange: (String) -> Unit,
	modifier: Modifier,
	allowMultiline: Boolean,
	allowResize: Boolean,
)

@Composable
fun PasswordInput(
	password: String,
	onChange: (String) -> Unit,
	modifier: Modifier = Modifier,
) = PasswordInputImpl(password, onChange, modifier)

@Composable
internal expect fun PasswordInputImpl(
	password: String,
	onChange: (String) -> Unit,
	modifier: Modifier,
)
