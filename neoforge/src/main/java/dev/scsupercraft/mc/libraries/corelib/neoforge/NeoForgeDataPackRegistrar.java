package dev.scsupercraft.mc.libraries.corelib.neoforge;

import com.mojang.serialization.Codec;
import dev.scsupercraft.mc.libraries.corelib.CoreLib;
import dev.scsupercraft.mc.libraries.corelib.api.util.DataPackRegistrar;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The NeoForge implementation of {@link DataPackRegistrar}.
 */
public class NeoForgeDataPackRegistrar implements DataPackRegistrar {
	private final DataPackRegistryEvent.NewRegistry event;

	NeoForgeDataPackRegistrar(DataPackRegistryEvent.NewRegistry event) {
		this.event = event;
	}

	@Override
	public <T> void addRegistry(@NotNull RegistryKey<Registry<T>> registryKey, @NotNull Codec<T> codec) {
		CoreLib.LOGGER.info("Added data pack registry with id: {}", registryKey.getValue());
		event.dataPackRegistry(registryKey, codec);
	}

	@Override
	public <T> void addSyncedRegistry(@NotNull RegistryKey<Registry<T>> registryKey, @NotNull Codec<T> codec) {
		addSyncedRegistry(registryKey, codec, codec);
	}

	@Override
	public <T> void addSyncedRegistry(@NotNull RegistryKey<Registry<T>> registryKey, @NotNull Codec<T> codec, @NotNull Codec<T> networkCodec) {
		CoreLib.LOGGER.info("Added data pack registry with id: {}", registryKey.getValue());
		event.dataPackRegistry(registryKey, codec, networkCodec);
	}
}
