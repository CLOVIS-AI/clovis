package clovis.ui.modifier

import androidx.compose.ui.Modifier as FoundationModifier

actual open class Modifier(
	actual val parent: Modifier?,
	private val foundationModifier: FoundationModifier,
) {

	/**
	 * Converts the CLOVIS [Modifier] to a Jetpack Compose [Modifier][FoundationModifier].
	 */
	fun toFoundation() = asSequence()
		.map { it.foundationModifier }
		.fold(FoundationModifier.Companion as FoundationModifier) { acc, it -> acc.then(it) }

	actual companion object : Modifier(null, FoundationModifier)
}
