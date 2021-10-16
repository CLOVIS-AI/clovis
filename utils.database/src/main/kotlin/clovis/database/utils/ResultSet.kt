package clovis.database.utils

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.core.cql.ResultSet
import com.datastax.oss.driver.api.core.cql.Row
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Reads values from an [ResultSet] into a [Flow].
 */
internal fun ResultSet.toFlow() = flow<Row> {
	for (row in this@toFlow) {
		emit(row)
	}
}.flowOn(Dispatchers.IO)

/**
 * Reads values from an [AsyncResultSet] into a [Flow].
 */
internal fun AsyncResultSet.toFlow() = flow<Row> {
	for (row in currentPage()) {
		emit(row)
	}

	while (hasMorePages()) {
		fetchNextPage()

		for (row in currentPage()) {
			emit(row)
		}
	}
}.flowOn(Dispatchers.IO)
