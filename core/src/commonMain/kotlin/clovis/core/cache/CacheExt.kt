package clovis.core.cache

import clovis.core.Identifiable
import clovis.core.Provider
import kotlinx.coroutines.CoroutineScope

fun <Id : IdBound, O : Identifiable<Id>> Provider<Id, O>.cached() =
	DirectCache(this)

fun <Id : IdBound, O : Identifiable<Id>> Cache<Id, O>.cachedInMemory(scope: CoroutineScope) =
	MemoryCache(this, scope)
