package me.vaan.mapjammer.implementation;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.utils.LoreBuilder;
import me.vaan.mapjammer.util.ConfigStorage;
import org.bukkit.Material;

public class Items {
    public static SlimefunItemStack JAMMER = new SlimefunItemStack("MAP_JAMMER", Material.COMPASS, "&cMap Jammer", "", "&7Holding this into your inventory allows you", "&7to be hidden from the map", "", LoreBuilder.power((int) ConfigStorage.COST, "/" + ConfigStorage.TIME_FRAME + "s"), LoreBuilder.powerCharged(0, 120));
    public static SlimefunItemStack JAMMER_2 = new SlimefunItemStack("MAP_JAMMER_2", Material.COMPASS, "&cMap Jammer", "", "&7Holding this into your inventory allows you", "&7to be hidden from the map", "", LoreBuilder.power((int) ConfigStorage.COST, "/" + ConfigStorage.TIME_FRAME + "s"), LoreBuilder.powerCharged(0, 360));
    public static SlimefunItemStack JAMMER_3 = new SlimefunItemStack("MAP_JAMMER_3", Material.COMPASS, "&cMap Jammer", "", "&7Holding this into your inventory allows you", "&7to be hidden from the map", "", LoreBuilder.power((int) ConfigStorage.COST, "/" + ConfigStorage.TIME_FRAME + "s"), LoreBuilder.powerCharged(0, 1440));

}
