package com.github.drakescraft_labs.slimefuntranslation.utils;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ReflectionUtils {
    @ParametersAreNonnullByDefault
    public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            clazz.getDeclaredMethod(methodName, parameterTypes);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (LinkageError | IllegalStateException e) {
            // p. ej. classloader de otro plugin con JAR cerrado (zip file closed) tras fallo de enable
            return false;
        } catch (Throwable t) {
            return false;
        }
    }
}
