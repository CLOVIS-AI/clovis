package clovis.money.database

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.Mapper

@Mapper
internal interface DbMoneyMapper {

	@DaoFactory
	fun denominations(): DbDenominationDao

}
