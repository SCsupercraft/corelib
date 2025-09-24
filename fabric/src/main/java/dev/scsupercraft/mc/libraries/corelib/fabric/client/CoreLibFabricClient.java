package dev.scsupercraft.mc.libraries.corelib.fabric.client;

import net.fabricmc.api.ClientModInitializer;

/**
 * The client entrypoint for CoreLib on Fabric.
 */
public final class CoreLibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }

    /**
     * Creates a new CoreLib {@link ClientModInitializer}.
     */
    public CoreLibFabricClient() {}
}
