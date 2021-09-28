@file:JvmName("ButtonJvm")

package clovis.ui.composables

import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier
import clovis.ui.modifier.asFoundation

@Composable
internal actual fun ButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
) {
	TextButton(
		onClick = onClick,
		modifier = modifier.asFoundation().modifier,
	) {
		contents()
	}
}
