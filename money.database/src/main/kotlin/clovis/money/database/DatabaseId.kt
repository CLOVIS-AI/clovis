package clovis.money.database

import clovis.core.Id
import java.util.*

data class DatabaseId<O : Any>(val uuid: UUID) : Id<O>
