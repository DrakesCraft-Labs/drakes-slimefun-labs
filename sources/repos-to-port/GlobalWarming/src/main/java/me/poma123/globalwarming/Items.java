/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
 *  com.github.drakescraft_labs.slimefun4.core.attributes.MachineTier
 *  com.github.drakescraft_labs.slimefun4.core.attributes.MachineType
 *  com.github.drakescraft_labs.slimefun4.utils.LoreBuilder
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.PotionMeta
 */
package me.poma123.globalwarming;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.core.attributes.MachineTier;
import com.github.drakescraft_labs.slimefun4.core.attributes.MachineType;
import com.github.drakescraft_labs.slimefun4.utils.LoreBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

public final class Items {
    public static final SlimefunItemStack THERMOMETER = new SlimefunItemStack("THERMOMETER", "24fa511f2628d56a8c8691ac5df3e3f82716384514a5ea5bae3eda86f48ad6e1", "&eTerm\u00f3metro", new String[]{"", "&7Muestra la temperatura donde est\u00e1s", "", "&eClic derecho&7 para cambiar de unidad"});
    public static final SlimefunItemStack AIR_QUALITY_METER = new SlimefunItemStack("AIR_QUALITY_METER", "179adc3d2dfda05497bb904bd6651922510ce2139a71c10eae3b27565292ebf0", "&bMedidor de Calidad del Aire", new String[]{"", "&7Muestra c\u00f3mo cambia la temperatura donde est\u00e1s", "", "&eClic derecho&7 para cambiar de unidad"});
    public static final SlimefunItemStack AIR_COMPRESSOR = new SlimefunItemStack("AIR_COMPRESSOR", Material.DISPENSER, "&bCompresor de Aire", new String[]{"", "&aComprime di\u00f3xido de carbono", "", LoreBuilder.machine((MachineTier)MachineTier.ADVANCED, (MachineType)MachineType.MACHINE), LoreBuilder.powerBuffer((int)512), LoreBuilder.powerPerSecond((int)16)});
    public static final SlimefunItemStack EMPTY_CANISTER = new SlimefunItemStack("EMPTY_CANISTER", Material.GLASS_BOTTLE, "&7Bombona de Aire", new String[0]);
    public static final SlimefunItemStack CO2_CANISTER;
    public static final SlimefunItemStack CINNABARITE;
    public static final SlimefunItemStack MERCURY;
    public static final SlimefunItemStack FILTER;
    public static final SlimefunItemStack CLIMATIZADOR;
    public static final SlimefunItemStack SUMIDERO_CARBONO;

    static {
        CINNABARITE = new SlimefunItemStack("CINNABARITE", "d67a8a3d7d5aa5db00dff5c82f846ea0aeb7d645f0e467d7e9d9a18e9fa5b012", "&cCinabrio", new String[0]);
        MERCURY = new SlimefunItemStack("MERCURY", Material.GRAY_DYE, "&7Mercurio", new String[0]);
        FILTER = new SlimefunItemStack("AIR_COMPRESSOR_FILTER", Material.GUNPOWDER, "&7Filtro", new String[0]);
        CLIMATIZADOR = new SlimefunItemStack("GW_CLIMATIZADOR", Material.BLUE_ICE, "&bClimatizador", new String[]{"", "&7Mantiene su propio clima alrededor", "&7sin importar lo que pase fuera", "", "&aCubre protecciones e islas completas", "&ao un radio configurable sin proteccion", "&ay la temperatura es la que le pongas", "", "&8&oRadio y temperatura se ajustan en Items.yml", "", LoreBuilder.machine((MachineTier)MachineTier.ADVANCED, (MachineType)MachineType.MACHINE), LoreBuilder.powerBuffer((int)512), LoreBuilder.powerPerSecond((int)24)});
        SUMIDERO_CARBONO = new SlimefunItemStack("GW_SUMIDERO_CARBONO", Material.DEEPSLATE_TILES, "&2Sumidero de Carbono", new String[]{"", "&7Fija bajo tierra el CO\u2082 capturado", "&7y baja la contaminaci\u00f3n del mundo", "", "&aConsume Bombonas de CO\u2082", "&ay devuelve la bombona vac\u00eda", "", LoreBuilder.machine((MachineTier)MachineTier.ADVANCED, (MachineType)MachineType.MACHINE), LoreBuilder.powerBuffer((int)512), LoreBuilder.powerPerSecond((int)32)});
        ItemStack item = new ItemStack(Material.POTION);
        ItemMeta meta = item.getItemMeta();
        ((PotionMeta)meta).setColor(Color.fromRGB((int)61, (int)61, (int)61));
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP});
        item.setItemMeta(meta);
        CO2_CANISTER = new SlimefunItemStack("CO2_CANISTER", item, "&7Bombona de CO\u2082", new String[]{"", "&8&oDi\u00f3xido de carbono comprimido"});
    }
}

