package clovis.ui.modifier

/**
 * Simple modification to an existing composable.
 *
 * The default modifier (modifier that does nothing) is [Modifier.Companion].
 * Thanks to modifiers, the following code can be written:
 * ```kotlin
 * Text(
 *     "Hello World",
 *     Modifier
 *         .padding(5.dp) // outer
 *         .border(red)   // middle
 *         .padding(5.dp) // inner
 * )
 * ```
 * Modifiers always apply around the component they decorate.
 */
expect open class Modifier {

	/**
	 * This modifier's outer modifier.
	 *
	 * A modifier's [parent] has an effect outside the modifier.
	 *
	 * `null` for the default modifier, than doesn't do anything.
	 */
	val parent: Modifier?

	companion object : Modifier
}

//region Extensions

/**
 * Goes through the [Modifier] hierarchy, starting at the current one and going up using [Modifier.parent].
 */
fun Modifier.asSequence() = sequence {
	// Add the current modifier to the sequence
	var modifier = this@asSequence
	yield(modifier)

	// Everytime a parent exists, add it
	// while(true)+break because Modifier is an interface, so Modifier.parent could return a different value every time it's called.
	// … Because of this, Kotlin cannot check the type.
	while (true) {
		modifier = modifier.parent ?: break
		yield(modifier)
	}
}

//endregion
