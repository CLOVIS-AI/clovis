package clovis.logger

private class LoggerJs(val obj: Any) : Logger {

	override var level = LogLevel.Default

	override fun forceTrace(message: String) {
		console.log(obj, message)
	}

	override fun forceInfo(message: String) {
		console.info(obj, message)
	}

	override fun forceWarn(message: String) {
		console.warn(obj, message)
	}

	override fun forceError(message: String) {
		console.error(obj, message)
	}
}

actual fun logger(obj: Any): Logger = LoggerJs(obj)
