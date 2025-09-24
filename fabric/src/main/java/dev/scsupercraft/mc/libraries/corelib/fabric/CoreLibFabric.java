package dev.scsupercraft.mc.libraries.corelib.fabric;

import dev.scsupercraft.mc.libraries.corelib.CoreLib;
import net.fabricmc.api.ModInitializer;

/**
 * The main entrypoint for CoreLib on Fabric.
 */
public final class CoreLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CoreLib.init();
    }

    /**
     * Creates a new CoreLib {@link ModInitializer}.
     */
    public CoreLibFabric() {}
}
