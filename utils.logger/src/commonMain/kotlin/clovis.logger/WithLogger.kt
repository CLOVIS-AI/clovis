package clovis.logger

/**
 * Simple utility to declare a logger, that can afterwards be used easily.
 *
 * The target usage looks something like this:
 * ```kotlin
 * class Foo {
 *     init {
 *         log.trace { "I can use the logger here!" }
 *     }
 *
 *     companion object : WithLogger()
 * }
 * ```
 *
 * To avoid polluting the user's API, the [logger][log] is declared as `protected`.
 * However, that requires declaring this utility as an `abstract class`, as `interface` doesn't support the `protected` visibility.
 *
 * If you need your companion object to inherit from some other class, you can instead declare the logger yourself:
 * ```kotlin
 * class Foo {
 *     init {
 *         log.trace { "I can use the logger here, as well!" }
 *     }
 *
 *     companion object {
 *         private val log = logger(this)
 *     }
 * }
 * ```
 */
abstract class WithLogger {

	@Suppress("LeakingThis") // No properties are read, just the class name, so it should be safe.
	protected val log = logger(this)

}
