package clovis.core.cache

import clovis.core.Id
import clovis.core.Provider
import kotlinx.coroutines.CoroutineScope

fun <I : Id<O>, O> Provider<I, O>.cached() =
	DirectCache(this)

fun <I : Id<O>, O> Cache<I, O>.cachedInMemory(scope: CoroutineScope) =
	MemoryCache(this, scope)
