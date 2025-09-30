package dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.unique;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHelper;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialisation.GenericClass;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * A codec resolver for {@link Pair}s.
 * <br><br>
 * Supports the following implementations of a pair:
 * <ul>
 *     <li>{@link com.mojang.datafixers.util.Pair}</li>
 *     <li>{@link net.minecraft.util.Pair}</li>
 *     <li>{@link org.apache.commons.lang3.tuple.Pair}</li>
 * </ul>
 */
public final class PairCodecResolver implements CodecResolver {
	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return genericClass.clazz == Pair.class
				|| genericClass.clazz == net.minecraft.util.Pair.class
				|| genericClass.clazz == org.apache.commons.lang3.tuple.Pair.class;
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		return resolvePairCodec(genericClass);
	}

	public @NotNull <T, F, S> CodecHolder<T> resolvePairCodec(GenericClass<T> genericClass) {
		Iterator<? extends GenericClass<?>> iterator = genericClass.typeParameterIterator();
		CodecHolder<F> holderFirst = Utils.cast(CodecHelper.getCodec(iterator.next()));
		CodecHolder<S> holderSecond = Utils.cast(CodecHelper.getCodec(iterator.next()));

		CodecHolder<Pair<F, S>> codecHolder = CodecHolder.pair(holderFirst, holderSecond);
		CodecHolder<?> codecHolder1 = codecHolder;

		if (genericClass.clazz != Pair.class) {
			if (genericClass.clazz == net.minecraft.util.Pair.class) {
				codecHolder1 = codecHolder.xmap(
						pair -> new net.minecraft.util.Pair<>(pair.getFirst(), pair.getSecond()),
						pair -> Pair.of(pair.getLeft(), pair.getRight())
				);
			} else if (genericClass.clazz == org.apache.commons.lang3.tuple.Pair.class) {
				codecHolder1 = codecHolder.xmap(
						pair -> org.apache.commons.lang3.tuple.Pair.of(pair.getFirst(), pair.getSecond()),
						pair -> Pair.of(pair.getLeft(), pair.getRight())
				);
			}
		}

		return Utils.cast(codecHolder1);
	}

	/**
	 * Creates a new {@link PairCodecResolver}.
	 */
	public PairCodecResolver() {

	}
}
