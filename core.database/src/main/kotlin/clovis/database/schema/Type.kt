package clovis.database.schema

import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalTime

/**
 * Mapper between C* and Kotlin types.
 */
sealed interface Type<T : Any?> {

	/**
	 * Encodes the [value] into a C* query-compatible [String].
	 */
	fun encode(value: T): String

	/**
	 * Decodes the [value] from a C* query-compatible [String] into a Kotlin type.
	 */
	fun decode(value: String): T

	/**
	 * The name of the C* type mapped by this [Type], as it appears in queries.
	 */
	val type: String

	object Binary {

		object Text : Type<String> {
			override val type = "text"
			override fun encode(value: String) = value
			override fun decode(value: String) = value
		}

		object TextASCII : Type<String> {
			override val type = "text"
			override fun encode(value: String) = value
			override fun decode(value: String) = value
		}

		object Binary : Type<ByteArray> {
			override val type = "blob"
			override fun encode(value: ByteArray) = value.decodeToString()
			override fun decode(value: String) = value.encodeToByteArray()
		}

		object Boolean : Type<kotlin.Boolean> {
			override val type = "boolean"
			override fun encode(value: kotlin.Boolean) = value.toString()
			override fun decode(value: String) = value.toBooleanStrict()
		}

		object Inet : Type<String> by Text {
			override val type = "inet"
		}

		object UUID : Type<java.util.UUID> {
			override val type = "uuid"
			override fun encode(value: java.util.UUID) = value.toString()
			override fun decode(value: String): java.util.UUID = java.util.UUID.fromString(value)
		}
	}

	object Number {
		//region Integers

		object Byte : Type<kotlin.Byte> {
			override val type = "tinyint"
			override fun encode(value: kotlin.Byte) = value.toString()
			override fun decode(value: String) = value.toByte()
		}

		object Short : Type<kotlin.Short> {
			override val type = "smallint"
			override fun encode(value: kotlin.Short) = value.toString()
			override fun decode(value: String) = value.toShort()
		}

		object Int : Type<kotlin.Int> {
			override val type = "int"
			override fun encode(value: kotlin.Int) = value.toString()
			override fun decode(value: String) = value.toInt()
		}

		object Long : Type<kotlin.Long> {
			override val type = "bigint"
			override fun encode(value: kotlin.Long) = value.toString()
			override fun decode(value: String) = value.toLong()
		}

		object Counter : Type<kotlin.Long> by Long {
			override val type = "counter"
		}

		object BigInteger : Type<java.math.BigInteger> {
			override val type = "varint"
			override fun encode(value: java.math.BigInteger) = value.toString()
			override fun decode(value: String): java.math.BigInteger = BigInteger(value)
		}

		//endregion
		//region Floating-point

		object Float : Type<kotlin.Float> {
			override val type = "float"
			override fun encode(value: kotlin.Float) = value.toString()
			override fun decode(value: String) = value.toFloat()
		}

		object Double : Type<kotlin.Double> {
			override val type = "double"
			override fun encode(value: kotlin.Double) = value.toString()
			override fun decode(value: String) = value.toDouble()
		}

		object BigDecimal : Type<java.math.BigDecimal> {
			override val type = "decimal"
			override fun encode(value: java.math.BigDecimal) = value.toString()
			override fun decode(value: String) = BigDecimal(value)
		}

		//endregion
	}

	object Dates {
		object Timestamp : Type<Instant> {
			override val type = "timestamp"
			override fun encode(value: Instant) = value.toString()
			override fun decode(value: String): Instant = Instant.parse(value)
		}

		object Date : Type<Instant> {
			override val type = "date"
			override fun encode(value: Instant) = value.epochSecond.toString()
			override fun decode(value: String): Instant = Instant.ofEpochSecond(value.toLong())
		}

		object Time : Type<LocalTime> {
			override val type = "time"
			override fun encode(value: LocalTime) = value.toString()
			override fun decode(value: String): LocalTime = LocalTime.parse(value)
		}

		object Duration : Type<java.time.Duration> {
			override val type = "duration"
			override fun encode(value: java.time.Duration) = value.toString()
			override fun decode(value: String): java.time.Duration = java.time.Duration.parse(value)
		}
	}
}
