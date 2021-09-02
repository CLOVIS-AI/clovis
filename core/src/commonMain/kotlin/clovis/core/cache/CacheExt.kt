package clovis.core.cache

import clovis.core.Id
import clovis.core.Identifiable
import clovis.core.Provider
import kotlinx.coroutines.CoroutineScope

fun <I : Id, O : Identifiable<I>> Provider<I, O>.cached() =
	DirectCache(this)

fun <Id : clovis.core.Id, O : Identifiable<Id>> Cache<Id, O>.cachedInMemory(scope: CoroutineScope) =
	MemoryCache(this, scope)
