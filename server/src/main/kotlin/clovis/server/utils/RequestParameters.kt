package clovis.server.utils

import arrow.core.Left
import arrow.core.Right
import clovis.server.ParameterType
import io.ktor.application.*

/**
 * Finds the value of a request's parameter from its [name].
 */
inline fun <reified T> ApplicationCall.param(name: String): Request<T> {
	val data = parameters[name] ?: return Left(ParameterType(name, null, T::class.java))

	@Suppress("UNCHECKED_CAST")
	return when (T::class) {
		Integer::class -> data.toTOrEither(name) { it.toIntOrNull() } as Request<T>
		Long::class -> data.toTOrEither(name) { it.toLongOrNull() } as Request<T>
		String::class -> Right(data) as Request<T>
		else -> error("Cannot parse parameter type ${T::class}.")
	}
}

/**
 * Internal function to [transform] a String to another value.
 * @param name The name of the parameter being converted.
 */
inline fun <reified T> String.toTOrEither(name: String, transform: (String) -> T?): Request<T> {
	val v = transform(this)

	return if (v != null)
		Right(v)
	else
		Left(ParameterType(name, this, T::class.java))
}
