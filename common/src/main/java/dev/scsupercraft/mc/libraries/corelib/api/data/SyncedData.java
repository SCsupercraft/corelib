package dev.scsupercraft.mc.libraries.corelib.api.data;

import dev.scsupercraft.mc.libraries.corelib.api.util.Synchronisation;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents an object holding a piece of data that will be automatically synced to the client.
 * <p>
 * Synchronised using the utility class, {@link Synchronisation}.
 * @param <T> The type of data being synced.
 */
@ApiStatus.AvailableSince("1.0.0")
public interface SyncedData<T> extends Data<T> {
	/**
	 * Get the data synced over from the server.
	 * @return The synced data.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	T getSyncedData();

	/**
	 * Set the data synced from the server.
	 * Please don't use this, it should only be called by CoreLib during data synchronisation.
	 * @param clientData The new data
	 */
	@ApiStatus.AvailableSince("1.0.0")
	void setSyncedData(T clientData);

	/**
	 * Used the sync data to all clients.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	default void sync() {
		Synchronisation.synchronise(this);
	}

	/**
	 * Used the sync data to a client.
	 * @param client The client to sync.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	default void sync(ServerPlayerEntity client) {
		Synchronisation.synchronise(this, client);
	}

	/**
	 * Used to get the id of this object.
	 * @return The object's id.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	Identifier getSyncId();
}
