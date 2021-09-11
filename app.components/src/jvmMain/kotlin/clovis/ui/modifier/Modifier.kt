package clovis.ui.modifier

import androidx.compose.ui.Modifier as FoundationModifier

private class ModifierElement : FoundationModifier.Element

/**
 * Implementation of the [Modifier] multiplatform class that is used to convert calls to the equivalent [FoundationModifier] calls.
 */
internal class ActualModifier : Modifier {
	var modifier: FoundationModifier = ModifierElement()
}

/**
 * Gets an implementation of the [ActualModifier].
 */
internal fun Modifier.asFoundation(): ActualModifier =
	(this as? ActualModifier) ?: ActualModifier()
