@file:JvmName("InputsJvm")

package clovis.ui.composables

import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import clovis.ui.modifier.Modifier
import clovis.ui.modifier.asFoundation

@Composable
internal actual fun TextInputImpl(
	text: String,
	onChange: (String) -> Unit,
	modifier: Modifier,
	allowMultiline: Boolean,
	allowResize: Boolean
) {
	TextField(
		text,
		onValueChange = onChange,
		modifier = modifier.asFoundation().modifier,
		singleLine = !allowMultiline,
	)
}

private val passwordTranformation = PasswordVisualTransformation()

@Composable
internal actual fun PasswordInputImpl(
	password: String,
	onChange: (String) -> Unit,
	modifier: Modifier
) {
	TextField(
		password,
		onValueChange = onChange,
		modifier = modifier.asFoundation().modifier,
		visualTransformation = passwordTranformation
	)
}
