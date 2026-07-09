package dev.drake.dough.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Este es un clon simplificado de ReflectionUtils, purgado de lógica NMS legacy.
 * 
 * @author TheBusyBiscuit
 * @author Drake (DrakeLabs Clean Port)
 *
 */
@SuppressWarnings("java:S3011")
public final class ReflectionUtils {

    private ReflectionUtils() {}

    /**
     * Returns a certain Method in the specified Class
     *
     * @param c
     *            The Class in which the Method is in
     * @param method
     *            The Method you are looking for
     * @return The found Method
     */
    public static @Nullable Method getMethod(@Nonnull Class<?> c, @Nonnull String method) {
        for (Method m : c.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }

        return null;
    }

    /**
     * Returns the Method with certain Parameters
     *
     * @param c
     *            The Class in which the Method is in
     * @param method
     *            The Method you are looking for
     * @param paramTypes
     *            The Types of the Parameters
     * @return The found Method
     */
    public static @Nullable Method getMethod(@Nonnull Class<?> c, @Nonnull String method, Class<?>... paramTypes) {
        Class<?>[] expectParamTypes = toPrimitiveTypeArray(paramTypes);

        for (Method m : c.getMethods()) {
            Class<?>[] methodParameters = toPrimitiveTypeArray(m.getParameterTypes());

            if ((m.getName().equals(method)) && (equalsTypeArray(methodParameters, expectParamTypes))) {
                return m;
            }
        }

        return null;
    }

    /**
     * Returns the Field of a Class
     *
     * @param c
     *            The Class conating this Field
     * @param field
     *            The name of the Field you are looking for
     * @return The found Field
     *
     * @throws NoSuchFieldException
     *             If the field could not be found.
     */
    public static @Nonnull Field getField(@Nonnull Class<?> c, @Nonnull String field) throws NoSuchFieldException {
        return c.getDeclaredField(field);
    }

    /**
     * Modifies a Field in an Object
     *
     * @param <T>
     *            The type of the specified field
     * @param object
     *            The Object containing the Field
     * @param c
     *            The Class in which we are looking for this field
     * @param field
     *            The Name of that Field
     * @param value
     *            The Value for that Field
     * 
     * @throws NoSuchFieldException
     *             If the field could not be found.
     * @throws IllegalAccessException
     *             If the field could not be modified.
     */
    public static <T> void setFieldValue(@Nonnull T object, @Nonnull Class<?> c, @Nonnull String field, @Nullable Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = getField(c, field);
        f.setAccessible(true);
        f.set(object, value);
    }

    /**
     * Converts the Classes to a Primitive Type Array
     * in order to be used as paramaters
     *
     * @param classes
     *            The Types you want to convert
     * 
     * @return An Array of primitive Types
     */
    public static @Nonnull Class<?>[] toPrimitiveTypeArray(@Nonnull Class<?>[] classes) {
        int size = classes.length;
        Class<?>[] types = new Class[size];

        for (int i = 0; i < size; i++) {
            types[i] = PrimitiveTypeConversion.convertIfNecessary(classes[i]);
        }

        return types;
    }

    /**
     * Returns the Constructor of a Class with the specified Parameters
     *
     * @param <T>
     *            The Type argument for the class of this constructor
     * @param c
     *            The Class containing the Constructor
     * @param paramTypes
     *            The Parameters for that Constructor
     * 
     * @return The Constructor for that Class
     */
    @SuppressWarnings("unchecked")
    public static @Nullable <T> Constructor<T> getConstructor(@Nonnull Class<T> c, Class<?>... paramTypes) {
        Class<?>[] expectedParamTypes = toPrimitiveTypeArray(paramTypes);

        for (Constructor<?> constructor : c.getConstructors()) {
            Class<?>[] constructorTypes = toPrimitiveTypeArray(constructor.getParameterTypes());

            if (equalsTypeArray(constructorTypes, expectedParamTypes)) {
                return (Constructor<T>) constructor;
            }
        }

        return null;
    }

    /**
     * Compares multiple Type Arrays
     *
     * @param a
     *            The first Array for comparison
     * @param b
     *            All following Array you want to compare
     * 
     * @return Whether they equal each other
     */
    private static boolean equalsTypeArray(@Nonnull Class<?>[] a, Class<?>[] b) {
        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if ((!a[i].equals(b[i])) && (!a[i].isAssignableFrom(b[i]))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns all Enum Constants in an Enum
     *
     * @param <T>
     *            The Type argument of the enum we are querying
     * @param c
     *            The Enum you are targeting
     * 
     * @return An ArrayList of all Enum Constants in that Enum
     */
    public static @Nonnull <T extends Enum<T>> List<T> getEnumConstants(@Nonnull Class<T> c) {
        return Arrays.asList(c.getEnumConstants());
    }

    /**
     * Returns a specific Enum Constant in an Enum
     *
     * @param <T>
     *            The Type argument of the enum we are querying
     * @param c
     *            The Enum you are targeting
     * @param name
     *            The Name of the Constant you are targeting
     * 
     * @return The found Enum Constant
     */
    @ParametersAreNonnullByDefault
    public static @Nullable <T extends Enum<T>> T getEnumConstant(Class<T> c, String name) {
        for (T field : c.getEnumConstants()) {
            if (field.toString().equals(name)) {
                return field;
            }
        }

        return null;
    }
}
