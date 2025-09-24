package dev.scsupercraft.mc.libraries.corelib.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.scsupercraft.mc.libraries.corelib.api.data.WorldSaveData;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contains events for loading and saving {@link WorldSaveData}.
 */
public class WorldSaveDataEvent {
	private WorldSaveDataEvent() {}

	/**
	 * Used to run code after {@link WorldSaveData} has been loaded.
	 * @see WorldSaveData
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static final Event<LoadEvent> LOAD_EVENT = EventFactory.createLoop();

	/**
	 * Used to run code after {@link WorldSaveData} has been saved.
	 * @see WorldSaveData
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static final Event<SaveEvent> SAVE_EVENT = EventFactory.createLoop();

	/**
	 * A functional interface for listeners of {@link #LOAD_EVENT}
	 */
	@FunctionalInterface
	public interface LoadEvent {
		/**
		 * Called when all {@link WorldSaveData} gets loaded.
		 * @param server The current minecraft server.
		 */
		void onLoaded(MinecraftServer server);
	}

	/**
	 * A functional interface for listeners of {@link #SAVE_EVENT}
	 */
	@FunctionalInterface
	public interface SaveEvent {
		/**
		 * Called when all {@link WorldSaveData} gets saved.
		 * @param server The current minecraft server.
		 */
		void onSaved(MinecraftServer server);
	}
}
