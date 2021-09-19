package clovis.core.cache

import clovis.core.Ref
import clovis.core.awaitResultOrThrow
import kotlinx.coroutines.CoroutineScope

//region Cache implementation shortcuts

fun <R, O> Cache<R, O>.cachedInMemory(scope: CoroutineScope) where R : Ref<R, O> =
	MemoryCache(this, scope)

//endregion
//region Cache extensions

suspend fun <R, O> Cache<R, O>.getOrThrow(id: R): O where R : Ref<R, O> =
	get(id).awaitResultOrThrow()

//endregion
