package dev.scsupercraft.mc.libraries.corelib.api.util;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.scsupercraft.mc.libraries.corelib.CoreLib;
import dev.scsupercraft.mc.libraries.corelib.api.data.SyncedData;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for synchronising data.
 */
@ApiStatus.AvailableSince("1.0.0")
public class Synchronisation {
	private static final Map<Identifier, SyncedObject<?>> SYNCED_OBJECTS = new HashMap<>();

	static {
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, DataSyncPacket.PACKET_ID, DataSyncPacket.PACKET_CODEC, (packet, context) -> {
			SyncedData<Object> syncedData = Synchronisation.get(packet.id()).syncedData();
			syncedData.setSyncedData(packet.data());
			CoreLib.LOGGER.info("Synchronised {} with the server.", packet.id());
		});

		PlayerEvent.PLAYER_JOIN.register(Synchronisation::synchronise);
	}

	/**
	 * Set up an object for synchronisation.
	 * <p>
	 * Failing to call this method will result in errors upon trying to synchronise the data.
	 * @param syncedData  The synced data to set up synchronisation for.
	 * @param codecHolder The codec holder to use for serialisation.
	 * @param <T>         The type of data being synchronised.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static <T> void setup(SyncedData<T> syncedData, CodecHolder<T> codecHolder) {
		if (!SYNCED_OBJECTS.containsKey(syncedData.getSyncId())) SYNCED_OBJECTS.put(syncedData.getSyncId(), new SyncedObject<>(syncedData, codecHolder));
	}

	/**
	 * Synchronises the data to all clients.
	 * @param syncedData The data to synchronise.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static void synchronise(SyncedData<?> syncedData) {
		synchronise(SYNCED_OBJECTS.get(syncedData.getSyncId()));
	}

	/**
	 * Synchronises the data to the specified client.
	 * @param syncedData The data to synchronise.
	 * @param client     The client to send the data to.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static void synchronise(SyncedData<?> syncedData, ServerPlayerEntity client) {
		synchronise(SYNCED_OBJECTS.get(syncedData.getSyncId()), client);
	}

	/**
	 * Synchronises all data to the specified client.
	 * @param client The client to send the data to.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public static void synchronise(ServerPlayerEntity client) {
		SYNCED_OBJECTS.values().forEach(obj -> synchronise(obj, client));
	}

	private static void synchronise(SyncedObject<?> syncedObject) {
		CoreLib.LOGGER.info("Synchronising {} to all clients...", syncedObject.syncedData.getSyncId());
		syncedObject.sync();
	}
	private static void synchronise(SyncedObject<?> syncedObject, ServerPlayerEntity client) {
		CoreLib.LOGGER.info("Synchronising {} to client {}...", syncedObject.syncedData.getSyncId(), client.getGameProfile().getName());
		syncedObject.sync(client);
	}

	private static <T> SyncedObject<T> get(Identifier id) {
		return Utils.cast(SYNCED_OBJECTS.get(id));
	}

	private record SyncedObject<T>(SyncedData<T> syncedData, CodecHolder<T> codecHolder) {
		private DataSyncPacket<T> createPacket() {
			return new DataSyncPacket<>(syncedData.getSyncId(), syncedData.getData());
		}

		private void sync() {
			if (CoreLib.server == null) return;
			NetworkManager.sendToPlayers(CoreLib.server.getPlayerManager().getPlayerList(), createPacket());
		}

		private void sync(ServerPlayerEntity client) {
			if (CoreLib.server == null || client == null) return;
			NetworkManager.sendToPlayer(client, createPacket());
		}
	}
	private record DataSyncPacket<T>(Identifier id, T data) implements CustomPayload {
		public static final Id<DataSyncPacket<?>> PACKET_ID = new Id<>(Identifier.of(CoreLib.MOD_ID, "data_sync"));

		public static final PacketCodec<PacketByteBuf, DataSyncPacket<?>> PACKET_CODEC = PacketCodec.of((value, buf) -> {
			buf.writeIdentifier(value.id);
			SyncedObject<Object> synced = get(value.id);
			synced.codecHolder().packetCodec().encode(buf, value.data);
		}, buf -> {
			Identifier id = buf.readIdentifier();
			return new DataSyncPacket<>(id, get(id).codecHolder().packetCodec().decode(buf));
		});

		@Override
		public Id<? extends CustomPayload> getId() {
			return PACKET_ID;
		}
	}

	private Synchronisation() {}
}
