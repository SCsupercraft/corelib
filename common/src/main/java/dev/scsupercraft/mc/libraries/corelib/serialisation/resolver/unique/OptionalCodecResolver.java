package dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.unique;

import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHelper;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialisation.GenericClass;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A codec resolver for {@link Optional}s.
 */
public final class OptionalCodecResolver implements CodecResolver {
	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return genericClass.clazz == Optional.class;
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		CodecHolder<?> holder = CodecHelper.getCodec(genericClass.typeParameterIterator().next());
		return Utils.cast(CodecHolder.optional(holder));
	}

	/**
	 * Creates a new {@link OptionalCodecResolver}.
	 */
	public OptionalCodecResolver() {

	}
}
