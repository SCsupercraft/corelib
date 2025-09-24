package dev.scsupercraft.mc.libraries.corelib.serialization.resolver.basic;

import dev.scsupercraft.mc.libraries.corelib.api.event.SerializationEvent;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialization.GenericClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to resolve codecs for class that don't have type parameters.
 * <br><br>
 * You can register your own standard codecs using {@link SerializationEvent.RegisterStandardCodecEvent}
 */
public final class StandardCodecResolver implements CodecResolver {
	private static final Map<Class<?>, CodecHolder<?>> STANDARD_CODECS = new HashMap<>();

	static {
		SerializationEvent.REGISTER_STANDARD_CODEC_EVENT.invoker().register(STANDARD_CODECS::putIfAbsent);
	}

	/**
	 * Triggers the {@link SerializationEvent.RegisterStandardCodecEvent}.
	 */
	public static void refreshCodecs() {
		SerializationEvent.REGISTER_STANDARD_CODEC_EVENT.invoker().register(STANDARD_CODECS::putIfAbsent);
	}

	/**
	 * Gets a codec holder for the provided class that was previously registered with {@link SerializationEvent.RegisterStandardCodecEvent}.
	 * @param tClass The class to get a codec holder for.
	 * @return The codec holder, or null if there was no registered codec holder.
	 * @param <T> The type of class.
	 */
	public static <T> CodecHolder<T> get(Class<T> tClass) {
		return Utils.cast(STANDARD_CODECS.get(tClass));
	}

	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return STANDARD_CODECS.containsKey(genericClass.clazz);
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		return Utils.cast(get(genericClass.clazz));
	}

	/**
	 * Creates a new {@link StandardCodecResolver}.
	 */
	public StandardCodecResolver() {

	}
}
