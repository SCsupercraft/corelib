package dev.scsupercraft.mc.libraries.corelib.api.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Modifier<T> {
	@NotNull T modify(@NotNull T value);
}
