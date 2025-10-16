package dev.scsupercraft.mc.libraries.corelib.serialisation;

import dev.scsupercraft.mc.libraries.corelib.serialisation.resolver.unique.TagKeyCodecResolver;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

/**
 * A reflective wrapper around generic types, used to resolve type parameters and annotations
 * for codec generation and serialization logic.
 * <br><br>
 * Supports nested generics, arrays, wildcards, and annotated types.
 * <br><br>
 * Do not touch unless you know what you are doing!
 * @param <T> The raw class type being represented.
 */
public class GenericClass<T> {
	/**
	 * The enclosing generic class, used to resolve type variables recursively.
	 * */
	public final GenericClass<?> parent;
	/**
	 * The class that this generic class represents.
	 */
	public final Class<T> clazz;
	/**
	 * The type parameters for the class.
	 */
	public final Map<TypeVariable<Class<T>>, GenericClass<?>> typeParameters = new HashMap<>();
	/**
	 * Is this an array type?
	 */
	public final boolean array;
	/**
	 * A nullable {@link AnnotatedType} for getting annotations if present.
	 * <p>
	 * See {@link TagKeyCodecResolver} for an example of how it's used.
	 */
	public final @Nullable AnnotatedType annotations;
	/**
	 * The hash code of this generic class.
	 */
	private @Nullable Integer hash;

	/**
	 * Creates a generic class from an {@link AnnotatedType}.
	 * @param annotatedType The annotated type.
	 * @return The generic class representing the annotated type.
	 */
	public static GenericClass<?> of(AnnotatedType annotatedType) {
		return of(annotatedType, null);
	}

	/**
	 * Creates a generic class from an {@link AnnotatedType}.
	 * @param annotatedType The annotated type.
	 * @param parentType The generic class that encloses this one.
	 * @return The generic class representing the annotated type.
	 */
	public static GenericClass<?> of(AnnotatedType annotatedType, GenericClass<?> parentType) {
		return of(annotatedType.getType(), annotatedType, parentType);
	}

	/**
	 * Creates a generic class for a {@link Type} and a nullable {@link AnnotatedType}
	 * @param type The type.
	 * @param annotatedType The annotated type.
	 * @return The generic class representing both the type and annotated type.
	 */
	public static GenericClass<?> of(Type type, @Nullable AnnotatedType annotatedType) {
		return of(type, annotatedType, null);
	}

	/**
	 * Creates a generic class for a {@link Type} and a nullable {@link AnnotatedType}
	 * @param type The type.
	 * @param annotatedType The annotated type.
	 * @param parentType The generic class that encloses this one.
	 * @return The generic class representing both the type and annotated type.
	 */
	public static GenericClass<?> of(Type type, @Nullable AnnotatedType annotatedType, GenericClass<?> parentType) {
		boolean array = false;
		if (type instanceof WildcardType wildcardType && wildcardType.getUpperBounds().length > 0) {
			type = wildcardType.getUpperBounds()[0];
			if (annotatedType instanceof AnnotatedWildcardType annotatedWildcardType && annotatedWildcardType.getAnnotatedUpperBounds().length > 0)
				annotatedType = annotatedWildcardType.getAnnotatedUpperBounds()[0];
		}
		if (type instanceof GenericArrayType genericArrayType) {
			type = genericArrayType.getGenericComponentType();
			if (annotatedType instanceof AnnotatedArrayType annotatedArrayType) annotatedType = annotatedArrayType.getAnnotatedGenericComponentType();
			array = true;
		}
		if (type instanceof Class<?> clazz) return array
				? new GenericClass<>(new GenericClass<>(clazz, null, parentType), annotatedType)
				: new GenericClass<>(clazz, annotatedType, parentType);
		if (type instanceof ParameterizedType parameterizedType && parameterizedType.getRawType() instanceof Class<?> clazz) {
			GenericClass<?> genericClass;
			if (annotatedType instanceof AnnotatedParameterizedType annotatedParameterizedType) {
				genericClass = new GenericClass<>(clazz, annotatedType, parentType, annotatedParameterizedType.getAnnotatedActualTypeArguments());
			} else {
				genericClass = new GenericClass<>(clazz, annotatedType, parentType, parameterizedType.getActualTypeArguments());
			}
			return array
					? new GenericClass<>(genericClass, annotatedType)
					: genericClass;
		}
		if (type instanceof TypeVariable<?> typeVariable) {
			GenericClass<?> genericClass = parentType;
			while (genericClass != null) {
				if (genericClass.typeParameters.containsKey(typeVariable)) return array
						? new GenericClass<>(genericClass.typeParameters.get(typeVariable), annotatedType)
						: genericClass.typeParameters.get(typeVariable);
				genericClass = genericClass.parent;
			}
		}
		throw new RuntimeException("Unsupported type: " + type);
	}

	/**
	 * Creates a new generic class as an array type.
	 * @param arrayType The type stored in the array.
	 * @param annotations The annotations for this type.
	 */
	public GenericClass(GenericClass<T> arrayType, @Nullable AnnotatedType annotations) {
		this.parent = arrayType;
		this.clazz = arrayType.clazz;
		this.typeParameters.putAll(arrayType.typeParameters);
		this.array = true;
		this.annotations = annotations;
	}

	/**
	 * Creates a new generic class representing the provided class.
	 * @param tClass The class this generic class represents.
	 * @param annotations The annotations for this type.
	 */
	public GenericClass(Class<T> tClass, @Nullable AnnotatedType annotations) {
		this(tClass, annotations, null);
	}

	/**
	 * Creates a new generic class representing the provided class.
	 * @param tClass The class this generic class represents.
	 * @param annotations The annotations for this type.
	 * @param parent The generic class that encloses this one.
	 */
	public GenericClass(Class<T> tClass, @Nullable AnnotatedType annotations, @Nullable GenericClass<?> parent) {
		this(tClass, annotations, parent, new Type[]{});
	}

	/**
	 * Creates a new generic class representing the provided class.
	 * @param tClass The class this generic class represents.
	 * @param annotations The annotations for this type.
	 * @param parent The generic class that encloses this one.
	 * @param typeParameters The type parameters used.
	 */
	public GenericClass(Class<T> tClass, @Nullable AnnotatedType annotations, @Nullable GenericClass<?> parent, Type[] typeParameters) {
		this(tClass, annotations, parent, self -> {
			GenericClass<?>[] resolved = new GenericClass<?>[typeParameters.length];
			for (int i = 0; i < typeParameters.length; i++) {
				resolved[i] = GenericClass.of(typeParameters[i], null, self);
			}
			return resolved;
		});
	}

	/**
	 * Creates a new generic class representing the provided class.
	 * @param tClass The class this generic class represents.
	 * @param annotations The annotations for this type.
	 * @param parent The generic class that encloses this one.
	 * @param typeParameters The type parameters used.
	 */
	public GenericClass(Class<T> tClass, @Nullable AnnotatedType annotations, @Nullable GenericClass<?> parent, AnnotatedType[] typeParameters) {
		this(tClass, annotations, parent, self -> {
			GenericClass<?>[] resolved = new GenericClass<?>[typeParameters.length];
			for (int i = 0; i < typeParameters.length; i++) {
				resolved[i] = GenericClass.of(typeParameters[i].getType(), typeParameters[i], self);
			}
			return resolved;
		});
	}

	/**
	 * Creates a new generic class representing the provided class.
	 * <p><strong>Implementation note:</strong> Use this constructor when type parameters depend on runtime resolution or recursive context.
	 * @param tClass The class this generic class represents.
	 * @param annotations The annotations for this type.
	 * @param parent The generic class that encloses this one.
	 * @param typeParameterProvider A function that gets given this generic class during creation and returns the type parameters used.
	 *                              This is here in case you need to use more advanced generics.
	 */
	public GenericClass(Class<T> tClass, @Nullable AnnotatedType annotations, @Nullable GenericClass<?> parent, Function<GenericClass<T>, GenericClass<?>[]> typeParameterProvider) {
		this.clazz = tClass;
		this.parent = parent;
		this.array = false;
		this.annotations = annotations;
		GenericClass<?>[] typeParameters = typeParameterProvider.apply(this);
		if (typeParameters.length != tClass.getTypeParameters().length)
			throw new RuntimeException(
					"Wrong amount of type parameters for class: "
							+ tClass
							+ " expected "
							+ tClass.getTypeParameters().length
							+ ", received "
							+ typeParameters.length
			);
		for (int i = 0; i < typeParameters.length; i++) {
			this.typeParameters.put(tClass.getTypeParameters()[i], typeParameters[i]);
		}
	}

	/**
	 * Creates an {@link Iterator} for the type parameters, staying in the order that they were defined in.
	 * @return The iterator.
	 */
	public Iterator<? extends GenericClass<?>> typeParameterIterator() {
		return Arrays.stream(clazz.getTypeParameters()).map(typeParameters::get).iterator();
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "<", ">");
		sj.setEmptyValue("");
		for(TypeVariable<Class<T>> typeVariable : clazz.getTypeParameters()) {
			sj.add(typeParameters.get(typeVariable).toString());
		}
		return clazz.getTypeName() + sj + (array ? "[]" : "");
	}

	@Override
	public int hashCode() {
		if (hash != null) return hash;
		return hash = annotations != null && annotations.getDeclaredAnnotations().length != 0
				? typeParameters.isEmpty() ? Objects.hash(clazz.getName(), array, annotations) : Objects.hash(clazz.getName(), typeParameters, array, annotations)
				: typeParameters.isEmpty() ? Objects.hash(clazz.getName(), array) : Objects.hash(clazz.getName(), typeParameters, array);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof GenericClass<?> other)) return super.equals(obj);
		if (clazz != other.clazz || array != other.array) return false;
		for (TypeVariable<Class<T>> typeVariable : typeParameters.keySet()) {
			GenericClass<?> genericClass = typeParameters.get(typeVariable);
			GenericClass<?> genericClass1 = other.typeParameters.get(typeVariable);

			if (!genericClass.equals(genericClass1)) return false;
		}
		Annotation[] annotations1 = annotations != null ? annotations.getDeclaredAnnotations() : new Annotation[0];
		Annotation[] annotations2 = other.annotations != null ? other.annotations.getDeclaredAnnotations() : new Annotation[0];
		return Arrays.equals(annotations1, annotations2);
	}
}
