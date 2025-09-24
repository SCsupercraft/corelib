package dev.scsupercraft.mc.libraries.corelib.api.data;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object holding a piece of data.
 * @param <T> The type of data.
 */
public interface Data<T> {
	/**
	 * Get the current data.
	 * @return The current data.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	@NotNull T getData();

	/**
	 * Set the current data.
	 * @param value The new data.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	void setData(@NotNull T value);
}
