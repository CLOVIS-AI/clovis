package clovis.money.server

import clovis.core.request
import clovis.money.Denomination
import clovis.money.DenominationProvider
import clovis.server.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.remoteDenominations(providers: UserProviders) {
	route("/money.denomination/") {

		get("{providerId}/get") {
			val providerId = call.providerId()
			val user = call.currentUser()
			val provider = providers.findProvider(user, providerId).ensureIs<DenominationProvider>()

			val id by call.request.queryParameters
			val data = provider.decodeRef(id).request()
			call.respond(data)
		}

		put("{providerId}/create") {
			val providerId = call.providerId()
			val user = call.currentUser()
			val provider = providers.findProvider(user, providerId).ensureIs<DenominationProvider>()

			val data = call.receive<Denomination>()
			val creator = provider.creator
				?: error("The provider '$providerId' does not implement the feature '${DenominationProvider.CREATOR_FEATURE}'")
			val ref = creator.create(data.name, data.symbol, data.symbolBeforeValue)
			call.respond(ref.encodeRef())
		}

	}
}
