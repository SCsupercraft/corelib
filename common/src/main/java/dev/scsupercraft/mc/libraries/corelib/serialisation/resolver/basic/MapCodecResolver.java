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
 * A codec resolver for {@link Map}s.
 * Works for any class extending the {@link Map} interface, as long as it declares a constructor that accepts a map.
 */
public final class MapCodecResolver implements CodecResolver {
	@Override
	public int priority() {
		return 100;
	}

	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return isMap(genericClass.clazz) &&
				(findConstructor(genericClass.clazz).isPresent() || genericClass.clazz.getTypeName().equals(Map.class.getTypeName()));
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		return Utils.cast(resolveMapCodec(Utils.cast(genericClass)));
	}

	private @NotNull <K, V, C extends Map<K, V>> CodecHolder<C> resolveMapCodec(GenericClass<C> genericClass) {
		Iterator<? extends GenericClass<?>> typeParameters = genericClass.typeParameterIterator();
		GenericClass<K> key = Utils.cast(typeParameters.next());
		CodecHolder<K> keyCodec = CodecHelper.getCodec(key);
		GenericClass<V> value = Utils.cast(typeParameters.next());
		CodecHolder<V> valueCodec = CodecHelper.getCodec(value);

		if (genericClass.clazz.getTypeName().equals(Map.class.getTypeName())) return Utils.cast(CodecHolder.unmodifiableMap(keyCodec, valueCodec));

		Constructor<C> constructor = findConstructor(genericClass.clazz).orElseThrow();
		return CodecHolder.map(keyCodec, valueCodec).xmap(
				map -> {
					try {
						constructor.setAccessible(true);
						return constructor.newInstance(map);
					} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}, map -> map
		);
	}

	private <T> Optional<Constructor<T>> findConstructor(Class<T> tClass) {
		return Utils.cast(Arrays.stream(tClass.getDeclaredConstructors()).filter(this::isValidConstructor).findFirst());
	}

	private boolean isMap(Class<?> clazz) {
		if (clazz == null || clazz == Object.class) return false;
		if (clazz == Map.class || Arrays.stream(clazz.getInterfaces()).anyMatch(this::isMap)) return true;
		return isMap(clazz.getSuperclass());
	}

	private boolean isValidConstructor(Constructor<?> constructor) {
		Parameter[] parameters = constructor.getParameters();
		if (parameters.length != 1) return false;

		Parameter parameter = parameters[0];
		return parameter.getType() == Map.class;
	}

	/**
	 * Creates a new {@link MapCodecResolver}.
	 */
	public MapCodecResolver() {

	}
}
