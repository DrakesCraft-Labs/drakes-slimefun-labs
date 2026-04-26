package net.guizhanss.fastmachines;

import java.lang.reflect.Constructor;

import org.bukkit.plugin.Plugin;

/**
 * Evita NoSuchMethodError entre ctor {@code Metrics(Plugin,int)} y {@code Metrics(JavaPlugin,int)}
 * cuando el classpath de compilación mezcla bStats 3.x con restos embebidos en GuizhanLib.
 * En el JAR sombreado la clase vive en el paquete relocado.
 */
public final class MetricsBootstrap {

    private static final String[] METRICS_CLASS_NAMES = {
        "com.github.drakescraft_labs.fastmachines.libs.bstats.bukkit.Metrics",
        "org.bstats.bukkit.Metrics",
    };

    private MetricsBootstrap() {}

    public static void start(Plugin plugin, int serviceId) {
        for (String className : METRICS_CLASS_NAMES) {
            try {
                Class<?> metricsClass = Class.forName(className);
                for (Constructor<?> ctor : metricsClass.getConstructors()) {
                    Class<?>[] p = ctor.getParameterTypes();
                    if (p.length != 2) {
                        continue;
                    }
                    if (!(p[1] == int.class || p[1] == Integer.TYPE)) {
                        continue;
                    }
                    if (!p[0].isAssignableFrom(plugin.getClass())) {
                        continue;
                    }
                    ctor.newInstance(plugin, serviceId);
                    return;
                }
            } catch (ReflectiveOperationException | LinkageError ignored) {
                // siguiente nombre de clase o constructor
            }
        }
    }
}
