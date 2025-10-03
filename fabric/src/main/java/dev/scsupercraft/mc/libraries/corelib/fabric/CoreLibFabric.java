package dev.scsupercraft.mc.libraries.corelib.fabric;

import dev.scsupercraft.mc.libraries.corelib.CoreLib;
import dev.scsupercraft.mc.libraries.corelib.api.event.DataPackRegistryEvent;
import dev.scsupercraft.mc.libraries.corelib.api.util.DataPackRegistrar;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

/**
 * The main entrypoint for CoreLib on Fabric.
 */
public final class CoreLibFabric implements ModInitializer {
    /**
     * Used by CoreLib for adding data pack registries.
     * <p>
     * Please use {@link DataPackRegistryEvent#NEW_REGISTRY} instead.
     */
    @ApiStatus.Internal
    public static final DataPackRegistrar DATA_PACK_REGISTRAR = new FabricDataPackRegistrar();

    @Override
    public void onInitialize() {
        CoreLib.init();
    }

    /**
     * Creates a new CoreLib {@link ModInitializer}.
     */
    public CoreLibFabric() {

    }
}
