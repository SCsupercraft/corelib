package dev.scsupercraft.mc.libraries.corelib.api.serialization;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.intprovider.IntProvider;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.OptionalLong;
import java.util.UUID;

/**
 * A collection of pre-made {@link CodecHolder}s.
 */
@ApiStatus.AvailableSince("1.0.0")
public class CodecHolders {
	private CodecHolders() {}

	/** A codec holder for {@code byte} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Byte> BYTE = CodecHolder.of(Codecs.BYTE, PacketCodecs.BYTE);

	/** A codec holder for {@code short} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Short> SHORT = CodecHolder.of(Codecs.SHORT, PacketCodecs.SHORT);

	/** A codec holder for {@code char} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Character> CHARACTER = CodecHolder.of(Codecs.CHARACTER, PacketCodecs.CHARACTER);

	/** A codec holder for {@code boolean} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Boolean> BOOLEAN = CodecHolder.of(Codecs.BOOL, PacketCodecs.BOOLEAN);

	/** A codec holder for {@link String} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<String> STRING = CodecHolder.of(Codecs.STRING, PacketCodecs.STRING);

	/** A codec holder for {@code int} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Integer> INTEGER = CodecHolder.of(Codecs.INT, PacketCodecs.INTEGER);

	/** A codec holder for {@code long} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Long> LONG = CodecHolder.of(Codecs.LONG, PacketCodecs.LONG);

	/** A codec holder for {@code float} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Float> FLOAT = CodecHolder.of(Codecs.FLOAT, PacketCodecs.FLOAT);

	/** A codec holder for {@code double} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Double> DOUBLE = CodecHolder.of(Codecs.DOUBLE, PacketCodecs.DOUBLE);

	/** A codec holder for {@link BigInteger} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<BigInteger> BIG_INTEGER = CodecHolder.of(Codecs.BIG_INTEGER, PacketCodecs.BIG_INTEGER);

	/** A codec holder for {@link BigDecimal} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<BigDecimal> BIG_DECIMAL = CodecHolder.of(Codecs.BIG_DECIMAL, PacketCodecs.BIG_DECIMAL);

	/** A codec holder for {@link OptionalLong} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<OptionalLong> OPTIONAL_LONG = CodecHolder.of(Codecs.OPTIONAL_LONG, PacketCodecs.OPTIONAL_LONG);

	/** A codec holder for {@link UUID} values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<UUID> UUID = CodecHolder.of(Codecs.UUID, PacketCodecs.UUID);

	/** A codec holder for {@link ItemStack} instances. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<ItemStack> ITEM_STACK = CodecHolder.of(ItemStack.CODEC, ItemStack.PACKET_CODEC);

	/** A codec holder for {@link BlockPos} positions. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<BlockPos> BLOCK_POS = CodecHolder.of(BlockPos.CODEC, BlockPos.PACKET_CODEC);

	/** A codec holder for {@link GlobalPos} values, combining dimension and position. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<GlobalPos> GLOBAL_POS = CodecHolder.of(GlobalPos.CODEC, GlobalPos.PACKET_CODEC);

	/** A codec holder for {@link GameProfile} instances, typically used for player identity. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<GameProfile> GAME_PROFILE = CodecHolder.of(Codecs.GAME_PROFILE, PacketCodecs.GAME_PROFILE);

	/** A codec holder for {@link Text} components, including chat and UI text. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Text> TEXT = CodecHolder.of(TextCodecs.CODEC, TextCodecs.PACKET_CODEC);

	/** A codec holder for {@link Identifier} values, used for differentiating objects from multiple mods. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Identifier> IDENTIFIER = CodecHolder.of(Identifier.CODEC, Identifier.PACKET_CODEC);

	/** A codec holder for {@link NbtCompound} values, representing structured NBT data. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<NbtCompound> NBT_COMPOUND = CodecHolder.of(NbtCompound.CODEC, PacketCodecs.NBT_COMPOUND);

	/** A codec holder for {@link NbtElement} values, representing any NBT element. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<NbtElement> NBT_ELEMENT = CodecHolder.of(Codecs.NBT_ELEMENT, PacketCodecs.NBT_ELEMENT);

	/** A codec holder for {@link RegistryKey} values, representing registry entries. */
	@ApiStatus.AvailableSince("1.0.0")
	@SuppressWarnings("rawtypes")
	public static final CodecHolder<RegistryKey> REGISTRY_KEY = CodecHolder.of(Codecs.REGISTRY_KEY, PacketCodecs.REGISTRY_KEY);

	/** A codec holder for {@link TagKey} values, representing tag references. */
	@ApiStatus.AvailableSince("1.0.0")
	@SuppressWarnings("rawtypes")
	public static final CodecHolder<TagKey> TAG_KEY = CodecHolder.of(Codecs.TAG_KEY, PacketCodecs.TAG_KEY);

	/** A codec holder for {@link Vector3f} values, representing 3D float vectors. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Vector3f> VECTOR_3F = CodecHolder.of(Codecs.VECTOR_3F, PacketCodecs.VECTOR_3F);

	/** A codec holder for {@link Quaternionf} values, representing rotation quaternions. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<Quaternionf> QUATERNION_F = CodecHolder.of(Codecs.QUATERNION_F, PacketCodecs.QUATERNION_F);

	/** A codec holder for {@link IntProvider} values, used for ranged or constant integer values. */
	@ApiStatus.AvailableSince("1.0.0")
	public static final CodecHolder<IntProvider> INT_PROVIDER = CodecHolder.of(IntProvider.VALUE_CODEC, PacketCodecs.INT_PROVIDER);
}
