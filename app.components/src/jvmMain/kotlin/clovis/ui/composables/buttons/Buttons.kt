@file:JvmName("ButtonsJvm")

package clovis.ui.composables.buttons

import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier

/**
 * Implementation for [Button].
 */
@Composable
internal actual fun ButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
) {
	TextButton(
		onClick = onClick,
		modifier = modifier.toFoundation(),
	) {
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
) {
	OutlinedButton(
		onClick = onClick,
		modifier = modifier.toFoundation(),
	) {
		contents()
	}
}
