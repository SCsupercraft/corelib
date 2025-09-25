package dev.scsupercraft.mc.libraries.corelib.api.data;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.scsupercraft.mc.libraries.corelib.CoreLib;
import dev.scsupercraft.mc.libraries.corelib.api.event.WorldSaveDataEvent;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A helper class for save data.
 * Automatically saves and loads data on server start and stop.
 * @param <T> The type of the data being saved.
 */
@ApiStatus.AvailableSince("1.0.0")
public sealed class WorldSaveData<T> extends SaveData<T> permits SyncedWorldSaveData {
	private static final Set<WorldSaveData<?>> WORLD_SAVABLE_SET = new HashSet<>();
	private static boolean loaded = false;

	static {
		LifecycleEvent.SERVER_BEFORE_START.register(s -> {
			CoreLib.server = s;
			WorldSaveData.loadAll();
			loaded = true;
			WorldSaveDataEvent.LOAD_EVENT.invoker().onLoaded(s);
		});
		LifecycleEvent.SERVER_STOPPED.register(s -> {
			WorldSaveData.saveAll();
			CoreLib.server = null;
			loaded = false;
			WorldSaveDataEvent.SAVE_EVENT.invoker().onSaved(s);
		});
	}

	/**
	 * Creates new synced world save data.
	 * @param defaultValue A supplier that returns the default value for this savable. Used if there isn't any existing data during loading.
	 * @param codecHolder A codec holder for serializing the save data.
	 * @param fileGetter A function that accepts the current server and returns the file that the data will be saved to.
	 * @param type What should the data be saved as.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public WorldSaveData(Supplier<@NotNull T> defaultValue, CodecHolder<T> codecHolder, Function<MinecraftServer, File> fileGetter, Type type) {
		super(defaultValue, codecHolder, () -> fileGetter.apply(CoreLib.server), type);
		WORLD_SAVABLE_SET.add(this);
	}

	/**
	 * Has the {@link WorldSaveData} been loaded.
	 * @return Is it loaded?
	 */
	public static boolean isLoaded() {
		return loaded;
	}

	/**
	 * Loads all {@link WorldSaveData} from the disk.
	 * Used when a server starts.
	 */
	private static void loadAll() {
		WORLD_SAVABLE_SET.forEach(SaveData::load);
	}

	/**
	 * Saves all {@link WorldSaveData} to the disk.
	 * Used when a server has stopped.
	 */
	private static void saveAll() {
		WORLD_SAVABLE_SET.forEach(SaveData::save);
	}
}
