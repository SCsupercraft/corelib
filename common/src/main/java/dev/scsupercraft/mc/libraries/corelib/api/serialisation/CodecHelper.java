package dev.scsupercraft.mc.libraries.corelib.api.serialisation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dev.architectury.platform.Platform;
import dev.scsupercraft.mc.libraries.corelib.CoreLib;
import dev.scsupercraft.mc.libraries.corelib.api.event.SerializationEvent;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialisation.GenericClass;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;
import java.util.*;

/**
 * A utility class for resolving codecs.
 * <br><br>
 * You can register your own codec resolvers using {@link SerializationEvent.RegisterCodecResolverEvent}.
 */
@ApiStatus.AvailableSince("1.0.0")
public final class CodecHelper {
	private static final Set<CodecResolver> CODEC_RESOLVERS = new LinkedHashSet<>();
	private static final LoadingCache<GenericClass<?>, CodecHolder<?>> RESOLVED_CODECS = CacheBuilder.newBuilder().build(CacheLoader.from(CodecHelper::resolveCodec));

	/**
	 * Gets a codec for the provided class.
	 * @param tClass The class to resolve a codec for.
	 * @param types An array of types for the class's type parameters.
	 * @return The resolved codec.
	 * @param <T> The type of class.
	 * @param <U> The type of the resulting codec.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static <T, U extends T> CodecHolder<U> getCodec(Class<T> tClass, Type... types) {
		return getCodec(new GenericClass<>(tClass, null, null, types));
	}

	/**
	 * A more advanced version of {@link #getCodec(Class, Type...)}
	 * <p>
	 * Use with caution.
	 * @param genericClass The generic class to resolve a codec for.
	 * @return The resolved codec.
	 * @param <T> The type of class held in the generic class.
	 * @param <U> The type of the resulting codec.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static <T, U extends T> CodecHolder<U> getCodec(GenericClass<T> genericClass) {
		if (Platform.isDevelopmentEnvironment()) CoreLib.LOGGER.info("Finding codec for class {}", genericClass);
		return Utils.cast(RESOLVED_CODECS.getUnchecked(genericClass));
	}

	private static <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		for (CodecResolver resolver : getResolvers()) {
			if (resolver.supportsValue(genericClass)) {
				if (Platform.isDevelopmentEnvironment()) CoreLib.LOGGER.info("Using {} to resolve codec for class {}", resolver.getClass().getSimpleName(), genericClass);
				return Utils.cast(resolver.resolveCodec(genericClass));
			}
		}
		throw new RuntimeException("Failed to resolve codec for class " + genericClass + ", no codec resolvers support the provided type");
	}

	/**
	 * Gets all codec resolvers registered with the {@link SerializationEvent.RegisterCodecResolverEvent}.
	 * @return The codec resolvers.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static Set<CodecResolver> getResolvers() {
		return Collections.unmodifiableSet(CODEC_RESOLVERS);
	}

	/**
	 * Triggers the {@link SerializationEvent.RegisterCodecResolverEvent}.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static void refreshCodecResolvers() {
		synchronized (CODEC_RESOLVERS) {
			Set<CodecResolver> unsorted = new HashSet<>();
			SerializationEvent.REGISTER_CODEC_RESOLVER_EVENT.invoker().register(unsorted::add);
			CODEC_RESOLVERS.clear();
			unsorted.stream()
					.sorted(Comparator.comparingInt(CodecResolver::priority).reversed())
					.forEach(CODEC_RESOLVERS::add);
		}
	}

	private CodecHelper() {

	}
}
