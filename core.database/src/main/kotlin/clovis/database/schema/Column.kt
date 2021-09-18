package clovis.database.schema

interface Column<T : Any?> {
	val name: String
	val type: Type<T>
}

//region Implementations

data class RegularColumn<T : Any?>(
	override val name: String,
	override val type: Type<T>,
) : Column<T> {

	override fun toString() = "Column('$name' $type)"
}

data class PartitionKey<T : Any?>(
	val details: RegularColumn<T>,
) : Column<T> by details {

	override fun toString() = "Partition key $details"
}

data class ClusteringKey<T : Any?>(
	val details: RegularColumn<T>,
) : Column<T> by details {

	override fun toString() = "Clustering key $details"
}

//endregion
//region DSL

inline fun <E, reified T : Type<E>> column(name: String, coder: T) = RegularColumn(name, coder)
fun <T> RegularColumn<T>.partitionKey() = PartitionKey(this)
fun <T> RegularColumn<T>.clusteringKey() = ClusteringKey(this)

//endregion
