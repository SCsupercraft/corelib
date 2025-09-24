package dev.scsupercraft.mc.libraries.corelib.api.data;

import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.util.Synchronisation;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Supplier;

/**
 * A helper class for save data.
 * Automatically saves and loads data.
 * Additionally, this class automatically syncs data to client using the utility class, {@link Synchronisation}.
 * @param <T> The type of the data being saved.
 */
@ApiStatus.AvailableSince("1.0.0")
public final class SyncedAutoSaveData<T> extends AutoSaveData<T> implements SyncedData<T> {
	private final Identifier id;
	private T syncedData;

	/**
	 * Creates new synced auto save data.
	 * @param defaultValue A supplier that returns the default value for this savable. Used if there isn't any existing data during loading.
	 * @param codecHolder A codec holder for serializing the save data.
	 * @param fileGetter A supplier that returns the file that the data will be saved to.
	 * @param type What should the data be saved as.
	 * @param id The id of this synced object. Used to find the synced object on the client.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public SyncedAutoSaveData(Supplier<@NotNull T> defaultValue, CodecHolder<T> codecHolder, Supplier<File> fileGetter, Type type, Identifier id) {
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
