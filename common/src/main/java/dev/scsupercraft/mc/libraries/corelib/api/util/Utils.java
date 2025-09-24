package dev.scsupercraft.mc.libraries.corelib.api.util;

import org.jetbrains.annotations.ApiStatus;

/**
 * A utility class containing general utilities.
 */
@ApiStatus.AvailableSince("1.0.0")
public class Utils {
	/**
	 * Performs an unchecked cast.
	 * @param value The object to cast.
	 * @return The cast object.
	 * @param <T> The type to cast to.
	 */
	@SuppressWarnings("unchecked")
	@ApiStatus.AvailableSince("1.0.0")
	public static <T> T cast(Object value) {
		return (T) value;
	}

	private Utils() {}
}
