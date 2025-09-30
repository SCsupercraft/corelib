package dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.basic;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHelper;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecHolder;
import dev.scsupercraft.mc.libraries.corelib.api.serialisation.CodecResolver;
import dev.scsupercraft.mc.libraries.corelib.api.util.Utils;
import dev.scsupercraft.mc.libraries.corelib.serialisation.GenericClass;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.function.Function;

/**
 * An advanced codec resolver that should work for all records,
 * as long as the {@link CodecHelper} can also resolve codecs for the record components.
 */
public final class RecordCodecResolver implements CodecResolver {
	private static <T> Codec<T> buildRecordCodec(GenericClass<T> genericClass) {
		Class<T> recordClass = genericClass.clazz;
		RecordComponent[] recordComponents = recordClass.getRecordComponents();
		try {
			Constructor<T> constructor = Utils.cast(recordClass.getDeclaredConstructors()[0]);
			constructor.setAccessible(true);

			if (recordComponents.length == 0) {
				return Codec.unit(constructor.newInstance());
			}

			List<RecordCodecBuilder<T, ?>> builders = new ArrayList<>();
			for (RecordComponent component : recordComponents) {
				@SuppressWarnings("unchecked")
				CodecHolder<Object> holder = (CodecHolder<Object>) CodecHelper.getCodec(GenericClass.of(component.getGenericType(), component.getAnnotatedType(), genericClass));

				Method accessor = component.getAccessor();
				accessor.setAccessible(true);

				builders.add(holder.codec().flatXmap(RecordCodecResolver::toResult, RecordCodecResolver::fromResult).fieldOf(component.getName()).forGetter(obj -> {
					try {
						return new Result<>(accessor.invoke(obj), null);
					} catch (Exception e) {
						return new Result<>(null, new RuntimeException("Error encoding record", e));
					}
				}));
			}

			return RecordCodecBuilder.create(instance -> group(builders, instance, args -> {
				try {
					return constructor.newInstance(noResult(args));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException("Error creating new instance, constructor arguments: " + Arrays.toString(noResult(args)), e);
				}
			}));
		} catch (Exception e) {
			throw new RuntimeException("Failed to build record codec!", e);
		}
	}

	private static <O> App<RecordCodecBuilder.Mu<O>, O> group(List<RecordCodecBuilder<O, ?>> list, RecordCodecBuilder.Instance<O> instance, Function<Object[], O> function) {
		switch (list.size()) {
			case 1 ->  { return instance.group(list.get(0)).apply(instance, a -> function.apply(new Object[]{ a })); }
			case 2 ->  { return instance.group(list.get(0), list.get(1)).apply(instance, (a, b) -> function.apply(new Object[]{ a, b })); }
			case 3 ->  { return instance.group(list.get(0), list.get(1), list.get(2)).apply(instance, (a, b, c) -> function.apply(new Object[]{ a, b, c })); }
			case 4 ->  { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3)).apply(instance, (a, b, c, d) -> function.apply(new Object[]{ a, b, c, d })); }
			case 5 ->  { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4)).apply(instance, (a, b, c, d, e) -> function.apply(new Object[]{ a, b, c, d, e })); }
			case 6 ->  { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5)).apply(instance, (a, b, c, d, e, f) -> function.apply(new Object[]{ a, b, c, d, e, f })); }
			case 7 ->  { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6)).apply(instance, (a, b, c, d, e, f, g) -> function.apply(new Object[]{ a, b, c, d, e, f, g })); }
			case 8 ->  { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7)).apply(instance, (a, b, c, d, e, f, g, h) -> function.apply(new Object[]{ a, b, c, d, e, f, g, h })); }
			case 9 ->  { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), list.get(8)).apply(instance, (a, b, c, d, e, f, g, h, i) -> function.apply(new Object[]{ a, b, c, d, e, f, g, h, i })); }
			case 10 -> { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), list.get(8), list.get(9)).apply(instance, (a, b, c, d, e, f, g, h, i, j) -> function.apply(new Object[]{ a, b, c, d, e, f, g, h, i, j })); }
			case 11 -> { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), list.get(8), list.get(9), list.get(10)).apply(instance, (a, b, c, d, e, f, g, h, i, j, k) -> function.apply(new Object[]{ a, b, c, d, e, f, g, h, i, j, k })); }
			case 12 -> { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), list.get(8), list.get(9), list.get(10), list.get(11)).apply(instance, (a, b, c, d, e, f, g, h, i, j, k, l) -> function.apply(new Object[]{ a, b, c, d, e, f, g, h, i, j, k, l })); }
			case 13 -> { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), list.get(8), list.get(9), list.get(10), list.get(11), list.get(12)).apply(instance, (a, b, c, d, e, f, g, h, i, j, k, l, m) -> function.apply(new Object[]{ a, b, c, d, e, f, g, h, i, j, k, l, m })); }
			case 14 -> { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), list.get(8), list.get(9), list.get(10), list.get(11), list.get(12), list.get(13)).apply(instance, (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> function.apply(new Object[]{ a, b, c, d, e, f, g, h, i, j, k, l, m, n })); }
			case 15 -> { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), list.get(8), list.get(9), list.get(10), list.get(11), list.get(12), list.get(13), list.get(14)).apply(instance, (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> function.apply(new Object[]{ a, b, c, d, e, f, g, h, i, j, k, l, m, n, o })); }
			case 16 -> { return instance.group(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), list.get(8), list.get(9), list.get(10), list.get(11), list.get(12), list.get(13), list.get(14), list.get(15)).apply(instance, (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> function.apply(new Object[]{ a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p })); }
		}
		throw new IllegalStateException();
	}

	@Override
	public boolean supportsValue(GenericClass<?> genericClass) {
		return genericClass.clazz.isRecord();
	}

	@Override
	public @NotNull <T> CodecHolder<T> resolveCodec(GenericClass<T> genericClass) {
		if (!genericClass.clazz.isRecord()) throw new IllegalArgumentException("You can only create an record codec from a record!");

		Codec<T> codec = buildRecordCodec(genericClass);
		PacketCodec<ByteBuf, T> packetCodec = PacketCodecs.codec(codec);

		return new CodecHolder<>(codec, packetCodec);
	}

	/**
	 * Creates a new {@link RecordCodecResolver}.
	 */
	public RecordCodecResolver() {

	}

	private static Object[] noResult(Object[] objects) {
		Object[] array = new Object[objects.length];
		for (int i = 0; i < objects.length; i++) {
			array[i] = noResult(objects[i]);
		}
		return array;
	}

	private static Object noResult(Object object) {
		if (object instanceof Result<?> result) {
			if (result.value != null) return result.value;
			if (result.error != null) throw result.error;
		}
		return object;
	}

	private static <T> DataResult<Result<T>> toResult(T object) {
		return DataResult.success(new Result<>(object, null));
	}

	private static <T> DataResult<T> fromResult(Result<T> result) {
		return result.error != null
				? DataResult.error(result.error::getMessage)
				: DataResult.success(result.value);
	}

	private record Result<T>(@Nullable T value, @Nullable RuntimeException error) {
		@Override
		public String toString() {
			return value == null
					? "Result[error=" + error + "]"
					: "Result[value=" + value + "]";
		}
	}
}
