package dev.scsupercraft.mc.libraries.corelib.api.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.scsupercraft.mc.libraries.corelib.api.serialization.CodecHolder;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A helper class for save data.
 * @param <T> The type of the data being saved.
 */
@ApiStatus.AvailableSince("1.0.0")
public sealed class SaveData<T> implements Data<T> permits AutoSaveData, WorldSaveData {
	private static final Gson GSON = new GsonBuilder().create();
	private final Supplier<File> fileGetter;
	private final CodecHolder<T> codecHolder;
	private boolean dirty = false;
	private final Type type;
	private final Supplier<T> defaultValue;
	@NotNull
	private T value;

	/**
	 * Creates new save data.
	 * @param defaultValue A supplier that returns the default value for this savable. Used if there isn't any existing data during loading.
	 * @param codecHolder A codec holder for serializing the save data.
	 * @param fileGetter A supplier that returns the file that the data will be saved to.
	 * @param type What should the data be saved as.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public SaveData(Supplier<@NotNull T> defaultValue, CodecHolder<T> codecHolder, Supplier<File> fileGetter, Type type) {
		this.defaultValue = defaultValue;
		this.value = defaultValue.get();
		this.codecHolder = codecHolder;
		this.fileGetter = fileGetter;
		this.type = type;
	}

	/**
	 * Loads the data from the disk.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public void load() {
		File file = fileGetter.get();
		if (!file.exists()) {
			value = defaultValue.get();
			save();
			return;
		}

		try (FileReader fileReader = new FileReader(file)) {
			switch (type) {
				case JSON -> {
					JsonObject object = GSON.fromJson(fileReader, JsonObject.class);

					DataResult<Pair<T, JsonElement>> result = codecHolder.codec().decode(JsonOps.INSTANCE, object.get("data"));
					value = result.getOrThrow().getFirst();
				}
				case NBT -> {
					NbtCompound compound = NbtIo.readCompressed(file.toPath(), NbtSizeTracker.ofUnlimitedBytes());

					DataResult<Pair<T, NbtElement>> result = codecHolder.codec().decode(NbtOps.INSTANCE, compound.get("data"));
					value = result.getOrThrow().getFirst();
				}
			}
		} catch (IOException | IllegalStateException e) {
			throw new RuntimeException("Error loading data", e);
		}
	}

	/**
	 * Saves the data to the disk.
	 * Only saves if marked with {@link #markDirty()} or if the file doesn't already exist.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public void save() {
		File file = fileGetter.get();

		if (!dirty && file.exists()) return;
		dirty = false;

		if (!file.getParentFile().isDirectory() && !file.getParentFile().mkdirs()) {
			return;
		}

		try (FileWriter fileWriter = new FileWriter(file)) {
			switch (type) {
				case JSON -> {
					JsonObject object = new JsonObject();
					JsonElement element = codecHolder.codec().encodeStart(JsonOps.INSTANCE, value).getOrThrow();
					object.add("data", element);

					fileWriter.write(GSON.toJson(object));
				}
				case NBT -> {
					NbtCompound compound = new NbtCompound();
					NbtElement element = codecHolder.codec().encodeStart(NbtOps.INSTANCE, value).getOrThrow();
					compound.put("data", element);
					NbtIo.writeCompressed(compound, file.toPath());
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Error saving data", e);
		}
	}

	/**
	 * Gets the codec holder used for serialization.
	 * @return The codec holder
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public CodecHolder<T> getCodecHolder() {
		return codecHolder;
	}

	/**
	 * Get the saved value.
	 * @return The current value.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	@Override
	public @NotNull T getData() {
		return value;
	}

	/**
	 * Set the saved value.
	 * This calls {@link #markDirty()} for you.
	 * @param value The new value.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	@Override
	public void setData(@NotNull T value) {
		this.value = Objects.requireNonNull(value);
		markDirty();
	}

	/**
	 * Tell the savable that it was updated.
	 * Failing to call this method after an update will result in the data not getting saved.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public void markDirty() {
		this.dirty = true;
	}

	/**
	 * How the data should be saved.
	 */
	@ApiStatus.AvailableSince("1.0.0")
	public enum Type {
		/**
		 * The data is saved as json without pretty print.
		 */
		JSON,
		/**
		 * The data is saved as compressed nbt.
		 */
		NBT
	}
}
