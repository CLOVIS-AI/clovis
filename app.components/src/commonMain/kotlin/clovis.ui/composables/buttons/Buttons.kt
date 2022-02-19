package clovis.ui.composables.buttons

import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier

/**
 * A slightly more important button than [Button].
 *
 * @see ButtonDocumentation
 * @see Button
 */
@Composable
fun EmphasizedButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	contents: @Composable () -> Unit,
) = EmphasizedButtonImpl(onClick, modifier, contents)

/**
 * A regular button.
 *
 * @see ButtonDocumentation
 * @see EmphasizedButton
 */
@Composable
fun Button(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	contents: @Composable () -> Unit,
) = ButtonImpl(onClick, modifier, contents)

//region Implementation

/**
 * Implementation for [EmphasizedButton].
 */
@Composable
internal expect fun EmphasizedButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
)

/**
 * Implementation for [Button].
 */
@Composable
internal expect fun ButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
)

//endregion
