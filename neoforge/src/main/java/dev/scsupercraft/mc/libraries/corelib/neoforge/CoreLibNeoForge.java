package dev.scsupercraft.mc.libraries.corelib.neoforge;

import dev.scsupercraft.mc.libraries.corelib.CoreLib;
import net.neoforged.fml.common.Mod;

/**
 * The main entrypoint for CoreLib on NeoForge.
 */
@Mod(CoreLib.MOD_ID)
public final class CoreLibNeoForge {
    /**
     * Initialises CoreLib on NeoForge.
     */
    public CoreLibNeoForge() {
        CoreLib.init();
    }
}
