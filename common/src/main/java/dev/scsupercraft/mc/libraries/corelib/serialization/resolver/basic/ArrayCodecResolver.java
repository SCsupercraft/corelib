package dev.scsupercraft.mc.libraries.corelib.serialization.resolver.basic;

import com.mojang.serialization.Codec;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecHelper;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialization.GenericClass;
import net.minecraft.network.codec.PacketCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A codec resolver for generic arrays like {@code String[]} or {@code ItemStack[]}.
 */
public final class ArrayCodecResolver implements CodecResolver {
	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return genericClass.array;
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		CodecHolder<T> holder = Utils.cast(CodecHelper.getCodec(genericClass.parent));
		return Utils.cast(new CodecHolder<T[]>(
				Codec.list(holder.codec()).xmap(list -> Utils.cast(list.toArray()), List::of),
				PacketCodecs.collection(this::newList, Utils.cast(holder.packetCodec())).xmap(list -> Utils.cast(list.toArray()), List::of)
		));
	}

	private <T> List<T> newList(int initialCapacity) {
		return new ArrayList<>(initialCapacity);
	}

	/**
	 * Creates a new {@link ArrayCodecResolver}.
	 */
	public ArrayCodecResolver() {

	}
}
