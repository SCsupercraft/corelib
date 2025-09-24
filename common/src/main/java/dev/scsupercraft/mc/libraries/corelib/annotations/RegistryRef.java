package dev.scsupercraft.mc.libraries.corelib.annotations;

import dev.scsupercraft.mc.libraries.corelib.serialization.resolver.unique.TagKeyCodecResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to tell codec resolvers what registry something belongs to.
 * <p>
 * See {@link TagKeyCodecResolver} for an example.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE_USE })
public @interface RegistryRef {
	/**
	 * Used to specify the name of the registry.
	 * @return The name of the registry.
	 */
	String value();

	/**
	 * Used to specify the id of the mod which added the registry.
	 * @return The id of the mod which added the registry.
	 */
	String namespace() default "minecraft";
}
