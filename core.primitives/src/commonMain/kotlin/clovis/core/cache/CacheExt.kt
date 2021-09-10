package clovis.core.cache

import clovis.core.Id
import clovis.core.Provider
import clovis.core.Result
import clovis.core.done
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first

//region Cache implementation shortcuts

fun <I : Id<O>, O> Provider<I, O>.cached() =
	DirectCache(this)

fun <I : Id<O>, O> Cache<I, O>.cachedInMemory(scope: CoroutineScope) =
	MemoryCache(this, scope)

//endregion
//region Cache extensions

suspend fun <I : Id<O>, O> Cache<I, O>.getOrThrow(id: I): O {
	val result = get(id).first { it.done }

	return if (result is Result.Success) result.value
	else throw RuntimeException(result.toString())
}

//endregion
