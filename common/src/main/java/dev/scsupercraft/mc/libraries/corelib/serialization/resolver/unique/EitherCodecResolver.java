package dev.scsupercraft.mc.libraries.corelib.serialization.resolver.unique;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.codecs.EitherCodec;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecHelper;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialization.GenericClass;
import net.minecraft.network.codec.PacketCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * A codec resolver for {@link Either}s.
 */
public final class EitherCodecResolver implements CodecResolver {
	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return genericClass.clazz == Either.class;
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		Iterator<? extends GenericClass<?>> iterator = genericClass.typeParameterIterator();
		CodecHolder<?> holderLeft = CodecHelper.getCodec(iterator.next());
		CodecHolder<?> holderRight = CodecHelper.getCodec(iterator.next());

		return Utils.cast(new CodecHolder<>(
				new EitherCodec<>(holderLeft.codec(), holderRight.codec()),
				Utils.cast(PacketCodecs.either(Utils.cast(holderLeft.packetCodec()), Utils.cast(holderRight.packetCodec())))
		));
	}

	/**
	 * Creates a new {@link EitherCodecResolver}.
	 */
	public EitherCodecResolver() {

	}
}
