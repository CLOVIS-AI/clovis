package clovis.ui.composables

import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier

/**
 * Displays [text] to the user.
 */
@Composable
fun Text(
	text: String,
	modifier: Modifier = Modifier,
) {
	TextImpl(text, modifier)
}

internal expect fun TextImpl(
	text: String,
	modifier: Modifier,
)
