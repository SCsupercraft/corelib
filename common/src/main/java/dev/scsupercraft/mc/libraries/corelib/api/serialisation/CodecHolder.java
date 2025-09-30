package dev.scsupercraft.mc.libraries.corelib.api.serialisation;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.EitherCodec;
import com.mojang.serialization.codecs.PairCodec;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialisation.codec.PairPacketCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

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
		return new CodecHolder<>(codec, (PacketCodec<ByteBuf, T>) packetCodec);
	}

	/**
	 *
	 * @param enumClass The class of the enum.
	 * @return A new codec holder.
	 * @param <T> The type of enum.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <T extends Enum<T>> CodecHolder<T> enumeration(Class<T> enumClass) {
		return of(
				Codec.STRING.xmap(string -> Enum.valueOf(enumClass, string), Enum::name),
				PacketCodecs.STRING.xmap(string -> Enum.valueOf(enumClass, string), Enum::name)
		);
	}

	/**
	 * Creates a new codec holder for an array.
	 * @param codecHolder The codec holder for serialising array elements.
	 * @return A new codec holder.
	 * @param <E> The type of element.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <E> CodecHolder<E[]> array(CodecHolder<E> codecHolder) {
		return list(codecHolder).xmap(list -> Utils.cast(list.toArray()), List::of);
	}

	/**
	 * Creates a new codec holder for a collection.
	 * @param factory     A factory for creating the collection.
	 * @param codecHolder The codec holder for serialising collection elements.
	 * @return A new codec holder.
	 * @param <E> The type of element.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <E, C extends Collection<E>> CodecHolder<C> collection(IntFunction<C> factory, CodecHolder<E> codecHolder) {
		return of(
				Codec.list(codecHolder.codec).xmap(list -> {
					C collection = factory.apply(list.size());
					collection.addAll(list);
					return collection;
				}, ArrayList::new),
				PacketCodecs.collection(factory, codecHolder.packetCodec)
		);
	}

	/**
	 * Creates a new codec holder for a list.
	 * @param codecHolder The codec holder for serialising list elements.
	 * @return A new codec holder.
	 * @param <E> The type of element.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <E> CodecHolder<List<E>> list(CodecHolder<E> codecHolder) {
		return collection(ArrayList::new, codecHolder);
	}

	/**
	 * Creates a new codec holder for a list.
	 * @param codecHolder The codec holder for serialising list elements.
	 * @return A new codec holder.
	 * @param <E> The type of element.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <E> CodecHolder<List<E>> unmodifiableList(CodecHolder<E> codecHolder) {
		return list(codecHolder).xmap(Collections::unmodifiableList, l -> l);
	}

	/**
	 * Creates a new codec holder for a set.
	 * @param codecHolder The codec holder for serialising set elements.
	 * @return A new codec holder.
	 * @param <E> The type of element.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <E> CodecHolder<Set<E>> set(CodecHolder<E> codecHolder) {
		return collection(HashSet::new, codecHolder);
	}

	/**
	 * Creates a new codec holder for a set.
	 * @param codecHolder The codec holder for serialising set elements.
	 * @return A new codec holder.
	 * @param <E> The type of element.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <E> CodecHolder<Set<E>> unmodifiableSet(CodecHolder<E> codecHolder) {
		return set(codecHolder).xmap(Collections::unmodifiableSet, s -> s);
	}

	/**
	 * Creates a new codec holder for a map.
	 * @param factory     A factory for creating the map.
	 * @param keyCodecHolder The codec holder for serialising map keys.
	 * @param valueCodecHolder The codec holder for serialising map values.
	 * @return A new codec holder.
	 * @param <K> The type of key.
	 * @param <V> The type of value.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <K, V, M extends Map<K, V>> CodecHolder<M> map(IntFunction<M> factory, CodecHolder<K> keyCodecHolder, CodecHolder<V> valueCodecHolder) {
		return of(
				Codec.unboundedMap(keyCodecHolder.codec, valueCodecHolder.codec).xmap(map -> {
					M map1 = factory.apply(map.size());
					map1.putAll(map);
					return map1;
				}, m -> m),
				PacketCodecs.map(factory, keyCodecHolder.packetCodec, valueCodecHolder.packetCodec)
		);
	}

	/**
	 * Creates a new codec holder for a map.
	 * @param keyCodecHolder The codec holder for serialising map keys.
	 * @param valueCodecHolder The codec holder for serialising map values.
	 * @return A new codec holder.
	 * @param <K> The type of key.
	 * @param <V> The type of value.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <K, V> CodecHolder<Map<K, V>> map(CodecHolder<K> keyCodecHolder, CodecHolder<V> valueCodecHolder) {
		return map(HashMap::new, keyCodecHolder, valueCodecHolder);
	}

	/**
	 * Creates a new codec holder for a map.
	 * @param keyCodecHolder The codec holder for serialising map keys.
	 * @param valueCodecHolder The codec holder for serialising map values.
	 * @return A new codec holder.
	 * @param <K> The type of key.
	 * @param <V> The type of value.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <K, V> CodecHolder<Map<K, V>> unmodifiableMap(CodecHolder<K> keyCodecHolder, CodecHolder<V> valueCodecHolder) {
		return map(keyCodecHolder, valueCodecHolder).xmap(Collections::unmodifiableMap, m -> m);
	}

	/**
	 * Creates a new codec holder for an optional value.
	 * @param codecHolder The codec holder for serialising the value if it exists.
	 * @return A new codec holder.
	 * @param <T> The type of value.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <T> CodecHolder<Optional<T>> optional(CodecHolder<T> codecHolder) {
		return of(Codecs.optional(codecHolder.codec), PacketCodecs.optional(codecHolder.packetCodec));
	}

	/**
	 * Creates a new codec holder for either value.
	 * @param leftCodecHolder The codec holder for serialising the first value.
	 * @param rightCodecHolder The codec holder for serialising the second value.
	 * @return A new codec holder.
	 * @param <L> The type of the first value.
	 * @param <R> The type of the second value.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <L, R> CodecHolder<Either<L, R>> either(CodecHolder<L> leftCodecHolder, CodecHolder<R> rightCodecHolder) {
		return of(
				new EitherCodec<>(leftCodecHolder.codec, rightCodecHolder.codec),
				PacketCodecs.either(leftCodecHolder.packetCodec, rightCodecHolder.packetCodec)
		);
	}

	/**
	 * Creates a new codec holder for either value.
	 * @param firstCodecHolder The codec holder for serialising the first value.
	 * @param secondCodecHolder The codec holder for serialising the second value.
	 * @return A new codec holder.
	 * @param <F> The type of the first value.
	 * @param <S> The type of the second value.
	 */
	@ApiStatus.AvailableSince("1.0.1")
	public static <F, S> CodecHolder<Pair<F, S>> pair(CodecHolder<F> firstCodecHolder, CodecHolder<S> secondCodecHolder) {
		return of(
				new PairCodec<>(firstCodecHolder.codec.fieldOf("first").codec(), secondCodecHolder.codec.fieldOf("second").codec()),
				new PairPacketCodec<>(firstCodecHolder.packetCodec, secondCodecHolder.packetCodec)
		);
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
