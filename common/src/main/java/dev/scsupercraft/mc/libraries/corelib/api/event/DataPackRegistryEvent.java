package dev.scsupercraft.mc.libraries.corelib.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.scsupercraft.mc.libraries.corelib.api.util.DataPackRegistrar;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contains events for data pack registries.
 */
@ApiStatus.AvailableSince("1.1.0")
public abstract class DataPackRegistryEvent {
	private DataPackRegistryEvent() {}

	/**
	 * Allows registering new data pack registries.
	 */
	@ApiStatus.AvailableSince("1.1.0")
	public static final Event<NewRegistry> NEW_REGISTRY = new CustomEvent<>(EventFactory.createLoop(), DataPackRegistryEvent::onUpdatedListeners);

	@ExpectPlatform
	private static void onUpdatedListeners() {
		throw new AssertionError();
	}

	/**
	 * A functional interface for listeners of {@link #NEW_REGISTRY}
	 */
	@FunctionalInterface
	public interface NewRegistry {
		/**
		 * Registers data pack registries.
		 * @param registrar The registrar for registering data pack registries.
		 */
		void addRegistries(DataPackRegistrar registrar);
	}
}
