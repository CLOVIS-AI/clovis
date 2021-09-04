package clovis.logger

import org.slf4j.LoggerFactory

private class LoggerJvm(obj: Any) : Logger {
	private val log = LoggerFactory.getLogger(obj::class.java)
		?: error("Couldn't instantiate a logger for ${obj::class}.")

	override var level = LogLevel.Default

	override fun forceTrace(message: String) = log.trace(message)

	override fun forceInfo(message: String) = log.info(message)

	override fun forceWarn(message: String) = log.warn(message)

	override fun forceError(message: String) = log.error(message)

}

actual fun logger(obj: Any): Logger = LoggerJvm(obj)
