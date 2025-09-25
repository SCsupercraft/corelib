package dev.scsupercraft.mc.libraries.corelib.api.serialisation;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.intprovider.IntProvider;
import org.jetbrains.annotations.ApiStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.OptionalLong;
import java.util.UUID;

/**
 * A collection of pre-made {@link PacketCodec}s.
 */
@ApiStatus.AvailableSince("1.0.0")
public class PacketCodecs implements net.minecraft.network.codec.PacketCodecs {
	private PacketCodecs() {}

	/**
	 * A packet codec for {@code char} values.
	 * Encodes characters as single-character strings.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PacketCodec<ByteBuf, Character> CHARACTER = STRING.xmap(s -> s.charAt(0), c -> String.valueOf((char) c));

	/** A packet codec for {@link UUID} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PacketCodec<ByteBuf, UUID> UUID = Uuids.PACKET_CODEC;

	/** A packet codec for {@link IntProvider} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PacketCodec<ByteBuf, IntProvider> INT_PROVIDER = net.minecraft.network.codec.PacketCodecs.codec(IntProvider.VALUE_CODEC);

	/** A packet codec for {@link BigInteger} values, encoded as strings. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PacketCodec<ByteBuf, BigInteger> BIG_INTEGER = net.minecraft.network.codec.PacketCodecs.STRING.xmap(BigInteger::new, BigInteger::toString);

	/** A packet codec for {@link BigDecimal} values, encoded as strings. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PacketCodec<ByteBuf, BigDecimal> BIG_DECIMAL = net.minecraft.network.codec.PacketCodecs.STRING.xmap(BigDecimal::new, BigDecimal::toString);

	/**
	 * A packet codec for {@link OptionalLong} values.
	 * Encodes presence as a boolean followed by a long if present.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static final PacketCodec<ByteBuf, OptionalLong> OPTIONAL_LONG =
			PacketCodec.of(
					(value, buf) -> {
						buf.writeBoolean(value.isPresent());
						if (value.isPresent()) buf.writeLong(value.getAsLong());
					},
					buf -> {
						boolean present = buf.readBoolean();
						return present ? OptionalLong.of(buf.readLong()) : OptionalLong.empty();
					}
			);

	/**
	 * A packet codec for {@link RegistryKey} values.
	 * Encodes registry keys as {@link Identifier}s.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	@SuppressWarnings("rawtypes")
	public static final PacketCodec<ByteBuf, RegistryKey> REGISTRY_KEY = Identifier.PACKET_CODEC.xmap(RegistryKey::ofRegistry, RegistryKey::getValue);

	/**
	 * A packet codec for {@link TagKey} values.
	 * Encodes both registry reference and tag ID.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final PacketCodec<ByteBuf, TagKey> TAG_KEY = PacketCodec.tuple(
			REGISTRY_KEY,
			TagKey::registryRef,
			Identifier.PACKET_CODEC,
			TagKey::id,
			TagKey::of
	);
}
