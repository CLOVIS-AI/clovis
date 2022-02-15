package clovis.core

/**
 * A backend or API that can be used to interact with a specific service.
 *
 * Each service possess multiple [providers].
 */
abstract class Service(

	/**
	 * The [Clovis] instance this [Service] is instantiated for.
	 */
	val clovis: Clovis,

) {

	/**
	 * The different [Provider] implementations available via this [Service].
	 *
	 * This value should be a constant.
	 * The [Set] should be immutable.
	 * If it is needed to edit the list of providers in any way, this [Service] should be [stopped][stop], and a copy with the new set should be [started][start].
	 */
	abstract val providers: Set<Provider<*>>

	//region Registration

	/**
	 * Is this service currently running in the [clovis] instance?
	 *
	 * `true` if the service is running, `false` if the service is stopped.
	 */
	val running
		get() = this in clovis.services.value

	/**
	 * Starts this service in [clovis].
	 *
	 * Despite being `suspend`, this method is not meant for long-running operations.
	 * Short suspension is allowed (for example acquiring a lock).
	 * Long-running operations (for example authenticating a user via a network call) should be done before instance creation, for example in a factory method.
	 *
	 * Starting a service that was never started previously (a new service instance) may never fail.
	 * If failure during creation is necessary, it should happen in a factory method before the [Service] instance is created, not when this method is called.
	 *
	 * Starting a running service may either (depending on the implementation):
	 * - do nothing,
	 * - restart the service.
	 *
	 * Starting a service that was previously [stopped][stop] may either (depending on the implementation):
	 * - start it again normally,
	 * - throw [UnsupportedOperationException] if starting it again is not supported.
	 *
	 * @see stop
	 * @see running
	 */
	open suspend fun start() = clovis.register(this)

	/**
	 * Stops this service in [clovis].
	 *
	 * This method follows the same suspension and failure contract than [start].
	 *
	 * Additionally, stopping a service that is not running (never started, or already stopped) should do nothing.
	 *
	 * @see start
	 * @see running
	 */
	open suspend fun stop() = clovis.unregister(this)

	//endregion

}
