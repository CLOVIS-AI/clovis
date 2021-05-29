package clovis.server.utils

import arrow.core.Left
import arrow.core.Right
import clovis.server.ParameterType
import io.ktor.application.*
import io.ktor.request.*

/**
 * Finds the value of a request's route parameter from its [name].
 *
 * Wrapper around [ApplicationCall.parameters].
 */
inline fun <reified T : Any> ApplicationCall.routeParam(name: String): Request<T> = convertParam(name, parameters[name])

/**
 * Finds the value of a request's URL parameter from its [name].
 *
 * Wrapper around [ApplicationRequest.queryParameters].
 */
inline fun <reified T : Any> ApplicationCall.urlParam(name: String): Request<T> =
	convertParam(name, request.queryParameters[name])

/**
 * Gets the application's body.
 *
 * Wrapper around [ApplicationCall.receiveOrNull].
 */
suspend inline fun <reified T : Any> ApplicationCall.bodyParam(): Request<T> {
	val data = receiveOrNull<T>() ?: return Left(ParameterType("<request body>", null, T::class.java))

	return Right(data)
}

/**
 * Finds the value of a request's parameter from its [name].
 */
inline fun <reified T : Any> ApplicationCall.convertParam(name: String, value: String?): Request<T> {
	val data = value ?: return Left(ParameterType(name, null, T::class.java))

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
inline fun <reified T : Any> String.toTOrEither(name: String, transform: (String) -> T?): Request<T> {
	val v = transform(this)

	return if (v != null)
		Right(v)
	else
		Left(ParameterType(name, this, T::class.java))
}
