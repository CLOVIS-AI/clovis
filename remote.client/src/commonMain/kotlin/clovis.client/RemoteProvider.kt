package clovis.client

import clovis.core.Provider
import clovis.core.Ref

/**
 * A [Provider] implementation located on the CLOVIS server, instead of being located in the current process.
 *
 * Via this class, remote [Provider] implementations can be used as if they were local.
 */
abstract class RemoteProvider<R : Ref<R, O>, O>(
	protected val client: Client.Authenticated,

	/**
	 * A value unique between all [RemoteProvider] instances, used to differentiate between multiple instances of the same type.
	 */
	id: String,

	/**
	 * A value that represents the type of object referenced by this [RemoteProvider].
	 *
	 * The simplest implementation is to use `someObject::class.qualifiedName`.
	 * The client is not expected to parse this value.
	 */
	typeId: String,
) : Provider<R, O> {

	/**
	 * The endpoint prefix so the call is received by the server-side listener.
	 */
	protected val remoteEndpoint = "/remote/$typeId/$id"

}
