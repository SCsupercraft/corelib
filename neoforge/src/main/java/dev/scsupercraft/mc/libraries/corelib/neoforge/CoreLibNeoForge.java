package dev.scsupercraft.mc.libraries.corelib.neoforge;

import dev.scsupercraft.mc.libraries.corelib.CoreLib;
import dev.scsupercraft.mc.libraries.corelib.api.util.DataPackRegistrar;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

/**
 * The main entrypoint for CoreLib on NeoForge.
 */
@Mod(CoreLib.MOD_ID)
public final class CoreLibNeoForge {
    /**
     * Initialises CoreLib on NeoForge.
     * @param container The mod container.
     */
    public CoreLibNeoForge(ModContainer container) {
        CoreLib.init();

        container.getEventBus().addListener(this::dataPackRegistries);
    }

    private void dataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        DataPackRegistrar registrar = new NeoForgeDataPackRegistrar(event);
        dev.scsupercraft.mc.libraries.corelib.api.event.DataPackRegistryEvent.NEW_REGISTRY.invoker().addRegistries(registrar);
    }
}
