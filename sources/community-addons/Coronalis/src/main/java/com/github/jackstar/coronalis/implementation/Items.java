package com.github.jackstar.coronalis.implementation;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public final class Items {

    private Items() {}

    /* Componentes de Crafteo */
    public static final SlimefunItemStack CORONALIS_ANTENNA_DISH = new SlimefunItemStack(
            "CORONALIS_ANTENNA_DISH",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTI2ZDhjMDJmZjM2Y2QyM2EyNzBhYjNjMzk5YTQxYzE1MzhhMTcyNzM0MzZiMzg4ZjhiMThlYmZlZDRjNzU1In19fQ==",
            "&dPlato de Antena Parabólica",
            "&7Componente de ingeniería de radio",
            "&7utilizado en la construcción de radiotelescopios.",
            "",
            "&5&oCoronalis Array Labs"
    );

    public static final SlimefunItemStack CORONALIS_RECEIVER = new SlimefunItemStack(
            "CORONALIS_RECEIVER",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGJkMjU2ODBlY2EyMzk3Zjg4M2FlNjFmOTVkMzcyY2FiZjFmMTBhZDY1YjUyYjNjYTQzODdhMDdiOTlhOCJ9fX0=",
            "&dReceptor de Radiofrecuencia (1mm)",
            "&7Receptor criogénico de alta fidelidad",
            "&7sintonizado para ondas milimétricas de 1mm.",
            "&7Captura variaciones de fase y amplitud.",
            "",
            "&5&oCoronalis Array Labs"
    );

    public static final SlimefunItemStack CORONALIS_PID_CONTROLLER = new SlimefunItemStack(
            "CORONALIS_PID_CONTROLLER",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzhkMWEyODFhNDVkOWZlOTk3MTVkNzhkYTg5MDJhMmRkMGUyYmE1YzVkMDUwNzAxNjc5MDdjZTcxMWRjMmRlIn19fQ==",
            "&dControlador PID del Vacío",
            "&7Controlador Proporcional-Integral-Derivativo.",
            "&7Ajusta automáticamente los servomotores",
            "&7para contrarrestar disturbios de viento en las antenas.",
            "",
            "&5&oCoronalis Array Labs"
    );

    public static final SlimefunItemStack CORONALIS_DATA_CELL = new SlimefunItemStack(
            "CORONALIS_DATA_CELL",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU2Y2M2Yjg4MzMwZjU2M2RhMjJkOGU0YWM4ZjEwMWVhMzhiNjg5YTc3NTc2NTFkNjVhMWIyOTgxMTU0OWUifX19",
            "&bCelda de Datos Celestes",
            "&7Celda de almacenamiento de telemetría y datos.",
            "&7Insértala en la consola de control para",
            "&7registrar nuevos descubrimientos espaciales.",
            "",
            "&9&oCoronalis Array Command"
    );

    public static final SlimefunItemStack CORONALIS_COAXIAL_CABLE = new SlimefunItemStack(
            "CORONALIS_COAXIAL_CABLE",
            Material.CHAIN,
            "&8Cable Coaxial de Fase",
            "&7Bloque conductor para enlazar consolas,",
            "&7núcleos SU y radiotelescopios Coronalis.",
            "&7La red valida continuidad bloque a bloque.",
            "",
            "&5&oCoronalis Array Infrastructure"
    );

    public static final SlimefunItemStack CORONALIS_SIGNAL_CORE = new SlimefunItemStack(
            "CORONALIS_SIGNAL_CORE",
            Material.LODESTONE,
            "&bNúcleo de Energía SU",
            "&7Genera Signal Units para alimentar",
            "&7movimiento PID, calibración y correlación.",
            "&7Debe conectarse a la consola con cable coaxial.",
            "",
            "&9&oCoronalis Array Power"
    );

    /* Bloques Funcionales */
    public static final SlimefunItemStack CORONALIS_RADIO_TELESCOPE = new SlimefunItemStack(
            "CORONALIS_RADIO_TELESCOPE",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWM5MzhjYTFkYjBiYzhhMTc3M2RlOTRkNzNjYzFmODU5OTM0M2M1YjI3NDQ3NDliY2VkM2QxMWNjMTlhIn19fQ==",
            "&6Escucha del Vacío — Antena",
            "&7Unidad de antena parabólica del observatorio.",
            "&7Debe conectarse por cable coaxial",
            "&7a una consola de control.",
            "&7Máximo operativo: 50 por red.",
            "",
            "&5&oCoronalis Array Labs"
    );

    public static final SlimefunItemStack CORONALIS_CONTROL_CONSOLE = new SlimefunItemStack(
            "CORONALIS_CONTROL_CONSOLE",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0N2M3OWU3MTRlYmNmYTliZjVlOTFkMzA1Y2VjYmI3ODhlNGM0MDIyM2ZkM2E0Y2MzNWM4YWQyMTZhMDFlIn19fQ==",
            "&6Consola de Control del Observatorio",
            "&7Permite seleccionar objetivos cósmicos,",
            "&7alinear las antenas con el bucle PID",
            "&7y correlacionar señales electromagnéticas.",
            "",
            "&9&oCoronalis Array Command"
    );

    /* Ítems Especiales */
    public static final SlimefunItemStack CORONALIS_RECORD_DISCOVERED = new SlimefunItemStack(
            "CORONALIS_RECORD_DISCOVERED",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjVkMWMyMjk1OGE1Yzg3M2JiYWFlZTQ0Nzg5ODcxOTM1M2NmYmQzMDkyODk5In19fQ==",
            "&e&lEco de Fase Coronalis",
            "&7Contiene datos de telemetría y firmas de fase",
            "&7de un cuerpo celeste recién descubierto.",
            "",
            "&eClic derecho para analizar y recibir XP."
    );

    static {
        // Darle un brillo encantador al registro y celdas
        addGlow(CORONALIS_RECORD_DISCOVERED);
        addGlow(CORONALIS_DATA_CELL);
    }

    private static void addGlow(@Nonnull SlimefunItemStack item) {
        item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }
}
