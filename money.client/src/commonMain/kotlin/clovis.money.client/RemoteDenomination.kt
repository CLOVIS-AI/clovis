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

data class RemoteDenominationRef(
	internal val id: String,
	override val provider: Provider<Denomination>,
) : Ref<Denomination> {
	override fun encodeRef(): String = id
}

class RemoteDenominationProvider(
	override val cache: Cache<Denomination>,
	client: Client.Authenticated,
	providerId: String,
	supportedFeatures: Set<String>,
) : RemoteProvider<Denomination>(client, providerId, Denomination.PROVIDER_TYPE_ID),
    DenominationProvider {

	override fun directRequest(ref: Ref<Denomination>): Flow<Progress<Denomination>> = flow {
		require(ref is RemoteDenominationRef) { "Illegal reference type: $ref" }

		emit(Progress.Loading(ref, lastKnownValue = null))

		val obj = client.get<Denomination>("$remoteEndpoint/get") {
			parameter("id", ref.id)
		}

		emit(Progress.Success(ref, obj))
	}

	override fun decodeRef(encoded: String) = RemoteDenominationRef(encoded, this)

	override val creator: DenominationCreator? =
		if (DenominationProvider.CREATOR_FEATURE !in supportedFeatures) null
		else object : DenominationCreator {
			override suspend fun create(
				name: String,
				symbol: String,
				symbolBeforeValue: Boolean,
			): RemoteDenominationRef {
				val id = client.put<String>("$remoteEndpoint/create", body = Denomination(
					name, symbol, symbolBeforeValue
				))

				return RemoteDenominationRef(id, this@RemoteDenominationProvider)
			}
		}

}
