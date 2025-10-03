package dev.scsupercraft.mc.libraries.corelib.fabric;

import com.mojang.serialization.Codec;
import dev.scsupercraft.mc.libraries.corelib.CoreLib;
import dev.scsupercraft.mc.libraries.corelib.api.util.DataPackRegistrar;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * The Fabric implementation of {@link DataPackRegistrar}.
 */
public class FabricDataPackRegistrar implements DataPackRegistrar {
	private static final Set<RegistryKey<? extends Registry<?>>> KEY_SET = new HashSet<>();

	FabricDataPackRegistrar() {}

	@Override
	public <T> void addRegistry(@NotNull RegistryKey<Registry<T>> registryKey, @NotNull Codec<T> codec) {
		if (KEY_SET.contains(registryKey)) return;
		CoreLib.LOGGER.info("Added data pack registry with id: {}", registryKey.getValue());
		DynamicRegistries.register(registryKey, codec);
		KEY_SET.add(registryKey);
	}

	@Override
	public <T> void addSyncedRegistry(@NotNull RegistryKey<Registry<T>> registryKey, @NotNull Codec<T> codec) {
		if (KEY_SET.contains(registryKey)) return;
		CoreLib.LOGGER.info("Added data pack registry with id: {}", registryKey.getValue());
		DynamicRegistries.registerSynced(registryKey, codec);
		KEY_SET.add(registryKey);
	}

	@Override
	public <T> void addSyncedRegistry(@NotNull RegistryKey<Registry<T>> registryKey, @NotNull Codec<T> codec, @NotNull Codec<T> networkCodec) {
		if (KEY_SET.contains(registryKey)) return;
		CoreLib.LOGGER.info("Added data pack registry with id: {}", registryKey.getValue());
		DynamicRegistries.registerSynced(registryKey, codec, networkCodec);
		KEY_SET.add(registryKey);
	}
}
