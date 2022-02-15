package clovis.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

/**
 * The entrypoint to the CLOVIS project.
 *
 * CLOVIS is split into [services] that themselves contain [providers][Provider].
 * Modules represent different APIs or backends that can be used to perform different operations.
 * Providers are implementations of operations on a specific type.
 *
 * For example, it is possible to instantiate a CLOVIS object which contains
 * a `GoogleCalendar` and a `LocalCalendar` modules, with each implement providers that relate to handling events.
 * Common settings, like authentication, can therefore be set in the [Service] implementation instead of once per [Provider].
 * It is also possible to [unregister] a full module at once (instead of one provider at a time).
 */
class Clovis {

	/**
	 * Internal storage of services.
	 *
	 * Reading the value is thread-safe at any point (see [MutableStateFlow.value]).
	 *
	 * Although [MutableStateFlow] guarantees thread-safe writes, because the [Set] must be copied on every write, it is still possible for writes to be lost.
	 * To ensure this doesn't happen, all writes must first acquire a [lock][servicesLock].
	 */
	private val _services = MutableStateFlow(emptySet<Service>())
	private val servicesLock = Semaphore(1)

	//region Service API

	/**
	 * The currently registered services.
	 *
	 * @see Service
	 * @see register
	 * @see unregister
	 */
	val services: StateFlow<Set<Service>>
		get() = _services.asStateFlow()

	/**
	 * Registers a new [service].
	 *
	 * This method suspends because it needs to acquire a lock on the internal set of services to prevent concurrent modification.
	 *
	 * @see unregister
	 * @see services
	 */
	internal suspend fun register(service: Service) = servicesLock.withPermit {
		_services.value = _services.value + service
	}

	/**
	 * Unregisters a [service].
	 *
	 * This method suspends because it needs to acquire a lock on the internal set of services to prevent concurrent modification.
	 *
	 * @see services
	 * @see register
	 */
	internal suspend fun unregister(service: Service) = servicesLock.withPermit {
		_services.value = _services.value - service
	}

	//endregion
	//region Provider API

	/**
	 * Queries the list of [registered/started][Service.start] services.
	 *
	 * @see services
	 * @see Service.providers
	 */
	val providers
		get() = _services.value
			.asSequence()
			.flatMap { it.providers }

	//endregion

}
