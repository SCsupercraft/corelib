package dev.scsupercraft.mc.libraries.corelib.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHelper;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.basic.StandardCodecResolver;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Contains events for resolving codecs.
 */
@ApiStatus.AvailableSince("1.0.0")
public class SerializationEvent {
	private SerializationEvent() {}

	/**
	 * Used to register codecs for classes without type parameters.
	 * @see StandardCodecResolver
 	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static final Event<RegisterStandardCodecEvent> REGISTER_STANDARD_CODEC_EVENT = new CustomEvent<>(EventFactory.createLoop(), StandardCodecResolver::refreshCodecs);

	/**
	 * Used to register codec resolvers for more advanced classes.
	 * @see CodecResolver
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static final Event<RegisterCodecResolverEvent> REGISTER_CODEC_RESOLVER_EVENT = new CustomEvent<>(EventFactory.createLoop(), CodecHelper::refreshCodecResolvers);

	/**
	 * A functional interface for listeners of {@link #REGISTER_STANDARD_CODEC_EVENT}
	 */
	@FunctionalInterface
	public interface RegisterStandardCodecEvent {
		/**
		 * Registers standard codecs.
		 * @param registry A bi-consumer that accepts and registers a standard codec.
		 *                 The first parameter is the class, the second is a codec holder for that class.
		 */
		void register(BiConsumer<Class<?>, CodecHolder<?>> registry);
	}

	/**
	 * A functional interface for listeners of {@link #REGISTER_CODEC_RESOLVER_EVENT}
	 */
	@FunctionalInterface
	public interface RegisterCodecResolverEvent {
		/**
		 * Registers codec resolvers.
		 * @param registry A consumer that accepts and registers a codec resolver.
		 */
		void register(Consumer<CodecResolver> registry);
	}
}
