package clovis.database.schema

import clovis.database.utils.asStringLiteral
import clovis.database.utils.fromStringLiteral
import com.datastax.oss.driver.api.core.type.reflect.GenericType
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
	 * The name of the C* type mapped by this [Type], as it appears in queries.
	 */
	val type: String

	val codec: GenericType<out T>

	fun defaultToString() = this::class.qualifiedName?.removePrefix("clovis.database.schema.Type.") ?: type

	sealed class StatelessType<T : Any?> : Type<T> {
		override fun toString() = defaultToString()
	}

	object Binary {

		object Text : StatelessType<String>() {
			override val codec: GenericType<String> get() = GenericType.STRING
			override val type = "text"
			override fun encode(value: String) = value.asStringLiteral()
		}

		object TextASCII : StatelessType<String>() {
			override val codec: GenericType<String> get() = GenericType.STRING
			override val type = "ascii"
			override fun encode(value: String) = value.asStringLiteral()
		}

		object Binary : StatelessType<ByteArray>() {
			override val codec: GenericType<ByteArray> = GenericType.of(ByteArray::class.java)
			override val type = "blob"
			override fun encode(value: ByteArray) = value.decodeToString()
		}

		object Boolean : StatelessType<kotlin.Boolean>() {
			override val codec: GenericType<kotlin.Boolean> get() = GenericType.BOOLEAN
			override val type = "boolean"
			override fun encode(value: kotlin.Boolean) = value.toString()
		}

		object Inet : Type<String> by Text {
			override val type = "inet"
			override fun toString() = defaultToString()
		}

		object UUID : StatelessType<java.util.UUID>() {
			override val codec: GenericType<java.util.UUID> get() = GenericType.UUID
			override val type = "uuid"
			override fun encode(value: java.util.UUID) = value.toString()
		}
	}

	object Number {
		//region Integers

		object Byte : StatelessType<kotlin.Byte>() {
			override val codec: GenericType<kotlin.Byte> get() = GenericType.BYTE
			override val type = "tinyint"
			override fun encode(value: kotlin.Byte) = value.toString()
		}

		object Short : StatelessType<kotlin.Short>() {
			override val codec: GenericType<kotlin.Short> get() = GenericType.SHORT
			override val type = "smallint"
			override fun encode(value: kotlin.Short) = value.toString()
		}

		object Int : StatelessType<kotlin.Int>() {
			override val codec: GenericType<kotlin.Int> get() = GenericType.INTEGER
			override val type = "int"
			override fun encode(value: kotlin.Int) = value.toString()
		}

		object Long : StatelessType<kotlin.Long>() {
			override val codec: GenericType<kotlin.Long> get() = GenericType.LONG
			override val type = "bigint"
			override fun encode(value: kotlin.Long) = value.toString()
		}

		object Counter : Type<kotlin.Long> by Long {
			override val type = "counter"
			override fun toString() = defaultToString()
		}

		object BigInteger : StatelessType<java.math.BigInteger>() {
			override val codec: GenericType<java.math.BigInteger> get() = GenericType.BIG_INTEGER
			override val type = "varint"
			override fun encode(value: java.math.BigInteger) = value.toString()
		}

		//endregion
		//region Floating-point

		object Float : StatelessType<kotlin.Float>() {
			override val codec: GenericType<kotlin.Float> get() = GenericType.FLOAT
			override val type = "float"
			override fun encode(value: kotlin.Float) = value.toString()
		}

		object Double : StatelessType<kotlin.Double>() {
			override val codec: GenericType<kotlin.Double> get() = GenericType.DOUBLE
			override val type = "double"
			override fun encode(value: kotlin.Double) = value.toString()
		}

		object BigDecimal : StatelessType<java.math.BigDecimal>() {
			override val codec: GenericType<java.math.BigDecimal> get() = GenericType.BIG_DECIMAL
			override val type = "decimal"
			override fun encode(value: java.math.BigDecimal) = value.toString()
		}

		//endregion
	}

	object Dates {
		object Timestamp : StatelessType<Instant>() {
			override val codec: GenericType<Instant> get() = GenericType.INSTANT
			override val type = "timestamp"
			override fun encode(value: Instant) = value.toString()
		}

		object Date : StatelessType<Instant>() {
			override val codec: GenericType<Instant> get() = GenericType.INSTANT
			override val type = "date"
			override fun encode(value: Instant) = value.epochSecond.toString()
		}

		object Time : StatelessType<LocalTime>() {
			override val codec: GenericType<LocalTime> get() = GenericType.LOCAL_TIME
			override val type = "time"
			override fun encode(value: LocalTime) = value.toString()
		}

		object Duration : StatelessType<java.time.Duration>() {
			override val codec: GenericType<java.time.Duration> = GenericType.DURATION
			override val type = "duration"
			override fun encode(value: java.time.Duration) = value.toString()
		}
	}

	object Collections {

		/**
		 * An implementation of a nullable type.
		 *
		 * @see orNull
		 */
		class Nullable<T : Any>(val contents: Type<T>) : Type<T?> {
			override val codec: GenericType<out T>
				get() = contents.codec // In Cassandra, all types are nullable anyway

			override val type: String
				get() = contents.type // In Cassandra, all types are nullable anyway

			override fun encode(value: T?) =
				if (value != null) contents.encode(value)
				else "null"
		}

	}

	companion object {
		fun fromCqlName(string: String): Type<*> {
			val simpleTypes = sequenceOf(
				Binary.Text, Binary.TextASCII, Binary.Binary, Binary.Boolean, Binary.Inet, Binary.UUID,
				Number.Byte, Number.Short, Number.Int, Number.Long, Number.BigInteger,
				Number.Float, Number.Double, Number.BigDecimal,
				Number.Counter,
				Dates.Timestamp, Dates.Date, Dates.Time, Dates.Duration,
			)

			val simpleResult = simpleTypes.find { it.type == string }
			if (simpleResult != null)
				return simpleResult
			else error("Type '$string' could not be interpreted.")
		}

		fun <T : Any> Type<T>.orNull() = Collections.Nullable(this)
	}
}
