package dev.scsupercraft.mc.libraries.corelib.api.data;

import dev.scsupercraft.mc.libraries.corelib.api.util.Modifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An object holding a piece of data.
 * @param <T> The type of data.
 */
@ApiStatus.AvailableSince("1.1.0")
public class SimpleData<T> implements Data<T> {
	@NotNull
	private T value;

	/**
	 * Creates a new piece of data.
	 * @param defaultValue The default value.
	 */
	@ApiStatus.AvailableSince("1.1.0")
	public SimpleData(T defaultValue) {
		this.value = Objects.requireNonNull(defaultValue);
	}

	@ApiStatus.AvailableSince("1.1.0")
	@Override
	public synchronized @NotNull T getData() {
		return value;
	}

	@ApiStatus.AvailableSince("1.1.0")
	@Override
	public synchronized void setData(@NotNull T value) {
		this.value = Objects.requireNonNull(value);
	}

	@ApiStatus.AvailableSince("1.1.0")
	@Override
	public synchronized void modifyData(Modifier<T> modifier) {
		value = Objects.requireNonNull(modifier.modify(value));
	}
}
