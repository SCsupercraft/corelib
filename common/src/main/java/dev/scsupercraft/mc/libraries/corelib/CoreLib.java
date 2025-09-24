package dev.scsupercraft.mc.libraries.corelib;

import com.mojang.logging.LogUtils;
import dev.scsupercraft.mc.libraries.corelib.listener.SerializationEventListener;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * The common entry point for CoreLib.
 */
public final class CoreLib {
	/**
	 * The mod id.
	 */
    public static final String MOD_ID = "corelib";
	/**
	 * The logger.
	 */
    public static final Logger LOGGER = LogUtils.getLogger();
	/**
	 * The current minecraft server, null if no server is online.
	 */
    public static @Nullable MinecraftServer server;

    static {
        SerializationEventListener.init();
    }

	private CoreLib() {}

	/**
	 * Initializes CoreLib. Should only be called by CoreLib.
	 */
    public static void init() {
		try { // Initialise classes
			Class.forName("dev.scsupercraft.mc.libraries.corelib.api.util.Synchronisation");
			Class.forName("dev.scsupercraft.mc.libraries.corelib.api.data.WorldSaveData");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
    }
}
