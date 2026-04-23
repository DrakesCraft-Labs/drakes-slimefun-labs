package me.voper.slimeframe.implementation.researches;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.researches.Research;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.core.managers.SettingsManager;
import me.voper.slimeframe.implementation.SFrameItems;
import me.voper.slimeframe.implementation.SFrameStacks;
import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.resources.SpecialOre;
import me.voper.slimeframe.utils.Keys;

@ParametersAreNonnullByDefault
public final class Researches {

    private static final int FIRST_RESEARCH_ID = 131300000;
    private static final SettingsManager sm = SlimeFrame.getSettingsManager();

    public static void setup() {
        if (!sm.getBoolean(SettingsManager.ConfigField.RESEARCHES_ENABLED)) return;

        create(Keys.ORES_RESEARCH, 1, "SlimeFrame ores", 50, SpecialOre.SOURCE_ORE_MAP.values().toArray(SpecialOre[]::new));
        create(Keys.ALLOYS_RESEARCH, 2, "Unveiling the secrets of rare Alloys", 50, SFrameItems.ALLOYS.toArray(SlimefunItem[]::new));

        create(Keys.NOSAM_RESEARCH, 3, "Nosam Pickaxe", 30, SFrameStacks.NOSAM_PICK);
        create(Keys.FOCUSED_NOSAM_RESEARCH, 4, "Sharpening your knowledge to wield the Focused Nosam Pickaxe with precision", 60, SFrameStacks.FOCUSED_NOSAM_PICK);
        create(Keys.PRIME_NOSAM_RESEARCH, 5, "Attaining mastery over the Prime Nosam Pickaxe", 90, SFrameStacks.PRIME_NOSAM_PICK);

        create(Keys.MACHINES_RESEARCH, 6, "Unearthing the essence of fundamental machines to expand your technological arsenal", 30, Groups.MACHINES.getItems().stream()
                .filter(item -> !item.getItemName().contains("Prime ") && !item.getItemName().contains("Advanced "))
                .toList());

        create(Keys.ADV_MACHINES_RESEARCH, 7, "Delving into advanced machinery", 50, Groups.MACHINES.getItems().stream()
                .filter(item -> item.getItemName().contains("Advanced "))
                .toList());

        create(Keys.PRIME_MACHINES_RESEARCH, 8, "Ascending to the pinnacle of technological prowess with Prime-tier machines", 60, Groups.MACHINES.getItems().stream()
                .filter(item -> item.getItemName().contains("Prime "))
                .toList());

        create(Keys.CRYO_SUIT_RESEARCH, 9, "Unleashing the potential of Cryo technology", 50, SFrameStacks.CRYO_BOOTS, SFrameStacks.CRYO_CHESTPLATE, SFrameStacks.CRYO_HELMET, SFrameStacks.CRYO_LEGGINGS);
        create(Keys.ALLOY_PLATES_RESEARCH, 10, "Studying the intricacies of Alloy Plates, vital components in advanced equipment", 30, SFrameItems.ALLOY_PLATES_MAP.values().toArray(new SlimefunItemStack[0]));

        create(Keys.ENERGY_GEN_RESEARCH, 11, "Tapping into the power of advanced Energy Generators", 40,
                SFrameStacks.GRAVITECH_ENERCELL, SFrameStacks.ARCANE_FLUX_DYNAMO, SFrameStacks.SPECTRA_REACTOR,
                SFrameStacks.PRISMA_POWER_CORE, SFrameStacks.VOIDFORGE_CELESTIUM_GENERATOR, SFrameStacks.AXIOM_ENERGENESIS_ENGINE,
                SFrameStacks.CHRONOS_INFINITY_DYNAMO, SFrameStacks.PRIMORDIAL_ETERNACORE_REACTOR, SFrameStacks.VOIDLIGHT_FUSION_GENERATOR);

        create(Keys.ASTRAL_GEN_RESEARCH, 12, "Achieving celestial heights with the Astral Prime Generator, a testament to energy mastery", 50, SFrameStacks.ASTRAL_PRIME_GENERATOR);
        create(Keys.MULTIBLOCKS_RESEARCH, 13, "Comprehending the complexities of SlimeFrame Multiblocks", 30, SFrameStacks.FOUNDRY);
        create(Keys.CONDENSED_PLATE_RESEARCH, 14, "Unlocking the secrets of Condensed Plates", 40, SFrameStacks.CONDENSED_PLATE);

        create(Keys.GENERAL_RESOURCES_RESEARCH, 15, "General SlimeFrame resources", 25,
                SFrameStacks.CRYOTIC, SFrameStacks.COOLANT_CANISTER, SFrameStacks.TELLURIUM_FRAGMENT, SFrameStacks.TELLURIUM,
                SFrameStacks.DILUTED_THERMIA, SFrameStacks.RUBEDO, SFrameStacks.ARGON_CRYSTAL, SFrameStacks.CUBIC_DIODES,
                SFrameStacks.PLASTIDS, SFrameStacks.CONTROL_MODULE, SFrameStacks.GALLIUM, SFrameStacks.SALVAGE,
                SFrameStacks.PRISMATIC_ENERGIZED_CORE, SFrameStacks.MORPHICS, SFrameStacks.NEURAL_SENSORS, SFrameStacks.NEURODES,
                SFrameStacks.OROKIN_CELL);

    }

    private static void create(NamespacedKey key, int id, String name, int cost, ItemStack... itemStacks) {
        new Research(key, FIRST_RESEARCH_ID + id, name, cost).addItems(itemStacks).register();
    }

    private static void create(NamespacedKey key, int id, String name, int cost, SlimefunItem... items) {
        Research research = new Research(key, FIRST_RESEARCH_ID + id, name, cost);
        research.addItems(items);
        research.register();
    }

    private static void create(NamespacedKey key, int id, String name, int cost, List<SlimefunItem> items) {
        create(key, id, name, cost, items.toArray(new SlimefunItem[0]));
    }

}
