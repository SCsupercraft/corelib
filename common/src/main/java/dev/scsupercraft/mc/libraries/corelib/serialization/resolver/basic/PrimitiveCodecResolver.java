package dev.scsupercraft.mc.libraries.corelib.serialization.resolver.basic;

import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialization.GenericClass;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A codec resolver for primitive types.
 * <p>
 * Asks the {@link StandardCodecResolver} for the codec to the boxed version of the primitive value.
 */
public final class PrimitiveCodecResolver implements CodecResolver {
	private static final Map<Class<?>, Class<?>> MAP = Map.of(
			int.class, Integer.class,
			long.class, Long.class,
			float.class, Float.class,
			double.class, Double.class,
			boolean.class, Boolean.class,
			byte.class, Byte.class,
			short.class, Short.class,
			char.class, Character.class
	);

	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return MAP.containsKey(genericClass.clazz);
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		return Utils.cast(StandardCodecResolver.get(MAP.get(genericClass.clazz)));
	}

	/**
	 * Creates a new {@link PrimitiveCodecResolver}.
	 */
	public PrimitiveCodecResolver() {

	}
}
