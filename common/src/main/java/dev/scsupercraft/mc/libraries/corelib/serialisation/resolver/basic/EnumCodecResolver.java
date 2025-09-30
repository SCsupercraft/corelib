package dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.basic;

import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialisation.GenericClass;
import org.jetbrains.annotations.NotNull;

/**
 * A codec resolver for {@link Enum}s.
 */
public final class EnumCodecResolver implements CodecResolver {
	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return genericClass.clazz.isEnum();
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		return Utils.cast(CodecHolder.enumeration(Utils.cast(genericClass.clazz)));
	}

	/**
	 * Creates a new {@link EnumCodecResolver}.
	 */
	public EnumCodecResolver() {

	}
}
