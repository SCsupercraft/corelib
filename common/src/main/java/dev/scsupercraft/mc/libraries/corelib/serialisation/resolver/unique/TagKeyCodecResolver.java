package dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.unique;

import dev.scsupercraft.mc.libraries.corelib.annotations.RegistryRef;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolders;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialisation.GenericClass;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * A codec resolver for {@link TagKey}s
 * <br><br>
 * Annotate a type with {@link RegistryRef} to tell the codec resolver what registry the tag is for.
 * Not required but highly recommended.
 * <br><br>
 * If not included upon serialization, the registry ref will be serialized with the tag instead.
 * <br><br>
 * Without @RegistryRef("block") - {"data": {"registryRef": "minecraft:block", id: "minecraft:needs_iron_tool"}}
 * <br>
 * With @RegistryRef("block") - {"data": "#minecraft:needs_iron_tool"}
 */
public final class TagKeyCodecResolver implements CodecResolver {
	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return genericClass.clazz == TagKey.class;
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		RegistryRef registryRef = genericClass.annotations != null ? genericClass.annotations.getDeclaredAnnotation(RegistryRef.class) : null;
		if (registryRef == null) return Utils.cast(CodecHolders.TAG_KEY);
		RegistryKey<Registry<Object>> registryKey = RegistryKey.ofRegistry(Identifier.of(registryRef.namespace(), registryRef.value()));

		return Utils.cast(new CodecHolder<>(
				TagKey.codec(registryKey),
				TagKey.packetCodec(registryKey)
		));
	}

	/**
	 * Creates a new {@link TagKeyCodecResolver}.
	 */
	public TagKeyCodecResolver() {

	}
}
