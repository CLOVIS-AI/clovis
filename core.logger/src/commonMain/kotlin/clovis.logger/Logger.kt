package clovis.logger

interface Logger {

	var level: LogLevel

	fun forceTrace(message: String)
	fun forceInfo(message: String)
	fun forceWarn(message: String)
	fun forceError(message: String)

}

inline fun Logger.trace(lazyMessage: () -> String) {
	if (level.trace)
		forceTrace(lazyMessage())
}

inline fun Logger.info(lazyMessage: () -> String) {
	if (level.info)
		forceInfo(lazyMessage())
}

inline fun Logger.warn(lazyMessage: () -> String) {
	if (level.warn)
		forceWarn(lazyMessage())
}

inline fun Logger.error(lazyMessage: () -> String) {
	if (level.error)
		forceError(lazyMessage())
}

expect fun logger(obj: Any): Logger
