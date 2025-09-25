package dev.scsupercraft.mc.libraries.corelib.api.data;

import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Supplier;

/**
 * A helper class for save data.
 * Automatically saves and loads data.
 * @param <T> The type of the data being saved.
 */
@ApiStatus.AvailableSince("1.0.0")
public sealed class AutoSaveData<T> extends SaveData<T> permits SyncedAutoSaveData {
	/**
	 * Creates new auto save data.
	 * @param defaultValue A supplier that returns the default value for this savable. Used if there isn't any existing data during loading.
	 * @param codecHolder A codec holder for serializing the save data.
	 * @param fileGetter A supplier that returns the file that the data will be saved to.
	 * @param type What should the data be saved as.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public AutoSaveData(Supplier<@NotNull T> defaultValue, CodecHolder<T> codecHolder, Supplier<File> fileGetter, Type type) {
		super(defaultValue, codecHolder, fileGetter, type);
		load();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		save();
	}
}
