@file:JvmName("TextJvm")

package clovis.ui.composables

import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier
import androidx.compose.material.Text as MaterialText

@Composable
internal actual fun TextImpl(
	text: String,
	modifier: Modifier,
) {
	MaterialText(text, modifier.toFoundation())
}
