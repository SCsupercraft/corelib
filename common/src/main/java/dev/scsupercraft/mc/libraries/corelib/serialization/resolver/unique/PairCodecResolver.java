package dev.scsupercraft.mc.libraries.corelib.serialization.resolver.unique;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecHelper;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialization.GenericClass;
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
		Iterator<? extends GenericClass<?>> iterator = genericClass.typeParameterIterator();
		CodecHolder<?> holderFirst = CodecHelper.getCodec(iterator.next());
		CodecHolder<?> holderSecond = CodecHelper.getCodec(iterator.next());
		Codec<Pair<?, ?>> codec = Utils.cast(Codec.pair(holderFirst.codec().fieldOf("first").codec(), holderSecond.codec().fieldOf("second").codec()));
		PacketCodec<? extends ByteBuf, Pair<Object, Object>> packetCodec = pair(Utils.cast(holderFirst.packetCodec()), Utils.cast(holderSecond.packetCodec()));

		CodecHolder<Pair<?, ?>> codecHolder = new CodecHolder<>(
				codec,
				Utils.cast(packetCodec)
		);
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

	static <B extends ByteBuf, F, S> PacketCodec<B, Pair<F, S>> pair(final PacketCodec<? super B, F> first, final PacketCodec<? super B, S> second) {
		return new PacketCodec<>() {
			public Pair<F, S> decode(B byteBuf) {
				return new Pair<>(first.decode(byteBuf), second.decode(byteBuf));
			}

			public void encode(B byteBuf, Pair<F, S> pair) {
				first.encode(byteBuf, pair.getFirst());
				second.encode(byteBuf, pair.getSecond());
			}
		};
	}
}
