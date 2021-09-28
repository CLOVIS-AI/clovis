package clovis.ui.layout

import androidx.compose.runtime.Composable

/**
 * A simple horizontal layout: the [contents] are displayed one after the other.
 * If the line is too long, the contents wrap to the next line.
 *
 * @see Column
 */
@Composable
fun Row(
	contents: @Composable () -> Unit,
) = RowImpl(contents)

@Composable
internal expect fun RowImpl(
	contents: @Composable () -> Unit,
)

/**
 * A simple vertical layout: the [contents] are displayed one below the other.
 *
 * @see Row
 */
@Composable
fun Column(
	contents: @Composable () -> Unit,
) = ColumnImpl(contents)

@Composable
internal expect fun ColumnImpl(
	contents: @Composable () -> Unit,
)
