package dev.scsupercraft.mc.libraries.corelib.api.util;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * An api used for registering data pack registries.
 */
@ApiStatus.AvailableSince("1.1.0")
public interface DataPackRegistrar {
	/**
	 * Registers the given registry key as an unsynced datapack registry, which will cause data to be loaded from
	 * a datapack folder based on the registry's name. The datapack registry is not required to be present
	 * on clients when connecting to servers with the mod/registry.
	 * <p>
	 * Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
	 *
	 * @param registryKey The root registry key of the new datapack registry.
	 * @param codec       The codec to be used for loading data from datapacks on servers.
	 * @param <T>         The type of the registry.
	 * @see #addSyncedRegistry(RegistryKey, Codec)
	 */
	@ApiStatus.AvailableSince("1.1.0")
	<T> void addRegistry(@NotNull RegistryKey<Registry<T>> registryKey, @NotNull Codec<T> codec);

	/**
	 * Registers the registry key as a datapack registry, which will cause data to be loaded from
	 * a datapack folder based on the registry's name.
	 * <p>
	 * Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
	 * <p>
	 * The registry will be synced from the server to players' clients using the same codec
	 * that is used to load the registry.
	 *
	 * @param registryKey  The root registry key of the new datapack registry.
	 * @param codec        The codec to be used for loading data from datapacks on servers.
	 * @param <T>          The type of the registry.
	 * @see #addRegistry(RegistryKey, Codec)
	 */
	@ApiStatus.AvailableSince("1.1.0")
	<T> void addSyncedRegistry(@NotNull RegistryKey<Registry<T>> registryKey, @NotNull Codec<T> codec);

	/**
	 * Registers the registry key as a datapack registry, which will cause data to be loaded from
	 * a datapack folder based on the registry's name.
	 * <p>
	 * Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
	 *
	 * @param registryKey  The root registry key of the new datapack registry.
	 * @param codec        The codec to be used for loading data from datapacks on servers.
	 * @param networkCodec The codec to be used for syncing loaded data to clients.
	 * @param <T>          The type of the registry.
	 * @see #addRegistry(RegistryKey, Codec)
	 */
	@ApiStatus.AvailableSince("1.1.0")
	<T> void addSyncedRegistry(@NotNull RegistryKey<Registry<T>> registryKey, @NotNull Codec<T> codec, @NotNull Codec<T> networkCodec);
}
