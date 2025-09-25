package dev.scsupercraft.mc.libraries.corelib.listener;

import com.mojang.authlib.GameProfile;
import dev.architectury.fluid.FluidStack;
import dev.scsupercraft.mc.libraries.corelib.api.event.SerializationEvent;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.*;
import dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.basic.*;
import dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.unique.EitherCodecResolver;
import dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.unique.OptionalCodecResolver;
import dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.unique.PairCodecResolver;
import dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.unique.TagKeyCodecResolver;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.intprovider.IntProvider;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.OptionalLong;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * An event listener used by CoreLib to register its own standard codecs and codec resolvers.
 */
public final class SerialisationEventListener {
	/**
	 * Called by CoreLib to register its serialization event listeners.
	 */
	public static void init() {
		SerializationEvent.REGISTER_STANDARD_CODEC_EVENT.register(SerialisationEventListener::registerStandardCodecs);
		SerializationEvent.REGISTER_CODEC_RESOLVER_EVENT.register(SerialisationEventListener::registerCodecResolvers);
	}

	private static void registerStandardCodecs(BiConsumer<Class<?>, CodecHolder<?>> registry) {
		registry.accept(         Byte.class, CodecHolders.BYTE);
		registry.accept(        Short.class, CodecHolders.SHORT);
		registry.accept(    Character.class, CodecHolders.CHARACTER);
		registry.accept(      Boolean.class, CodecHolders.BOOLEAN);
		registry.accept(       String.class, CodecHolders.STRING);
		registry.accept(      Integer.class, CodecHolders.INTEGER);
		registry.accept(         Long.class, CodecHolders.LONG);
		registry.accept(        Float.class, CodecHolders.FLOAT);
		registry.accept(       Double.class, CodecHolders.DOUBLE);
		registry.accept(   BigInteger.class, CodecHolders.BIG_INTEGER);
		registry.accept(   BigDecimal.class, CodecHolders.BIG_DECIMAL);
		registry.accept( OptionalLong.class, CodecHolders.OPTIONAL_LONG);
		registry.accept(         UUID.class, CodecHolders.UUID);
		registry.accept(    ItemStack.class, CodecHolders.ITEM_STACK);
		registry.accept(   FluidStack.class, CodecHolders.FLUID_STACK);
		registry.accept(     BlockPos.class, CodecHolders.BLOCK_POS);
		registry.accept(    GlobalPos.class, CodecHolders.GLOBAL_POS);
		registry.accept(  GameProfile.class, CodecHolders.GAME_PROFILE);
		registry.accept(         Text.class, CodecHolders.TEXT);
		registry.accept(   Identifier.class, CodecHolders.IDENTIFIER);
		registry.accept(  NbtCompound.class, CodecHolders.NBT_COMPOUND);
		registry.accept(   NbtElement.class, CodecHolders.NBT_ELEMENT);
		registry.accept(  RegistryKey.class, CodecHolders.REGISTRY_KEY);
		registry.accept(     Vector3f.class, CodecHolders.VECTOR_3F);
		registry.accept(  Quaternionf.class, CodecHolders.QUATERNION_F);
		registry.accept(  IntProvider.class, CodecHolders.INT_PROVIDER);
	}

	private static void registerCodecResolvers(Consumer<CodecResolver> registry) {
		// Basic codec resolvers
		registry.accept(new ArrayCodecResolver());
		registry.accept(new PrimitiveCodecResolver());
		registry.accept(new StandardCodecResolver());
		registry.accept(new EnumCodecResolver());
		registry.accept(new RecordCodecResolver());

		// Commonly used resolvers
		registry.accept(new OptionalCodecResolver());
		registry.accept(new EitherCodecResolver());
		registry.accept(new PairCodecResolver());

		// Unique resolvers
		registry.accept(new TagKeyCodecResolver());
	}

	private SerialisationEventListener() {

	}
}
