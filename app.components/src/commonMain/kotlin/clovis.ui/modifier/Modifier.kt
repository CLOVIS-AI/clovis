package clovis.ui.modifier

interface Modifier {
	open class Element : Modifier
	companion object : Element()
}
