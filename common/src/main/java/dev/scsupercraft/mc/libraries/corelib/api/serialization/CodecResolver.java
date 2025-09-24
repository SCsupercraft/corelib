package dev.scsupercraft.mc.libraries.corelib.api.serialization;

import dev.scsupercraft.mc.libraries.corelib.serialization.GenericClass;
import org.jetbrains.annotations.NotNull;

/**
 * A general interface that all codec resolvers implement.
 */
public interface CodecResolver {
	/**
	 * Is the generic class supported by this codec resolver?
	 * @param genericClass The generic class.
	 * @return Is the generic class supported?
	 */
	boolean supportsValue(GenericClass<?> genericClass);

	/**
	 * Resolves a codec for the provided generic class.
	 * <p>
	 * Called only if {@link CodecResolver#supportsValue(GenericClass)} returned true.
	 * @param genericClass The generic class.
	 * @return A codec holder containing the codecs for the provided generic class.
	 * @param <T> The type of class contained in the generic class.
	 */
	@NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass);
}
