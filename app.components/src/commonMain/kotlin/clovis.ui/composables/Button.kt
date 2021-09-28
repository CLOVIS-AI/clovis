package clovis.ui.composables

import androidx.compose.runtime.Composable
import clovis.ui.modifier.Modifier

@Composable
fun Button(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	contents: @Composable () -> Unit,
) = ButtonImpl(onClick, modifier, contents)

@Composable
internal expect fun ButtonImpl(
	onClick: () -> Unit,
	modifier: Modifier,
	contents: @Composable () -> Unit,
)
