package dev.scsupercraft.mc.libraries.corelib.serialisation.codec;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.codec.PacketCodec;

/**
 * A {@link PacketCodec} implementation for encoding and decoding {@link Pair} values.
 * @param <B> The buffer type used for encoding and decoding.
 * @param <F> The type of the first element in the pair.
 * @param <S> The type of the second element in the pair.
 */
public class PairPacketCodec<B, F, S> implements PacketCodec<B, Pair<F, S>> {
	private final PacketCodec<B, F> first;
	private final PacketCodec<B, S> second;

	/**
	 * Constructs a {@link PairPacketCodec} with the given codecs for the first and second elements.
	 * @param first  The codec responsible for serialising the first element
	 * @param second The codec responsible for serialising the second element
	 */
	public PairPacketCodec(PacketCodec<B, F> first, PacketCodec<B, S> second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public Pair<F, S> decode(B buf) {
		return Pair.of(first.decode(buf), second.decode(buf));
	}

	@Override
	public void encode(B buf, Pair<F, S> value) {
		first.encode(buf, value.getFirst());
		second.encode(buf, value.getSecond());
	}
}
