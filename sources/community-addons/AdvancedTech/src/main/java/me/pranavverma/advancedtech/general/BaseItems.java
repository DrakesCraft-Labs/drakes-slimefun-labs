package me.pranavverma.advancedtech.general;

import com.github.drakescraft_labs.slimefun4.core.attributes.MachineTier;
import com.github.drakescraft_labs.slimefun4.core.attributes.MachineType;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactivity;
import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;
import lombok.experimental.UtilityClass;

import org.bukkit.Material;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.utils.LoreBuilder;



@UtilityClass
public final class BaseItems {

    public static int advanced_solar_gen_power_day = 2056;
    public static int advanced_solar_gen_power_night = 1080;
    
    public static final SlimefunItemStack command_engine_ = new SlimefunItemStack("COMMAND_HUB_ENGINE", PlayerHead.getItemStack(PlayerSkin.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2NmMTZiYmZlYzZlMGU1YTU4YTk5YjEwYzUyYWNkMDgzZTJhYzg2ZTAwMTNlMDM3ZDZmOTA2MjQzYjYzYTI3OSJ9fX0=")), "&cCommand Hub Engine", "" + "&r&lEngine For Command Hub", LoreBuilder.radioactive(Radioactivity.MODERATE), LoreBuilder.HAZMAT_SUIT_REQUIRED);
    public static final SlimefunItemStack command_hub_ = new SlimefunItemStack("COMMAND_HUB", PlayerHead.getItemStack(PlayerSkin.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM4ZjA4MzljMmRiNDlmZGI1ZmM0NTUxNmY3ZTBlMWY2MzE1MTY4OGEwMjY3ZDBkZmQxM2Y4MTYyMWU2ZDU0NCJ9fX0=")), "&eCommand Hub", "" + "&r&l&cMain Central Command Hub", LoreBuilder.radioactive(Radioactivity.LOW));
    public static final SlimefunItemStack boosted_carbonado_ = new SlimefunItemStack("BOOSTED_CARBONADO", PlayerHead.getItemStack(PlayerSkin.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4ZjFhNzJlYjUyYzdkNjgxZjc5NTk5YzNiZjdlYTkxMmY4YzhhZmM4ZWU5NTRiZTMzZWFhNmJiNGNlNGZiIn19fQ==")), "&dBoosted Carbonado","" + "&r&lBlack Diamond V2.0");
    public static final SlimefunItemStack framed_uranium_ = new SlimefunItemStack("FRAMED_URANIUM", PlayerHead.getItemStack(PlayerSkin.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBjMGM5Nzg5ZGNmYTk5ZDZjMDRiOTE2MTc3MTEwYjNmOWEwNzdjOTY0ZTVjYTBhMGJmODZkNTdkYmU1OTNiYSJ9fX0=")), "&dFramed Uranium","" + "&r&lHypercharged Uranium", LoreBuilder.radioactive(Radioactivity.VERY_DEADLY), LoreBuilder.HAZMAT_SUIT_REQUIRED);

    public static final SlimefunItemStack fire_cake = new SlimefunItemStack("FIRE_CAKE", Material.CAKE, "&aFire Cake", "Be Careful...", LoreBuilder.radioactive(Radioactivity.HIGH));
    public static final SlimefunItemStack handheld_digger_1_ = new SlimefunItemStack("HANDHELD_DIGGER_1", Material.NETHERITE_PICKAXE, "&4Basic Handheld Power Digger", "&cAllows you to mine a 4 x 4 Area.");
    public static final SlimefunItemStack handheld_digger_2_ = new SlimefunItemStack("HANDHELD_DIGGER_2", Material.NETHERITE_PICKAXE, "&eAdvanced Handheld Power Digger", "&cAllows you to mine a 5 x 5 Area.");
    public static final SlimefunItemStack handheld_digger_3_ = new SlimefunItemStack("HANDHELD_DIGGER_3", Material.NETHERITE_PICKAXE, "&dCarbonado Handheld Power Digger", "&cAllows you to mine a 7 x 7 Area.");

    public static final SlimefunItemStack advanced_solar_gen = new SlimefunItemStack("ADVANCED_SOLAR_GEN", PlayerHead.getItemStack(PlayerSkin.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZkZDllNTg4ZDI0NjFkMmQzZDA1OGNiM2UwYWYyYjNhMzM2NzYwN2FhMTRkMTI0ZWQ5MmE4MzNmMjVmYjExMiJ9fX0=")), "&aAdvanced Solar Generator", "&cBasics of AdvancedTech.", "&bThis Has to be Connected Through the Command Hub!", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR), LoreBuilder.powerBuffer(0), LoreBuilder.powerPerSecond(advanced_solar_gen_power_day * 2));
}
