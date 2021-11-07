package clovis.core.cache

import clovis.core.Ref
import clovis.core.firstResultOrThrow
import kotlinx.coroutines.CoroutineScope

//region Cache implementation shortcuts

fun <O> Cache<O>.cachedInMemory(scope: CoroutineScope) =
	MemoryCache(this, scope)

//endregion
//region Cache extensions

suspend fun <O> Cache<O>.getOrThrow(id: Ref<O>): O =
	get(id).firstResultOrThrow()

//endregion
