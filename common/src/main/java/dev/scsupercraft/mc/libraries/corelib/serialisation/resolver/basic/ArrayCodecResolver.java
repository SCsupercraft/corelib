package dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.basic;

import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHelper;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialisation.GenericClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A codec resolver for generic arrays like {@code String[]} or {@code ItemStack[]}.
 */
public final class ArrayCodecResolver implements CodecResolver {
	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return genericClass.array;
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		CodecHolder<T> holder = Utils.cast(CodecHelper.getCodec(genericClass.parent));
		return Utils.cast(CodecHolder.array(holder));
	}

	private <T> List<T> newList(int initialCapacity) {
		return new ArrayList<>(initialCapacity);
	}

	/**
	 * Creates a new {@link ArrayCodecResolver}.
	 */
	public ArrayCodecResolver() {

	}
}
