package clovis.core

import clovis.core.cache.Cache

@kotlin.Suppress("unused") // T is a type-safe marker
interface Id<out T : Any?>

fun <I : Id<O>, O> I.from(cache: Cache<I, O>) = cache[this]
