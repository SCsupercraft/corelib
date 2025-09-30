package dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.basic;

import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHelper;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialisation.GenericClass;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * A codec resolver for sets.
 * Works for any class extending the {@link Set} interface, as long as it declares a constructor that accepts a collection.
 * @see HashSet#HashSet(Collection)  HashSet(Collection<? extends E>)
 */
public final class SetCodecResolver implements CodecResolver {
	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return isSet(genericClass.clazz) &&
				(findConstructor(genericClass.clazz).isPresent() || genericClass.clazz.getTypeName().equals(Set.class.getTypeName()));
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		return Utils.cast(resolveSetCodec(Utils.cast(genericClass)));
	}

	private @NotNull <T, C extends Set<T>> CodecHolder<C> resolveSetCodec(GenericClass<C> genericClass) {
		GenericClass<T> element = Utils.cast(genericClass.typeParameterIterator().next());
		CodecHolder<T> elementCodec = CodecHelper.getCodec(element);

		if (genericClass.clazz.getTypeName().equals(Set.class.getTypeName())) return Utils.cast(CodecHolder.unmodifiableSet(elementCodec));

		Constructor<C> constructor = findConstructor(genericClass.clazz).orElseThrow();
		return CodecHolder.set(elementCodec).xmap(
				list -> {
					try {
						constructor.setAccessible(true);
						return constructor.newInstance(list);
					} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}, list -> list
		);
	}

	private <T> Optional<Constructor<T>> findConstructor(Class<T> tClass) {
		return Utils.cast(Arrays.stream(tClass.getDeclaredConstructors()).filter(this::isValidConstructor).findFirst());
	}

	private boolean isSet(Class<?> clazz) {
		if (clazz == null || clazz == Object.class) return false;
		if (clazz == Set.class || Arrays.stream(clazz.getInterfaces()).anyMatch(this::isSet)) return true;
		return isSet(clazz.getSuperclass());
	}

	private boolean isValidConstructor(Constructor<?> constructor) {
		Parameter[] parameters = constructor.getParameters();
		if (parameters.length != 1) return false;

		Parameter parameter = parameters[0];
		return parameter.getType() == Collection.class;
	}

	/**
	 * Creates a new {@link SetCodecResolver}.
	 */
	public SetCodecResolver() {

	}
}
