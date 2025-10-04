package dev.scsupercraft.mc.libraries.corelib.api.data;

import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.util.Synchronisation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A helper class for save data.
 * Automatically saves and loads data on server start and stop.
 * Additionally, this class automatically syncs data to client using the utility class, {@link Synchronisation}.
 * @param <T> The type of the data being saved.
 */
@ApiStatus.AvailableSince("1.0.0")
public final class SyncedWorldSaveData<T> extends WorldSaveData<T> implements SyncedData<T> {
	private final Identifier id;
	private T syncedData;

	/**
	 * Creates new synced world save data.
	 * @param defaultValue A supplier that returns the default value for this savable. Used if there isn't any existing data during loading.
	 * @param codecHolder A codec holder for serializing the save data.
	 * @param fileGetter A function that accepts the current server and returns the file that the data will be saved to.
	 * @param type What should the data be saved as.
	 * @param id The id of this synced data. Used to find the synced data on the client.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public SyncedWorldSaveData(Supplier<@NotNull T> defaultValue, CodecHolder<T> codecHolder, Function<MinecraftServer, File> fileGetter, Type type, Identifier id) {
		super(defaultValue, codecHolder, fileGetter, type);
		this.id = id;
		this.syncedData = getData();
		Synchronisation.setup(this, codecHolder);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		sync();
	}

	@Override
	public T getSyncedData() {
		return syncedData;
	}

	@Override
	public void setSyncedData(T syncedData) {
		this.syncedData = syncedData;
	}

	@Override
	public Identifier getSyncId() {
		return id;
	}
}
