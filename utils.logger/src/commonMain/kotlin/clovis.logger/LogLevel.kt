package clovis.logger

enum class LogLevel {
	// DO NOT switch the order of enum elements
	TRACE,
	INFO,
	WARN,
	ERROR,
	NONE,
	;

	val trace = ordinal <= 0
	val info = ordinal <= 1
	val warn = ordinal <= 2
	val error = ordinal <= 3

	companion object {
		val Default = TRACE
	}
}
