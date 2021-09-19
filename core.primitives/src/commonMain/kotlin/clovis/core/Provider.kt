package clovis.core

import clovis.core.cache.Cache

interface Provider<R : Ref<R, O>, O> {

	val cache: Cache<R, O>

}
