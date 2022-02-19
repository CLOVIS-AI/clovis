package clovis.ui.modifier

import androidx.compose.runtime.Composable

/**
 * Simple modification to an existing composable.
 */
actual open class Modifier(
	actual val parent: Modifier?,
	private val outer: @Composable (@Composable () -> Unit) -> Unit = {},
) {

	/**
	 * Applies this modifier to the [element] it decorates.
	 */
	@Composable
	fun apply(element: @Composable () -> Unit) {
		if (parent == null)
			outer(element)
		else
			parent.apply { outer(element) }
	}

	actual companion object : Modifier(null)
}
