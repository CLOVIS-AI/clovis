@file:JvmName("DirectionalJvm")

package clovis.ui.layout

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column as FoundationColumn
import androidx.compose.foundation.layout.Row as FoundationRow

@Composable
internal actual fun RowImpl(
	contents: @Composable () -> Unit,
) {
	FoundationRow {
		contents()
	}
}

@Composable
internal actual fun ColumnImpl(
	contents: @Composable () -> Unit,
) {
	FoundationColumn {
		contents()
	}
}
