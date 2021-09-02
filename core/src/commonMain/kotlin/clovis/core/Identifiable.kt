package clovis.core

import clovis.core.cache.Cache

interface Id

interface Identifiable<I : Id> {
	val id: I
}

fun <I : Id, O : Identifiable<I>> I.from(cache: Cache<I, O>) = cache[this]
