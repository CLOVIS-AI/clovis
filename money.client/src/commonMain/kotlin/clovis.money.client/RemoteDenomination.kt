package clovis.money.client

import clovis.client.Client
import clovis.client.RemoteProvider
import clovis.client.get
import clovis.client.put
import clovis.core.Progress
import clovis.core.Provider
import clovis.core.Ref
import clovis.core.cache.Cache
import clovis.money.Denomination
import clovis.money.DenominationCreator
import clovis.money.DenominationProvider
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
data class RemoteDenomination(
	val name: String,
	val symbol: String,
	val symbolBeforeValue: Boolean,
)

data class RemoteDenominationRef(
	internal val id: String,
	override val provider: Provider<RemoteDenominationRef, Denomination>,
) : Ref<RemoteDenominationRef, Denomination>

class RemoteDenominationProvider(
	override val cache: Cache<RemoteDenominationRef, Denomination>,
	client: Client.Authenticated,
	providerId: String,
	supportedFeatures: Set<String>,
) : RemoteProvider<RemoteDenominationRef, Denomination>(client, providerId, "money.denomination"),
    DenominationProvider<RemoteDenominationRef> {

	override fun directRequest(ref: RemoteDenominationRef): Flow<Progress<RemoteDenominationRef, Denomination>> = flow {
		emit(Progress.Loading(ref, lastKnownValue = null))

		val obj = client.get<RemoteDenomination>("$remoteEndpoint/get") {
			parameter("id", ref.id)
		}

		emit(Progress.Success(ref, Denomination(
			name = obj.name,
			symbol = obj.symbol,
			symbolBeforeValue = obj.symbolBeforeValue,
		)))
	}

	override val creator: DenominationCreator<RemoteDenominationRef>? =
		if (DenominationProvider.CREATOR_FEATURE !in supportedFeatures) null
		else object : DenominationCreator<RemoteDenominationRef> {
			override suspend fun create(
				name: String,
				symbol: String,
				symbolBeforeValue: Boolean,
			): RemoteDenominationRef {
				val id = client.put<String>("$remoteEndpoint/create", body = RemoteDenomination(
					name, symbol, symbolBeforeValue
				))

				return RemoteDenominationRef(id, this@RemoteDenominationProvider)
			}
		}

}
