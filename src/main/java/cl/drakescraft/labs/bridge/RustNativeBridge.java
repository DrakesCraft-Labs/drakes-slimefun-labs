package cl.drakescraft.labs.bridge;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * drakes-slimefun-labs Java 21 Project Panama (FFM API) Native Rust Bridge
 * High-Performance Native Ticker & Cargo Routing Dispatcher for all 44 Addons.
 */
public final class RustNativeBridge {
    private static final Logger LOGGER = Logger.getLogger("SlimefunLabs-RustBridge");
    private static boolean isNativeLoaded = false;
    private static MethodHandle solveEnergyTickMH;
    private static MethodHandle executeTickCycleMH;

    public static void initialize(Path nativeLibPath) {
        try {
            System.load(nativeLibPath.toAbsolutePath().toString());
            SymbolLookup lookup = SymbolLookup.loaderLookup();
            Linker linker = Linker.nativeLinker();

            MemorySegment energySym = lookup.find("slimefun_solve_energy_tick").orElse(null);
            MemorySegment tickSym = lookup.find("slimefun_execute_tick_cycle").orElse(null);

            if (energySym != null && tickSym != null) {
                solveEnergyTickMH = linker.downcallHandle(energySym, FunctionDescriptor.of(ValueLayout.JAVA_LONG));
                executeTickCycleMH = linker.downcallHandle(tickSym, FunctionDescriptor.of(ValueLayout.JAVA_LONG));
                isNativeLoaded = true;
                LOGGER.info("⚡ [drakes-slimefun-labs] Successfully bound to Slimefun-Rust Native FFM Engine!");
            }
        } catch (Throwable t) {
            LOGGER.warning("⚠️ [drakes-slimefun-labs] Slimefun-Rust native library not loaded: " + t.getMessage());
        }
    }

    public static long solveEnergyTick() {
        if (isNativeLoaded && solveEnergyTickMH != null) {
            try {
                return (long) solveEnergyTickMH.invokeExact();
            } catch (Throwable ignored) {}
        }
        return 0;
    }

    public static long executeTickCycle() {
        if (isNativeLoaded && executeTickCycleMH != null) {
            try {
                return (long) executeTickCycleMH.invokeExact();
            } catch (Throwable ignored) {}
        }
        return 0;
    }

    public static boolean isNativeLoaded() {
        return isNativeLoaded;
    }
}
