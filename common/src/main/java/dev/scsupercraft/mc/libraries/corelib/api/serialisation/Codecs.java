package dev.scsupercraft.mc.libraries.corelib.api.serialisation;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import org.jetbrains.annotations.ApiStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.OptionalLong;
import java.util.UUID;

/**
 * A collection of pre-made {@link Codec}s.
 */
@ApiStatus.AvailableSince("1.0.0")
public class Codecs extends net.minecraft.util.dynamic.Codecs {
	private Codecs() {}

	/** A primitive codec for {@code boolean} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PrimitiveCodec<Boolean> BOOL = Codec.BOOL;

	/** A primitive codec for {@code byte} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PrimitiveCodec<Byte> BYTE = Codec.BYTE;

	/** A primitive codec for {@code short} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PrimitiveCodec<Short> SHORT = Codec.SHORT;

	/** A primitive codec for {@code int} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PrimitiveCodec<Integer> INT = Codec.INT;

	/** A primitive codec for {@code long} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PrimitiveCodec<Long> LONG = Codec.LONG;

	/** A primitive codec for {@code float} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PrimitiveCodec<Float> FLOAT = Codec.FLOAT;

	/** A primitive codec for {@code double} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PrimitiveCodec<Double> DOUBLE = Codec.DOUBLE;

	/** A primitive codec for {@link String} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PrimitiveCodec<String> STRING = Codec.STRING;

	/**
	 * A primitive codec for {@code char} values.
	 * Encodes characters as single-character strings.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PrimitiveCodec<Character> CHARACTER = new PrimitiveCodec<>() {
		@Override
		public <T> DataResult<Character> read(final DynamicOps<T> ops, final T input) {
			return ops.getStringValue(input).map(s -> s.charAt(0));
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final Character value) {
			return ops.createString(String.valueOf((char) value));
		}

		@Override
		public String toString() {
			return "Character";
		}
	};

	/** A codec for {@link UUID} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final Codec<UUID> UUID = Uuids.CODEC;

	/** A codec for {@link BigInteger} values, encoded as strings. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final Codec<BigInteger> BIG_INTEGER = Codec.STRING.xmap(BigInteger::new, BigInteger::toString);

	/** A codec for {@link BigDecimal} values, encoded as strings. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final Codec<BigDecimal> BIG_DECIMAL = Codec.STRING.xmap(BigDecimal::new, BigDecimal::toString);

	/**
	 * A codec for {@link GameProfile} values.
	 * Encodes both {@code id} and {@code name} fields.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static final Codec<GameProfile> GAME_PROFILE = RecordCodecBuilder.create(instance -> instance.group(
			Uuids.CODEC.fieldOf("id").forGetter(GameProfile::getId),
			Codec.STRING.fieldOf("name").forGetter(GameProfile::getName)
	).apply(instance, GameProfile::new));

	/**
	 * A codec for {@link OptionalLong} values.
	 * Encodes present values as {@code long}, otherwise emits nothing.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static final Codec<OptionalLong> OPTIONAL_LONG = new Codec<>() {
		@Override
		public <T> DataResult<Pair<OptionalLong, T>> decode(DynamicOps<T> ops, T input) {
			DataResult<Pair<Long, T>> longResult = Codec.LONG.decode(ops, input);
			return longResult.result()
					.<DataResult<Pair<OptionalLong, T>>>map(pair ->
							DataResult.success(Pair.of(OptionalLong.of(pair.getFirst()), pair.getSecond()))
					).orElse(DataResult.success(Pair.of(OptionalLong.empty(), input)));
		}

		@Override
		public <T> DataResult<T> encode(OptionalLong input, DynamicOps<T> ops, T prefix) {
			return input.isPresent()
					? Codec.LONG.encode(input.getAsLong(), ops, prefix)
					: DataResult.success(prefix);
		}
	};

	/**
	 * A codec for {@link RegistryKey} values.
	 * Encodes registry keys as {@link Identifier}s.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	@SuppressWarnings("rawtypes")
	public static final Codec<RegistryKey> REGISTRY_KEY = Identifier.CODEC.xmap(RegistryKey::ofRegistry, RegistryKey::getValue);

	/**
	 * A codec for {@link TagKey} values.
	 * Encodes both registry reference and tag ID.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Codec<TagKey> TAG_KEY = RecordCodecBuilder.create(instance -> instance.group(
			REGISTRY_KEY.fieldOf("registryRef").forGetter(TagKey::registryRef),
			Identifier.CODEC.fieldOf("id").forGetter(TagKey::id)
	).apply(instance, TagKey::of));
}
