package clovis.core

typealias IdBound = Any

interface Identifiable<Id : IdBound> {
	val id: Id
}
