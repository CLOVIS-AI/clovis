package clovis.money.database

import clovis.core.Id
import java.util.*

internal const val MoneyKeyspace = "clovis_money"

data class DatabaseId<O : Any>(val uuid: UUID) : Id<O>
