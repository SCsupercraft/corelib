package dev.scsupercraft.mc.libraries.corelib.api.serialization;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

/**
 * A record containing both a codec and a packet codec.
 * @param codec A codec for serializing data.
 * @param packetCodec A packet codec for serializing data.
 * @param <T> The class that the codecs are for.
 */
@ApiStatus.AvailableSince("1.0.0")
public record CodecHolder<T>(Codec<T> codec, PacketCodec<ByteBuf, T> packetCodec) {
	/**
	 * Creates a new codec holder.
	 * @param codec A codec for serializing data.
	 * @param packetCodec A packet codec for serializing data.
	 * @return A new codec holder.
	 * @param <T> The class that the codecs are for.
	 */
	@SuppressWarnings("unchecked")
	@ApiStatus.AvailableSince("1.0.0")
	public static <T> CodecHolder<T> of(Codec<T> codec, PacketCodec<? extends ByteBuf, T> packetCodec) {
		return new CodecHolder<T>(codec, (PacketCodec<ByteBuf, T>) packetCodec);
	}

	/**
	 * Transforms this {@code CodecHolder<T>} into a {@code CodecHolder<U>} by applying mapping functions
	 * to both the {@link Codec} and the {@link PacketCodec}.
	 *
	 * <p>This is useful when you want to adapt the underlying serialization logic to a different type
	 * without creating new codecs from scratch.</p>
	 * @param to A function that maps from the original type {@code T} to the new type {@code U}.
	 * @param from A function that maps from the new type {@code U} back to the original type {@code T}.
	 * @return A new {@code CodecHolder<U>} with transformed codecs.
	 * @param <U> The target type to transform the codecs to.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public <U> CodecHolder<U> xmap(final Function<? super T, ? extends U> to, final Function<? super U, ? extends T> from) {
		return new CodecHolder<>(
				codec.xmap(to, from),
				packetCodec.xmap(to, from)
		);
	}
}
