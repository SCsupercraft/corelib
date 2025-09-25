package dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.basic;

import com.mojang.serialization.Codec;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialisation.GenericClass;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.jetbrains.annotations.NotNull;

/**
 * A codec resolver for {@link Enum}s.
 */
public final class EnumCodecResolver implements CodecResolver {
	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return genericClass.clazz.isEnum();
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		return Utils.cast(fromEnum(Utils.cast(genericClass.clazz)));
	}

	private <T extends Enum<T>> CodecHolder<T> fromEnum(Class<T> tClass) {
		if (!tClass.isEnum()) throw new IllegalArgumentException("You can only create an enum codec from a enum!");
		return new CodecHolder<>(codecFromEnum(tClass), packetCodecFromEnum(tClass));
	}

	private <T extends Enum<T>> Codec<T> codecFromEnum(Class<T> tClass) {
		return Codec.STRING.xmap(string -> Enum.valueOf(tClass, string), Enum::name);
	}

	private <T extends Enum<T>> PacketCodec<ByteBuf, T> packetCodecFromEnum(Class<T> tClass) {
		return PacketCodecs.STRING.xmap(string -> Enum.valueOf(tClass, string), Enum::name);
	}

	/**
	 * Creates a new {@link EnumCodecResolver}.
	 */
	public EnumCodecResolver() {

	}
}
