package clovis.ui.composables.buttons

import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier

/**
 * The most important button in the whole page.
 *
 * @see ButtonDocumentation
 * @see SecondaryActionButton
 */
@Composable
fun PrimaryActionButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	contents: @Composable () -> Unit,
) = PrimaryActionButtonImpl(onClick, modifier, contents)

/**
 * Important buttons that are of secondary importance.
 * The most important buttons of the page, excluding the [PrimaryActionButton].
 *
 * @see ButtonDocumentation
 * @see SecondaryActionButton
 */
@Composable
fun SecondaryActionButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	contents: @Composable () -> Unit,
) = SecondaryActionButtonImpl(onClick, modifier, contents)

//region Implementation

/**
 * Implementation for [PrimaryActionButton].
 */
@Composable
internal expect fun PrimaryActionButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
)

/**
 * Implementation for [SecondaryActionButton].
 */
@Composable
internal expect fun SecondaryActionButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
)

//endregion
