package dev.scsupercraft.mc.libraries.corelib.api.event;

import dev.architectury.event.Event;
import org.jetbrains.annotations.ApiStatus;

/**
 * An event wrapper that takes a runnable, which is called when a listener is added or removed.
 * @param <T> The type of event.
 */
@ApiStatus.AvailableSince("1.0.0")
public class CustomEvent<T> implements Event<T> {
	/**
	 * The event to listen to.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	protected final Event<T> event;
	/**
	 * The function to run when an event listener is added or removed.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	protected final Runnable onRefresh;

	/**
	 * Creates a new custom event.
	 * @param event The event to listen to.
	 * @param onRefresh The function to run when an event listener is added or removed.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public CustomEvent(Event<T> event, Runnable onRefresh) {
		this.event = event;
		this.onRefresh = onRefresh;
	}

	@Override
	public T invoker() {
		return event.invoker();
	}

	@Override
	public void register(T listener) {
		event.register(listener);
		onRefresh.run();
	}

	@Override
	public void unregister(T listener) {
		event.unregister(listener);
		onRefresh.run();
	}

	@Override
	public boolean isRegistered(T listener) {
		return event.isRegistered(listener);
	}

	@Override
	public void clearListeners() {
		event.clearListeners();
		onRefresh.run();
	}
}
