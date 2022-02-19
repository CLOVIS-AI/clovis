@file:JvmName("ActionButtonsJvm")

package clovis.ui.composables.buttons

import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier
import clovis.ui.theme.LocalTheme
import clovis.ui.theme.color.toFoundation
import androidx.compose.material.Button as FoundationButton

/**
 * Implementation for [PrimaryActionButton].
 */
@Composable
internal actual fun PrimaryActionButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
) {
	FoundationButton(
		onClick,
		modifier.toFoundation(),
		colors = ButtonDefaults.buttonColors(
			backgroundColor = LocalTheme.current.palette.primary.surface.regular.toFoundation(),
			contentColor = LocalTheme.current.palette.primary.surface.onRegular.toFoundation()
		)
	) {
		contents()
	}
}

/**
 * Implementation for [SecondaryActionButton].
 */
@Composable
internal actual fun SecondaryActionButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
) {
	FoundationButton(
		onClick,
		modifier.toFoundation(),
		colors = ButtonDefaults.buttonColors(
			backgroundColor = LocalTheme.current.palette.primary.surface.dark.toFoundation(),
			contentColor = LocalTheme.current.palette.primary.surface.onDark.toFoundation()
		)
	) {
		contents()
	}
}
