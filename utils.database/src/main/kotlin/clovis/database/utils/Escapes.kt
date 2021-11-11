package clovis.database.utils

import com.datastax.oss.driver.api.core.CqlIdentifier

/**
 * Escapes this [String] so it can be used as an identifier in CQL.
 */
internal fun String.escapeAsIdentifier(): String =
	CqlIdentifier.fromCql(this).asCql(false)

/**
 * Escapes this [String] so it can be used as a string literal in CQL.
 */
internal fun String.asStringLiteral(): String {
	val escaped = replace("'", "&apos")
	return "'$escaped'"
}

internal fun String.fromStringLiteral(): String = this
	.removeSurrounding("'")
	.replace("&apos", "'")
