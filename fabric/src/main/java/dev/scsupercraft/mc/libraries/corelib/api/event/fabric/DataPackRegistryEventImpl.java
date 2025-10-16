package dev.scsupercraft.mc.libraries.corelib.api.event.fabric;

import dev.scsupercraft.mc.libraries.corelib.api.event.DataPackRegistryEvent;
import dev.scsupercraft.mc.libraries.corelib.fabric.CoreLibFabric;

/**
 * The Fabric implementation of {@link DataPackRegistryEvent}.
 */
public class DataPackRegistryEventImpl {
	private DataPackRegistryEventImpl() {}

	/**
	 * The Fabric implementation.
	 */
	public static void onUpdatedListeners() {
		DataPackRegistryEvent.NEW_REGISTRY.invoker().addRegistries(CoreLibFabric.DATA_PACK_REGISTRAR);
	}
}
