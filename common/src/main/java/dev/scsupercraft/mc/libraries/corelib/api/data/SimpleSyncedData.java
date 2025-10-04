package dev.scsupercraft.mc.libraries.corelib.api.data;

import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.util.Synchronisation;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

/**
 * An object holding a piece of data that will be automatically synced to the client.
 * <p>
 * Synchronised using the utility class, {@link Synchronisation}.
 * @param <T> The type of data being synced.
 */
@ApiStatus.AvailableSince("1.1.0")
public class SimpleSyncedData<T> extends SimpleData<T> implements SyncedData<T> {
	private final Identifier id;
	private T syncedData;

	/**
	 * Creates a new piece of data.
	 * @param defaultValue The default value.
	 * @param codecHolder  The codec holder for synchronising the data.
	 * @param id           The id of this synced data. Used to find the synced data on the client.
	 */
	@ApiStatus.AvailableSince("1.1.0")
	public SimpleSyncedData(T defaultValue, CodecHolder<T> codecHolder, Identifier id) {
		super(defaultValue);
		this.id = id;
		this.syncedData = defaultValue;
		Synchronisation.setup(this, codecHolder);
	}

	@ApiStatus.AvailableSince("1.1.0")
	@Override
	public T getSyncedData() {
		return syncedData;
	}

	@ApiStatus.AvailableSince("1.1.0")
	@Override
	public void setSyncedData(T clientData) {
		this.syncedData = clientData;
	}

	@ApiStatus.AvailableSince("1.1.0")
	@Override
	public Identifier getSyncId() {
		return id;
	}
}
