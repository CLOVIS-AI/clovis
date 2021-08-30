package clovis.core

import clovis.core.cache.IdBound

interface Identifiable<Id : IdBound> {
	val id: Id
}
