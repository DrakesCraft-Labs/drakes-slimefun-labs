package me.voper.slimeframe.implementation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.cryptomorin.xseries.XMaterial;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.core.attributes.MachineTier;
import com.github.drakescraft-labs.slimefun4.core.attributes.MachineType;
import com.github.drakescraft-labs.slimefun4.core.attributes.Radioactivity;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft-labs.slimefun4.utils.LoreBuilder;

import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.components.PrimeComponents;
import me.voper.slimeframe.implementation.items.components.UtilsComponents;
import me.voper.slimeframe.implementation.items.relics.Relic;
import me.voper.slimeframe.implementation.items.relics.RelicItemStack;
import me.voper.slimeframe.utils.Colors;
import me.voper.slimeframe.utils.HeadTextures;
import me.voper.slimeframe.utils.Lore;
import me.voper.slimeframe.utils.RandomItemStacks;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

@UtilityClass
public final class SFrameStacks {

    public static final SFrameTheme RESOURCES_THEME = new SFrameTheme(ChatColor.AQUA, Groups.RESOURCES_NAME);
    public static final SFrameTheme MACHINES_THEME = new SFrameTheme(ChatColor.AQUA, Groups.MACHINES_NAME);
    public static final SFrameTheme GENERATORS_THEME = new SFrameTheme(ChatColor.AQUA, Groups.GENERATORS_NAME);
    public static final SFrameTheme UTILS_TOOLS_THEME = new SFrameTheme(ChatColor.AQUA, Groups.UTILS_AND_TOOLS_NAME);
    public static final SFrameTheme GEAR_THEME = new SFrameTheme(ChatColor.AQUA, Groups.GEAR_NAME);
    public static final SFrameTheme PRIME_COMPONENTS_THEME = new SFrameTheme(ChatColor.GOLD, Groups.PRIME_COMPONENTS_NAME);

    @Nonnull
    public static ItemStack enchantedItem(@Nonnull Material m) {
        return new CustomItemStack(m, (meta) -> {
            meta.addEnchant(Enchantment.LUCK, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });
    }

    @Nonnull
    private static ItemStack createPotion(@Nonnull PotionType type, @Nonnull Material recipient) {
        ItemStack potion = new ItemStack(recipient);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta != null) {
            meta.setBasePotionData(new PotionData(type));
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            potion.setItemMeta(meta);
        }
        return potion;
    }

    @Nonnull
    public static ItemStack createLingeringPotion(@Nonnull PotionType type) {
        return createPotion(type, Material.LINGERING_POTION);
    }

    @Nonnull
    public static ItemStack createPotion(@Nonnull PotionType type) {
        return createPotion(type, Material.POTION);
    }

    public static final RandomItemStacks<RelicItemStack> RANDOM_LITH_RELICS = new RandomItemStacks<>();
    public static final RandomItemStacks<RelicItemStack> RANDOM_MESO_RELICS = new RandomItemStacks<>();
    public static final RandomItemStacks<RelicItemStack> RANDOM_NEO_RELICS = new RandomItemStacks<>();
    public static final RandomItemStacks<RelicItemStack> RANDOM_AXI_RELICS = new RandomItemStacks<>();

    // <editor-fold defaultstate="collapsed" desc="Utils and Tools">
    public static final SlimefunItemStack NOSAM_PICK = SFrameTheme.sfStackFromTheme(
            "WF_NOSAM_PICK",
            Material.IRON_PICKAXE,
            UTILS_TOOLS_THEME.withNameColor(Colors.BRONZE),
            "Nosam Pickaxe",
            "A special pickaxe capable of extracting",
            " new resources from rocks and ores"
    );

    public static final SlimefunItemStack FOCUSED_NOSAM_PICK = SFrameTheme.sfStackFromTheme(
            "WF_FOC_NOSAM_PICK",
            Material.DIAMOND_PICKAXE,
            UTILS_TOOLS_THEME.withNameColor(Colors.SILVER),
            "Focused Nosam Pickaxe",
            "An improved version of the Nosam Pickaxe with",
            "higher chances of extracting rare resources"
    );

    public static final SlimefunItemStack PRIME_NOSAM_PICK = SFrameTheme.sfStackFromTheme(
            "WF_PRIME_NOSAM_PICK",
            Material.NETHERITE_PICKAXE,
            UTILS_TOOLS_THEME.withNameColor(Colors.GOLD_2),
            "Prime Nosam Pickaxe",
            "The best version of the Nosam Pickaxe,",
            "able to regularly get rare resources"
    );

    public static final SlimefunItemStack MERCHANT_SOUL_CONTRACT = SFrameTheme.sfStackFromTheme(
            "WF_SOUL_CONTRACT",
            Material.PAPER,
            UTILS_TOOLS_THEME.withNameColor(ChatColor.DARK_PURPLE),
            "Merchant Soul Contract",
            ChatColor.GREEN + "Right click " + ChatColor.WHITE + "on a trader to make",
            ChatColor.WHITE + "a contract with its trades",
            "",
            ChatColor.DARK_RED + "WARNING: " + Colors.ORANGE + " It will kill the trader"
    );

    public static final SlimefunItemStack OROKIN_WAND = SFrameTheme.sfStackFromTheme(
            "WF_OROKIN_WAND",
            Material.STICK,
            UTILS_TOOLS_THEME,
            "Orokin Wand",
            "This wand has the ability to",
            "collect some special blocks such as",
            "reinforced deepslates and ",
            "budding amethysts",
            "",
            Lore.usesLeft(64)
    );

    public static final SlimefunItemStack PRIME_OROKIN_WAND = SFrameTheme.sfStackFromTheme(
            "WF_PRIME_OROKIN_WAND",
            Material.BLAZE_ROD,
            UTILS_TOOLS_THEME.withNameColor(ChatColor.YELLOW),
            "Prime Orokin Wand",
            "An improved version of the Orokin Wand",
            "",
            Lore.usesLeft(1024)
    );

    public static final SlimefunItemStack INPUT_CONFIGURATOR = SFrameTheme.sfStackFromTheme(
            "WF_INPUT_CONFIGURATOR",
            Material.BLAZE_ROD,
            UTILS_TOOLS_THEME.withNameColor(ChatColor.DARK_PURPLE),
            "Input Configurator",
            "Transfer the input items from one ",
            "machine to another machine ",
            "of the same type",
            "",
            ChatColor.GREEN + "Right Click " + UTILS_TOOLS_THEME.getLoreColor() + "to copy the input",
            ChatColor.DARK_GREEN + "Shift " + ChatColor.GREEN + "Right Click " + UTILS_TOOLS_THEME.getLoreColor() + "to paste the configuration"
    );

    public static final SlimefunItemStack SELECTOR_CONFIGURATOR = SFrameTheme.sfStackFromTheme(
            "WF_SELECTOR_CONFIG",
            enchantedItem(Material.BLAZE_ROD),
            UTILS_TOOLS_THEME.withNameColor(Colors.FERRARI_RED),
            "Selector Configurator",
            "Copy the selected item in one",
            "machine and paste it in another one",
            "of the same type",
            "",
            ChatColor.GREEN + "Right Click " + UTILS_TOOLS_THEME.getLoreColor() + "to copy the selection",
            ChatColor.DARK_GREEN + "Shift " + ChatColor.GREEN + "Right Click " + UTILS_TOOLS_THEME.getLoreColor() + "to paste the selection"
    );

    public static final SlimefunItemStack ITEM_PROJECTOR = SFrameTheme.sfStackFromTheme(
            "WF_ITEM_PROJECTOR",
            Material.BLACKSTONE_SLAB,
            UTILS_TOOLS_THEME,
            "Item Projector",
            "This device made by the Corpus",
            "will display any item inside it",
            "as a beautiful hologram"
    );

    public static final SlimefunItemStack ENERGY_CENTRAL = SFrameTheme.sfStackFromTheme(
            "WF_ENERGY_CENTRAL",
            Material.BEACON,
            UTILS_TOOLS_THEME.withNameColor(ChatColor.YELLOW),
            "Energy Central",
            "A device that shows information",
            "about your energy network, such as",
            "energy produced, total capacity and",
            "the count of consumers,",
            "generators and capacitors"
    );
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Gear">
    private static final List<String> CRYOGENIC_SUIT_LORE = new ArrayList<>();

    static {
        CRYOGENIC_SUIT_LORE.add("");
        CRYOGENIC_SUIT_LORE.add(ChatColor.GOLD + "Full set effects:");
        CRYOGENIC_SUIT_LORE.add(ChatColor.YELLOW + "- Radiation immunity");
        CRYOGENIC_SUIT_LORE.add(ChatColor.YELLOW + "- Bee Sting protection");
        CRYOGENIC_SUIT_LORE.add(ChatColor.BLUE + "- Freezing immunity");
        CRYOGENIC_SUIT_LORE.add("");
        CRYOGENIC_SUIT_LORE.add(Groups.GEAR_NAME);
    }

    public static final SlimefunItemStack CRYO_HELMET = new SlimefunItemStack("WF_CRYO_HELMET", Material.LEATHER_HELMET, Color.BLUE, "&9Cryogenic Helmet", CRYOGENIC_SUIT_LORE.toArray(new String[0]));
    public static final SlimefunItemStack CRYO_CHESTPLATE = new SlimefunItemStack("WF_CRYO_CHESTP", Material.LEATHER_CHESTPLATE, Color.BLUE, "&9Cryogenic Chestplate", CRYOGENIC_SUIT_LORE.toArray(new String[0]));
    public static final SlimefunItemStack CRYO_LEGGINGS = new SlimefunItemStack("WF_CRYO_LEGGINGS", Material.LEATHER_LEGGINGS, Color.BLUE, "&9Cryogenic Leggings", CRYOGENIC_SUIT_LORE.toArray(new String[0]));
    public static final SlimefunItemStack CRYO_BOOTS = new SlimefunItemStack("WF_CRYO_BOOTS", Material.LEATHER_BOOTS, Color.BLUE, "&9Cryogenic Boots", CRYOGENIC_SUIT_LORE.toArray(new String[0]));
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Resources">
    public static final SlimefunItemStack PYROL = SFrameTheme.sfStackFromTheme(
            "WF_PYROL",
            Material.QUARTZ,
            RESOURCES_THEME,
            "Pyrol",
            "Obtained from end stones",
            "",
            ChatColor.of("#dff5e5") + "Rarity: &2Common"
    );

    public static final SlimefunItemStack TRAVORIDE = SFrameTheme.sfStackFromTheme(
            "WF_TRAVORIDE",
            Material.LAPIS_LAZULI,
            RESOURCES_THEME.withNameColor(ChatColor.BLUE),
            "Travoride",
            "Obtained from" + ChatColor.GRAY + " stones",
            "",
            ChatColor.of("#dff5e5") + "Rarity: &2Common"
    );

    public static final SlimefunItemStack ADRAMALIUM = SFrameTheme.sfStackFromTheme(
            "WF_ADRAMALIUM",
            Material.NETHER_BRICK,
            RESOURCES_THEME.withNameColor(ChatColor.RED),
            "Adramalium",
            "Obtained from" + ChatColor.RED + " netherracks",
            "",
            ChatColor.of("#dff5e5") + "Rarity: &2Common"
    );

    public static final SlimefunItemStack FERROS = SFrameTheme.sfStackFromTheme(
            "WF_FERROS",
            Material.RAW_IRON,
            RESOURCES_THEME.withNameColor(ChatColor.GRAY),
            "Ferros",
            "Obtained from deepslate",
            "and regular" + ChatColor.GRAY + " iron ores",
            "",
            ChatColor.of("#dff5e5") + "Rarity: &1Uncommon"
    );

    public static final SlimefunItemStack VENEROL = SFrameTheme.sfStackFromTheme(
            "WF_VENEROL",
            Material.BLUE_DYE,
            RESOURCES_THEME.withNameColor(ChatColor.BLUE),
            "Venerol",
            "Obtained from deepslate",
            "and regular" + ChatColor.BLUE + " lapis ores",
            "",
            ChatColor.of("#dff5e5") + "Rarity: &1Uncommon."
    );

    public static final SlimefunItemStack NAMALON = SFrameTheme.sfStackFromTheme(
            "WF_NAMALON",
            Material.BROWN_DYE,
            RESOURCES_THEME.withNameColor(ChatColor.DARK_RED),
            "Namalon",
            "Obtained from nether " + ChatColor.BOLD + "&fquartz ores",
            "",
            ChatColor.of("#dff5e5") + "Rarity: &1Uncommon"
    );

    public static final SlimefunItemStack AURON = SFrameTheme.sfStackFromTheme(
            "WF_AURON",
            Material.GOLD_NUGGET,
            RESOURCES_THEME.withNameColor(ChatColor.YELLOW),
            "Auron",
            "Obtained from deepslate",
            "and regular" + ChatColor.YELLOW + " gold ores",
            "",
            ChatColor.of("#dff5e5") + "Rarity: &dRare"
    );

    public static final SlimefunItemStack HESPERON = SFrameTheme.sfStackFromTheme(
            "WF_HESPERON",
            Material.AMETHYST_SHARD,
            RESOURCES_THEME.withNameColor(ChatColor.DARK_PURPLE),
            "Hesperon",
            "Obtained from deepslate",
            "and regular" + ChatColor.RED + " redstone ores",
            "",
            ChatColor.of("#dff5e5") + "Rarity: &dRare"
    );

    public static final SlimefunItemStack THAUMICA = SFrameTheme.sfStackFromTheme(
            "WF_THAUMICA",
            Material.RAW_GOLD,
            RESOURCES_THEME.withNameColor(ChatColor.YELLOW),
            "Thaumica",
            "Obtained from" + ChatColor.YELLOW + " nether gold ores",
            "",
            ChatColor.of("#dff5e5") + "Rarity: &dRare"
    );

    public static final SlimefunItemStack CRYOTIC = SFrameTheme.sfStackFromTheme(
            "WF_CRYOTIC",
            Material.DIAMOND,
            RESOURCES_THEME.withNameColor(ChatColor.BLUE),
            "Cryotic",
            "Found in extreme sub-zero environments",
            Colors.ORANGE + "Warning: " + RESOURCES_THEME.getLoreColor() + "It can instantly freezes you",
            "",
            "&8⇨ &4Cryogenic Suit required!"
    );

    public static final SlimefunItemStack CUBIC_DIODES = SFrameTheme.sfStackFromTheme(
            "WF_CUBIC_DIODES",
            HeadTextures.CUBIC_DIODES,
            RESOURCES_THEME,
            "Cubic Diodes",
            "Vital components used in the creation",
            "of Corpus photon-networks"
    );

    public static final SlimefunItemStack PLASTIDS = SFrameTheme.sfStackFromTheme(
            "WF_PLASTIDS",
            XMaterial.PITCHER_POD.parseMaterial() == null ? Material.BROWN_DYE : XMaterial.PITCHER_POD.parseMaterial(),
            RESOURCES_THEME,
            Colors.BROWN + "Plastids",
            "A disgusting nanite-infested tissue mass"
    );

    public static final SlimefunItemStack RUBEDO = SFrameTheme.sfStackFromTheme(
            "WF_RUBEDO",
            enchantedItem(Material.REDSTONE),
            RESOURCES_THEME.withNameColor(ChatColor.RED),
            "Rubedo",
            "A crystalline ore used in various recipes",
            "be careful, it may be poisonous",
            LoreBuilder.radioactive(Radioactivity.MODERATE)
    );

    public static final SlimefunItemStack ARGON_CRYSTAL = SFrameTheme.sfStackFromTheme(
            "WF_ARGON_CRYSTAL",
            Material.END_CRYSTAL,
            RESOURCES_THEME.withNameColor(ChatColor.DARK_PURPLE),
            "Argon Crystal",
            "Void based radioactive resource"
    );

    public static final SlimefunItemStack CONTROL_MODULE = SFrameTheme.sfStackFromTheme(
            "WF_CONTROL_MODULE",
            Material.DETECTOR_RAIL,
            RESOURCES_THEME,
            "Control Module",
            "Autonomy processor for Robotics",
            "A Corpus design"
    );

    public static final SlimefunItemStack GALLIUM = SFrameTheme.sfStackFromTheme(
            "WF_GALLIUM",
            Material.IRON_INGOT,
            RESOURCES_THEME,
            "Gallium",
            "Soft metal used in microelectronics",
            "and energy components",
            "Can be obtained by recycling",
            "advanced circuit boards"
    );

    public static final SlimefunItemStack MORPHICS = SFrameTheme.sfStackFromTheme(
            "WF_MORPHICS",
            Material.BONE_MEAL,
            RESOURCES_THEME,
            "Morphics",
            "An amorphous solid",
            "Possibly Orokin technology"
    );

    public static final SlimefunItemStack NEURAL_SENSORS = SFrameTheme.sfStackFromTheme(
            "WF_NEURAL_SENSORS",
            Material.FERMENTED_SPIDER_EYE,
            RESOURCES_THEME,
            "Neural Sensors",
            "Implanted neural-link for controlling augmentations",
            "Grineer design"
    );

    public static final SlimefunItemStack NEURODES = SFrameTheme.sfStackFromTheme(
            "WF_NEURODES",
            Material.SPIDER_EYE,
            RESOURCES_THEME,
            "Neurodes",
            "Biotech sensor organ",
            "harvested from Infested entities"
    );

    public static final SlimefunItemStack OROKIN_CELL = SFrameTheme.sfStackFromTheme(
            "WF_OROKIN_CELL",
            Material.TURTLE_EGG,
            RESOURCES_THEME,
            "Orokin Cell",
            "Ancient energy cell from the Orokin era"
    );

    public static final SlimefunItemStack TELLURIUM_FRAGMENT = SFrameTheme.sfStackFromTheme(
            "WF_TELLURIUM_FRAGMENT",
            new CustomItemStack(Material.DISC_FRAGMENT_5, (meta) -> meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)),
            RESOURCES_THEME.withNameColor(ChatColor.LIGHT_PURPLE),
            "Tellurium Fragment",
            "A synthesized fragment of Tellurium"
    );

    public static final SlimefunItemStack TELLURIUM = SFrameTheme.sfStackFromTheme(
            "WF_TELLURIUM",
            Material.ECHO_SHARD,
            RESOURCES_THEME.withNameColor(ChatColor.DARK_PURPLE),
            "Tellurium",
            "A rare metal originating from",
            "another solar system",
            "It can be synthesized"
    );

    public static final SlimefunItemStack BOOSTED_TELLURIUM = SFrameTheme.sfStackFromTheme(
            "WF_BOOSTED_TELLURIUM",
            enchantedItem(Material.ECHO_SHARD),
            RESOURCES_THEME.withNameColor(Colors.BLUE_VIOLET),
            "Boosted Tellurium",
            "Enhanced Tellurium used to",
            "craft some machines",
            "",
            LoreBuilder.radioactive(Radioactivity.VERY_DEADLY)
    );

    public static final SlimefunItemStack COOLANT_CANISTER = SFrameTheme.sfStackFromTheme(
            "WF_COOLANT_CANISTER",
            createPotion(PotionType.NIGHT_VISION),
            RESOURCES_THEME,
            "Coolant Canister",
            "Required to seal Thermia Fractures",
            "obtained from " + ChatColor.BLUE + "Coolant Raknoids"
    );

    public static final SlimefunItemStack DILUTED_THERMIA = SFrameTheme.sfStackFromTheme(
            "WF_DILUTED_THERMIA",
            createLingeringPotion(PotionType.FIRE_RESISTANCE),
            RESOURCES_THEME.withNameColor(ChatColor.YELLOW),
            "Diluted Thermia",
            "A magma like liquid that is diluted with",
            "coolant during the extraction process"
    );

    public static final SlimefunItemStack SALVAGE = SFrameTheme.sfStackFromTheme(
            "WF_SALVAGE",
            Material.PRISMARINE_CRYSTALS,
            RESOURCES_THEME,
            "Salvage",
            "High value metals collected",
            "by recycling machines"
    );

    public static final SlimefunItemStack PRISMATIC_ENERGIZED_CORE = SFrameTheme.sfStackFromTheme(
            "WF_PRISMATIC_ENERGIZED_CORE",
            Material.DIAMOND_BLOCK,
            RESOURCES_THEME,
            "Prismatic Energized Core",
            "The Prismatic Energized Core is a unique",
            "energy matrix infused with the essence",
            "of the Void and celestial energy"
    );

    public static final SlimefunItemStack CONDENSED_PLATE = SFrameTheme.sfStackFromTheme(
            "WF_CONDENSED_PLATE",
            Material.PAPER,
            RESOURCES_THEME.withNameColor(ChatColor.DARK_RED),
            "Condensed Plate",
            "All the plates condensed in one"
    );
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Alloys">
    public static final SlimefunItemStack PYROTIC_ALLOY = SFrameTheme.sfStackFromTheme(
            "WF_PYROTIC_ALLOY",
            Material.IRON_INGOT,
            RESOURCES_THEME.withNameColor(ChatColor.WHITE),
            "Pyrotic Alloy",
            "A strong metal made from pyrol"
    );

    public static final SlimefunItemStack ADRAMAL_ALLOY = SFrameTheme.sfStackFromTheme(
            "WF_ADRAMAL_ALLOY",
            Material.COPPER_INGOT,
            RESOURCES_THEME.withNameColor(Colors.BROWN),
            "Adramal Alloy",
            "Purged of spores and contaminants,",
            "this alloy is highly versatile"
    );

    public static final SlimefunItemStack AUROXIUM_ALLOY = SFrameTheme.sfStackFromTheme(
            "WF_AUROXIUM_ALLOY",
            Material.GOLD_INGOT,
            RESOURCES_THEME.withNameColor(ChatColor.YELLOW),
            "Auroxium Alloy",
            "A compound formed from Auron"
    );

    public static final SlimefunItemStack FERSTEEL_ALLOY = SFrameTheme.sfStackFromTheme(
            "WF_FERSTEEL_ALLOY",
            Material.IRON_INGOT,
            RESOURCES_THEME.withNameColor(ChatColor.GRAY),
            "Fersteel Alloy",
            "A compound formed from Ferros"
    );

    public static final SlimefunItemStack HESPAZYM_ALLOY = SFrameTheme.sfStackFromTheme(
            "WF_HESPAZYM_ALLOY",
            Material.IRON_INGOT,
            RESOURCES_THEME.withNameColor(ChatColor.WHITE),
            "Hespazym Alloy",
            "A compound formed from Hesperon"
    );

    public static final SlimefunItemStack TRAVOCYTE_ALLOY = SFrameTheme.sfStackFromTheme(
            "WF_TRAVOCYTE_ALLOY",
            Material.IRON_INGOT,
            RESOURCES_THEME,
            "Travocyte Alloy",
            "A compound formed from Travoride"
    );

    public static final SlimefunItemStack VENERDO_ALLOY = SFrameTheme.sfStackFromTheme(
            "WF_VENERDO_ALLOY",
            Material.NETHERITE_INGOT,
            RESOURCES_THEME,
            "Venerdo Alloy",
            "A compound formed from Venerol"
    );

    public static final SlimefunItemStack THAUMIC_DISTILLATE = SFrameTheme.sfStackFromTheme(
            "WF_THAUMIC_DISTILLATE",
            Material.NETHERITE_SCRAP,
            RESOURCES_THEME.withNameColor(Colors.DARK_BROWN),
            "Thaumic Distillate",
            "A compound formed from Thaumica"
    );

    public static final SlimefunItemStack DEVOLVED_NAMALON = SFrameTheme.sfStackFromTheme(
            "WF_DEVOLVED_NAMALON",
            Material.IRON_INGOT,
            RESOURCES_THEME.withNameColor(ChatColor.GRAY),
            "Devolved Namalon",
            "A compound formed from Namalon"
    );
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Machines">
    public static final SlimefunItemStack CRYOTIC_EXTRACTOR = SFrameTheme.sfStackFromTheme(
            "WF_CRYO_EXTRACTOR",
            Material.BEACON,
            MACHINES_THEME.withNameColor(ChatColor.DARK_BLUE),
            "Cryotic Extractor",
            "Extract Cryotic from sub-zero biomes",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(512)
    );

    public static final SlimefunItemStack ADV_CRYOTIC_EXTRACTOR = SFrameTheme.sfStackFromTheme(
            "WF_CRYO_EXTRACTOR_2",
            Material.BEACON,
            MACHINES_THEME.withNameColor(ChatColor.DARK_BLUE),
            "Advanced Cryotic Extractor",
            "An improved version of the",
            "Cryotic Extractor",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(1024),
            Lore.speed(4)
    );

    public static final SlimefunItemStack PRIME_CRYOTIC_EXTRACTOR = SFrameTheme.sfStackFromTheme(
            "WF_CRYO_EXTRACTOR_3",
            Material.BEACON,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + ChatColor.DARK_BLUE + "Cryotic Extractor",
            "Prime version of the Cryotic Extractor",
            "that uses Orokin technology for",
            "a better performance",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(2048),
            Lore.speed(20)
    );

    public static final SlimefunItemStack THERMIA_EXTRACTOR = SFrameTheme.sfStackFromTheme(
            "WF_THERMIA_EXTRACTOR",
            Material.LODESTONE,
            MACHINES_THEME.withNameColor(Colors.ORANGE),
            "Thermia Extractor",
            "A powerful extractor used to fill up",
            "coolant canisters with Dilluted Thermia",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(512)
    );

    public static final SlimefunItemStack CONCRETE_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_CONCRETE_GEN",
            Material.BRICKS,
            MACHINES_THEME,
            "Concrete Generator",
            "Produce any concrete with this",
            "powerful Orokin machine",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(128),
            Lore.speed(1)
    );

    public static final SlimefunItemStack ADV_CONCRETE_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_CONCRETE_GEN_2",
            Material.NETHER_BRICKS,
            MACHINES_THEME,
            "Advanced Concrete Generator",
            "An improved version of the",
            "Concrete Generator",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.speed(5)
    );

    public static final SlimefunItemStack PRIME_CONCRETE_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_CONCRETE_GEN_3",
            Material.RED_NETHER_BRICKS,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + MACHINES_THEME.getNameColor() + "Concrete Generator",
            "A cutting-edge version of the",
            "Concrete Generator for anyone who",
            "needs a massive concrete production",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.speed(30)
    );

    public static final SlimefunItemStack FLOWER_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_FLOWER_GEN",
            Material.DEAD_HORN_CORAL_BLOCK,
            MACHINES_THEME.withNameColor(ChatColor.GREEN),
            "Flower Generator",
            "This machine will produce any",
            "flower in a pot above it",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(128),
            Lore.production(1)
    );

    public static final SlimefunItemStack ADV_FLOWER_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_FLOWER_GEN_2",
            Material.TUFF,
            MACHINES_THEME.withNameColor(ChatColor.GREEN),
            "Advanced Flower Generator",
            "An improved version of the",
            "Flower Generator",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.production(8)
    );

    public static final SlimefunItemStack PRIME_FLOWER_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_FLOWER_GEN_3",
            Material.MOSS_BLOCK,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + ChatColor.GREEN + "Flower Generator",
            "A cutting-edge version of the",
            "Flower Generator for anyone who",
            "needs a massive flower production",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.production(32)
    );

    public static final SlimefunItemStack WOOL_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_WOOL_GEN",
            Material.WHITE_WOOL,
            MACHINES_THEME.withNameColor(ChatColor.DARK_AQUA),
            "Wool Generator",
            "Produce any wool on a large scale",
            "using Orokin technology",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(128),
            Lore.speed(1)
    );

    public static final SlimefunItemStack ADV_WOOL_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_WOOL_GEN_2",
            Material.WHITE_WOOL,
            MACHINES_THEME.withNameColor(ChatColor.DARK_AQUA),
            "Advanced Wool Generator",
            "An improved version of the",
            "Wool Generator",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.speed(5)
    );

    public static final SlimefunItemStack PRIME_WOOL_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_WOOL_GEN_3",
            Material.WHITE_WOOL,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + ChatColor.DARK_AQUA + "Wool Generator",
            "A cutting-edge version of the",
            "Wool Generator for anyone who",
            "needs a massive wool production",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.speed(30)
    );

    public static final SlimefunItemStack AUTO_TRADER = SFrameTheme.sfStackFromTheme(
            "WF_AUTO_TRADER",
            Material.CARTOGRAPHY_TABLE,
            MACHINES_THEME.withNameColor(ChatColor.GREEN),
            "Auto Trader",
            "With the knowledge of the void traders",
            "this machine is able to automatically",
            "make trades given a Merchant Contract",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(1024)
    );

    public static final SlimefunItemStack ARTIFICIAL_MANGROVE = SFrameTheme.sfStackFromTheme(
            "WF_ARTIFICIAL_MANGROVE",
            Material.MUD,
            MACHINES_THEME.withNameColor(Colors.BROWN),
            "Artificial Mangrove",
            "Efficiently generates resources from",
            "the mangrove biome",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(128),
            Lore.speed(1)
    );

    public static final SlimefunItemStack ADV_ARTIFICIAL_MANGROVE = SFrameTheme.sfStackFromTheme(
            "WF_ARTIFICIAL_MANGROVE_2",
            Material.MUDDY_MANGROVE_ROOTS,
            MACHINES_THEME.withNameColor(Colors.BROWN),
            "Advanced Artificial Mangrove",
            "An improved version of the",
            "Artificial Mangrove",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.speed(5)
    );

    public static final SlimefunItemStack PRIME_ARTIFICIAL_MANGROVE = SFrameTheme.sfStackFromTheme(
            "WF_ARTIFICIAL_MANGROVE_3",
            Material.PACKED_MUD,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + Colors.BROWN + "Artificial Mangrove",
            "A cutting-edge version of the",
            "Artificial Mangrove",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.speed(30)
    );

    public static final SlimefunItemStack BASALT_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_BASALT_GEN",
            Material.BASALT,
            MACHINES_THEME.withNameColor(ChatColor.DARK_GRAY),
            "Basalt Generator",
            "Produces basalt",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(128),
            Lore.production(1)
    );

    public static final SlimefunItemStack ADV_BASALT_GEN = SFrameTheme.sfStackFromTheme(
            "WF_BASALT_GEN_2",
            Material.POLISHED_BASALT,
            MACHINES_THEME.withNameColor(ChatColor.DARK_GRAY),
            "Advanced Basalt Generator",
            "An improved version of the",
            "Basalt Generator",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.production(8)
    );

    public static final SlimefunItemStack PRIME_BASALT_GEN = SFrameTheme.sfStackFromTheme(
            "WF_BASALT_GEN_3",
            Material.SMOOTH_BASALT,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + ChatColor.DARK_GRAY + "Basalt Generator",
            "A cutting-edge version of the",
            "Basalt Generator for anyone who",
            "needs a massive basalt production",
            "",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.production(64)
    );

    public static final SlimefunItemStack CHUNK_EATER = SFrameTheme.sfStackFromTheme(
            "WF_CHUNK_EATER",
            Material.OBSIDIAN,
            MACHINES_THEME.withNameColor(ChatColor.DARK_PURPLE),
            "Chunk Eater",
            "Destroys every block bellow it",
            "and in the same chunk",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(1024),
            Lore.speed(1)
    );

    public static final SlimefunItemStack ADV_CHUNK_EATER = SFrameTheme.sfStackFromTheme(
            "WF_CHUNK_EATER_2",
            Material.CRYING_OBSIDIAN,
            MACHINES_THEME.withNameColor(ChatColor.DARK_PURPLE),
            "Advanced Chunk Eater",
            "An advanced version of the Chunk Eater",
            "It will also collect the destroyed blocks",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(2048),
            Lore.speed(1)
    );

    public static final SlimefunItemStack PRIME_CHUNK_EATER = SFrameTheme.sfStackFromTheme(
            "WF_CHUNK_EATER_3",
            enchantedItem(Material.CRYING_OBSIDIAN),
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + Colors.BLUE_VIOLET + "Chunk Eater",
            "Devours a whole chunk",
            "layer by layer",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(4096),
            Lore.speed(16)
    );

    public static final SlimefunItemStack TREE_PEELER = SFrameTheme.sfStackFromTheme(
            "WF_TREE_PEELER",
            Material.STONECUTTER,
            MACHINES_THEME.withNameColor(Colors.BROWN),
            "Tree Peeler",
            "A machine capable of stripping",
            "any log or wood, converting",
            "it into a stripped form",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(128),
            Lore.speed(1)
    );

    public static final SlimefunItemStack ADV_TREE_PEELER = SFrameTheme.sfStackFromTheme(
            "WF_TREE_PEELER_2",
            Material.STONECUTTER,
            MACHINES_THEME.withNameColor(Colors.BROWN),
            "Advanced Tree Peeler",
            "An improved version of the",
            "Tree Peeler",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.speed(5)
    );

    public static final SlimefunItemStack PRIME_TREE_PEELER = SFrameTheme.sfStackFromTheme(
            "WF_TREE_PEELER_3",
            Material.STONECUTTER,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + Colors.BROWN + "Tree Peeler",
            "A cutting-edge version of the",
            "Tree Peeler",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.speed(30)
    );

    public static final SlimefunItemStack RECYCLER = SFrameTheme.sfStackFromTheme(
            "WF_RECYCLER",
            Material.PISTON,
            MACHINES_THEME,
            "Recycler",
            "Used to get Salvage and Gallium",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(512)
    );

    public static final SlimefunItemStack TELLURIUM_FRAGMENTS_SYNTHESIZER = SFrameTheme.sfStackFromTheme(
            "WF_TELLURIUM_FRAGS_SYNTHESIZER",
            Material.NETHER_WART_BLOCK,
            MACHINES_THEME.withNameColor(ChatColor.DARK_RED),
            "Tellurium Fragments Synthesizer",
            "Synthesizes tellurium fragments",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(512)
    );

    public static final SlimefunItemStack SULFATE_PRODUCER = SFrameTheme.sfStackFromTheme(
            "WF_SULFATE_PRODUCER",
            Material.FURNACE,
            MACHINES_THEME.withNameColor(ChatColor.YELLOW),
            "Sulfate Producer",
            "Produces sulfate from basalt",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(128),
            Lore.speed(1)
    );

    public static final SlimefunItemStack ADV_SULFATE_PRODUCER = SFrameTheme.sfStackFromTheme(
            "WF_SULFATE_PRODUCER_2",
            Material.FURNACE,
            MACHINES_THEME.withNameColor(Colors.ORANGE),
            "Advanced Sulfate Producer",
            "An improved version of the",
            "Sulfate Producer",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.speed(5),
            Lore.production(2)
    );

    public static final SlimefunItemStack PRIME_SULFATE_PRODUCER = SFrameTheme.sfStackFromTheme(
            "WF_SULFATE_PRODUCER_3",
            Material.FURNACE,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + ChatColor.RED + "Sulfate Producer",
            "The best version of the",
            "Sulfate Producer",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.speed(30),
            Lore.production(8)
    );

    public static final SlimefunItemStack DUST_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_DUST_GEN",
            Material.FURNACE,
            MACHINES_THEME.withNameColor(Colors.CRAYOLA_BLUE),
            "Dust Generator",
            "With this machine you can",
            "select which dust you",
            "want to produce!",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.speed(1)
    );

    public static final SlimefunItemStack ADV_DUST_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_DUST_GEN_2",
            Material.FURNACE,
            MACHINES_THEME.withNameColor(Colors.CRAYOLA_BLUE),
            "Advanced Dust Generator",
            "An improved version of the Dust Generator",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.speed(2),
            Lore.production(4)
    );

    public static final SlimefunItemStack PRIME_DUST_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_DUST_GEN_3",
            Material.FURNACE,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + Colors.CRAYOLA_BLUE + "Dust Generator",
            "The best version of the Dust Generator",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(1024),
            Lore.speed(4),
            Lore.production(32)
    );

    public static final SlimefunItemStack GLASS_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_GLASS_GENERATOR",
            Material.GLASS,
            MACHINES_THEME.withNameColor(Colors.SAVOY_BLUE),
            "Glass Generator",
            "A versatile machine capable of",
            "producing every color of glass",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(128),
            Lore.speed(1)
    );

    public static final SlimefunItemStack ADV_GLASS_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_GLASS_GENERATOR_2",
            Material.LIGHT_BLUE_STAINED_GLASS,
            MACHINES_THEME.withNameColor(Colors.CRAYOLA_BLUE),
            "Advanced Glass Generator",
            "An upgraded version of the",
            "Glass Generator for bigger productions",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.speed(5),
            Lore.production(2)
    );

    public static final SlimefunItemStack PRIME_GLASS_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_GLASS_GENERATOR_3",
            Material.BLUE_STAINED_GLASS,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + Colors.NEON_BLUE + "Glass Generator",
            "The best version of the",
            "Glass Generator, embedded with",
            "Orokin technology",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.speed(30),
            Lore.production(6)
    );

    public static final SlimefunItemStack PUTRIFIER = SFrameTheme.sfStackFromTheme(
            "WF_PUTRIFIER",
            Material.SOUL_SAND,
            MACHINES_THEME,
            Colors.BROWN + "Putrifier",
            "This machine is capable",
            "of putrefying items",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(128),
            Lore.speed(1)
    );

    public static final SlimefunItemStack ADV_PUTRIFIER = SFrameTheme.sfStackFromTheme(
            "WF_PUTRIFIER_2",
            Material.SOUL_SOIL,
            MACHINES_THEME,
            Colors.BROWN + "Advanced Putrifier",
            "An improved version of",
            "the Putrifier",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.speed(5)
    );

    public static final SlimefunItemStack PRIME_PUTRIFIER = SFrameTheme.sfStackFromTheme(
            "WF_PUTRIFIER_3",
            Material.MUDDY_MANGROVE_ROOTS,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + Colors.BROWN + "Putrifier",
            "A cutting-edge version of the",
            "Putrifier for anyone who",
            "needs a massive production",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.speed(30)
    );

    public static final SlimefunItemStack TERRACOTTA_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_TERRACOTTA_GEN",
            Material.LIGHT_BLUE_TERRACOTTA,
            MACHINES_THEME.withNameColor(ChatColor.BLUE),
            "Terracotta Generator",
            "A machine that can generate",
            "any non-glazed terracotta",
            "",
            LoreBuilder.machine(MachineTier.GOOD, MachineType.MACHINE),
            Lore.powerPerSecond(128),
            Lore.speed(1)
    );

    public static final SlimefunItemStack ADV_TERRACOTTA_GEN = SFrameTheme.sfStackFromTheme(
            "WF_TERRACOTTA_GEN_2",
            Material.BLUE_TERRACOTTA,
            MACHINES_THEME.withNameColor(ChatColor.DARK_BLUE),
            "Advanced Terracotta Generator",
            "An improved version of the",
            "Terracotta Generator",
            "",
            LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE),
            Lore.powerPerSecond(256),
            Lore.speed(5)
    );

    public static final SlimefunItemStack PRIME_TERRACOTTA_GEN = SFrameTheme.sfStackFromTheme(
            "WF_TERRACOTTA_GEN_3",
            Material.PURPLE_TERRACOTTA,
            MACHINES_THEME,
            ChatColor.GOLD + "Prime " + ChatColor.LIGHT_PURPLE + "Terracotta Generator",
            "The best version of the",
            "Terracotta Generator",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
            Lore.powerPerSecond(512),
            Lore.speed(30)
    );
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Energy Generators">
    public static final SlimefunItemStack GRAVITECH_ENERCELL = SFrameTheme.sfStackFromTheme(
            "WF_GRAVITECH_ENERCELL",
            Material.WHITE_GLAZED_TERRACOTTA,
            GENERATORS_THEME.withNameColor(ChatColor.of("#8789DB")),
            "Gravitech Enercell",
            "The Gravitech Enercell is a fundamental energy generator",
            "that utilizes gravitational manipulation technology",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            Lore.powerPerSecond(2000)
    );

    public static final SlimefunItemStack ARCANE_FLUX_DYNAMO = SFrameTheme.sfStackFromTheme(
            "WF_ARCANE_FLUX_DYNAMO",
            Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
            GENERATORS_THEME.withNameColor(ChatColor.of("#7872D3")),
            "Arcane Flux Dynamo",
            "The Arcane Flux Dynamo is an advanced energy generator",
            "that harnesses arcane energies from the Void",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            Lore.powerPerSecond(2000),
            Lore.bonusPower(1500)
    );

    public static final SlimefunItemStack SPECTRA_REACTOR = SFrameTheme.sfStackFromTheme(
            "WF_SPECTRA_REACTOR",
            Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
            GENERATORS_THEME.withNameColor(ChatColor.of("#6D5DCC")),
            "Spectra Reactor",
            "The Spectra Reactor is a highly efficient energy",
            "generator infused with spectral particles",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            Lore.powerPerSecond(4000),
            Lore.bonusPower(3000)
    );

    public static final SlimefunItemStack PRISMA_POWER_CORE = SFrameTheme.sfStackFromTheme(
            "WF_PRISMA_POWER_CORE",
            Material.CYAN_GLAZED_TERRACOTTA,
            GENERATORS_THEME.withNameColor(ChatColor.of("#6548C3")),
            "Prisma Power Core",
            "The Prisma Power Core is a radiant energy generator",
            "crafted from rare prismatic crystals",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            Lore.powerPerSecond(8000),
            Lore.bonusPower(6000)
    );

    public static final SlimefunItemStack VOIDLIGHT_FUSION_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_VOIDLIGHT_GEN",
            Material.BROWN_GLAZED_TERRACOTTA,
            GENERATORS_THEME.withNameColor(ChatColor.of("#5F34BB")),
            "Voidlight Fusion Generator",
            "The Voidlight Fusion Generator is a pinnacle of energy",
            "technology, utilizing the raw power of the Void",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            Lore.powerPerSecond(16000),
            Lore.bonusPower(12000)
    );

    public static final SlimefunItemStack AXIOM_ENERGENESIS_ENGINE = SFrameTheme.sfStackFromTheme(
            "WF_AXIOM_ENGINE",
            Material.GREEN_GLAZED_TERRACOTTA,
            GENERATORS_THEME.withNameColor(ChatColor.of("#4A26A6")),
            "Axiom Energenesis Engine",
            "The Axiom Energenesis Engine is a marvel of",
            "ancient Orokin technology",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            Lore.powerPerSecond(32000),
            Lore.bonusPower(24000)
    );

    public static final SlimefunItemStack CHRONOS_INFINITY_DYNAMO = SFrameTheme.sfStackFromTheme(
            "WF_CHRONOS_DYNAMO",
            Material.BLUE_GLAZED_TERRACOTTA,
            GENERATORS_THEME.withNameColor(ChatColor.of("#361A90")),
            "Chronos Infinity Dynamo",
            "The Chronos Infinity Dynamo harnesses the power",
            "of temporal anomalies to generate vast amounts of energy",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            Lore.powerPerSecond(64000),
            Lore.bonusPower(48000)
    );

    public static final SlimefunItemStack PRIMORDIAL_ETERNACORE_REACTOR = SFrameTheme.sfStackFromTheme(
            "WF_PRIMORDIAL_REACTOR",
            Material.PURPLE_GLAZED_TERRACOTTA,
            GENERATORS_THEME.withNameColor(ChatColor.of("#241177")),
            "Primordial Eternacore Reactor",
            "The Primordial Eternacore Reactor taps into",
            "the primordial energies of creation itself",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            Lore.powerPerSecond(128000),
            Lore.bonusPower(96000)
    );

    public static final SlimefunItemStack VOIDFORGE_CELESTIUM_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_VOIDFORGE_CELESTIUM_GEN",
            Material.GRAY_GLAZED_TERRACOTTA,
            GENERATORS_THEME.withNameColor(ChatColor.of("#16095B")),
            "Voidforge Celestium Generator",
            "The Voidforge Celestium Generator represents the",
            "pinnacle of energy manipulation. It channels the",
            "raw energy of the Void and celestial entities",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            Lore.powerPerSecond(256000),
            Lore.bonusPower(192000)
    );

    public static final SlimefunItemStack ASTRAL_PRIME_GENERATOR = SFrameTheme.sfStackFromTheme(
            "WF_ASTRAL_PRIME_GEN",
            Material.BLACK_GLAZED_TERRACOTTA,
            GENERATORS_THEME.withNameColor(ChatColor.GOLD),
            ChatColor.BOLD + "Astral Prime Generator",
            "The Astral Prime Generator epitomizes the zenith",
            "of Warframe energy technology. Fueled by celestial",
            "energies and empowered by Prime enhancements, it sets",
            "the standard for unrivaled energy production",
            "",
            LoreBuilder.machine(MachineTier.END_GAME, MachineType.GENERATOR),
            Lore.powerPerSecond(1_000_000),
            Lore.bonusPower(1_000_000)
    );
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Multiblocks">
    public static final SlimefunItemStack FOUNDRY = new SlimefunItemStack(
            "WF_FOUNDRY",
            Material.ANVIL,
            "&bFoundry",
            "",
            "&fA special foundry used to craft SlimeFrame items"
    );
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Relics">
    public static final RelicItemStack LITH_A1 = new RelicItemStack(
            "Lith A1",
            Relic.Era.LITH,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_BASALT_GEN), SlimefunItems.BLISTERING_INGOT, SlimefunItems.PLUTONIUM},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_WOOL_GENERATOR), SlimefunItems.SOLAR_GENERATOR_3},
            PrimeComponents.createCoreModule(PRIME_ARTIFICIAL_MANGROVE)
    );

    public static final RelicItemStack MESO_B1 = new RelicItemStack(
            "Meso B1",
            Relic.Era.MESO,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_CRYOTIC_EXTRACTOR), SlimefunItems.REINFORCED_ALLOY_INGOT, SFrameStacks.PYROTIC_ALLOY},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(ASTRAL_PRIME_GENERATOR), PrimeComponents.createPowerCell(PRIME_GLASS_GENERATOR)},
            PrimeComponents.createCoreModule(PRIME_BASALT_GEN)
    );

    public static final RelicItemStack NEO_C1 = new RelicItemStack(
            "Neo C1",
            Relic.Era.NEO,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_FLOWER_GENERATOR), SlimefunItems.BOOSTED_URANIUM, SFrameStacks.PLASTIDS},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_ARTIFICIAL_MANGROVE), UtilsComponents.createTemporal(PRIME_OROKIN_WAND)},
            PrimeComponents.createCoreModule(PRIME_CRYOTIC_EXTRACTOR)
    );

    public static final RelicItemStack AXI_F1 = new RelicItemStack(
            "Axi F1",
            Relic.Era.AXI,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_TREE_PEELER), UtilsComponents.createVoidShard(PRIME_OROKIN_WAND), SlimefunItems.POWER_CRYSTAL},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_BASALT_GEN), SFrameStacks.DILUTED_THERMIA},
            PrimeComponents.createCoreModule(PRIME_FLOWER_GENERATOR)
    );

    public static final RelicItemStack LITH_T1 = new RelicItemStack(
            "Lith T1",
            Relic.Era.LITH,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_WOOL_GENERATOR), SFrameStacks.AURON, SlimefunItems.VITAMINS},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_CRYOTIC_EXTRACTOR), SFrameStacks.ARGON_CRYSTAL},
            PrimeComponents.createCoreModule(PRIME_TREE_PEELER)
    );

    public static final RelicItemStack MESO_W1 = new RelicItemStack(
            "Meso W1",
            Relic.Era.MESO,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(ASTRAL_PRIME_GENERATOR), SlimefunItems.HARDENED_METAL_INGOT, SFrameStacks.SALVAGE},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_FLOWER_GENERATOR), UtilsComponents.createTemporal(PRIME_NOSAM_PICK)},
            PrimeComponents.createCoreModule(PRIME_WOOL_GENERATOR)
    );

    public static final RelicItemStack NEO_A1 = new RelicItemStack(
            "Neo A1",
            Relic.Era.NEO,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_ARTIFICIAL_MANGROVE), UtilsComponents.createVoidShard(PRIME_NOSAM_PICK), SlimefunItems.CARBONADO},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_TREE_PEELER), SlimefunItems.BIG_CAPACITOR},
            PrimeComponents.createCoreModule(ASTRAL_PRIME_GENERATOR)
    );

    public static final RelicItemStack AXI_N1 = new RelicItemStack(
            "Axi N1",
            Relic.Era.AXI,
            new SlimefunItemStack[]{SlimefunItems.BLANK_RUNE, SlimefunItems.HOLOGRAM_PROJECTOR, SFrameStacks.SALVAGE},
            new SlimefunItemStack[]{SFrameStacks.TELLURIUM_FRAGMENT, SlimefunItems.FLUID_PUMP},
            UtilsComponents.createNeuralNexus(PRIME_NOSAM_PICK)
    );

    public static final RelicItemStack LITH_O1 = new RelicItemStack(
            "Lith O1",
            Relic.Era.LITH,
            new SlimefunItemStack[]{SlimefunItems.TRASH_CAN, PrimeComponents.createControlUnit(PRIME_DUST_GENERATOR), SFrameStacks.PLASTIDS},
            new SlimefunItemStack[]{SlimefunItems.SOULBOUND_RUNE, SlimefunItems.ELECTRIC_DUST_WASHER_2},
            UtilsComponents.createNeuralNexus(PRIME_OROKIN_WAND)
    );

    public static final RelicItemStack MESO_S1 = new RelicItemStack(
            "Meso S1",
            Relic.Era.MESO,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_BASALT_GEN), SlimefunItems.SMALL_CAPACITOR, SlimefunItems.CARBONADO},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_DUST_GENERATOR), SlimefunItems.RAINBOW_RUNE},
            PrimeComponents.createCoreModule(PRIME_SULFATE_PRODUCER)
    );

    public static final RelicItemStack NEO_D1 = new RelicItemStack(
            "Neo D1",
            Relic.Era.NEO,
            new SlimefunItemStack[]{SlimefunItems.WITHER_PROOF_OBSIDIAN, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBON},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_WOOL_GENERATOR), PrimeComponents.createPowerCell(PRIME_CHUNK_EATER)},
            PrimeComponents.createCoreModule(PRIME_DUST_GENERATOR)
    );

    public static final RelicItemStack AXI_G1 = new RelicItemStack(
            "Axi G1",
            Relic.Era.AXI,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_CHUNK_EATER), SlimefunItems.URANIUM, SlimefunItems.GOLD_24K},
            new SlimefunItemStack[]{SlimefunItems.ELECTRIC_DUST_WASHER_2, SlimefunItems.MEDIUM_CAPACITOR},
            PrimeComponents.createCoreModule(PRIME_GLASS_GENERATOR)
    );

    public static final RelicItemStack LITH_C1 = new RelicItemStack(
            "Lith C1",
            Relic.Era.LITH,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_TREE_PEELER), PrimeComponents.createControlUnit(PRIME_GLASS_GENERATOR), SlimefunItems.REINFORCED_PLATE},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_FLOWER_GENERATOR), SlimefunItems.STAFF_ELEMENTAL},
            PrimeComponents.createCoreModule(PRIME_CHUNK_EATER)
    );

    public static final RelicItemStack MESO_C1 = new RelicItemStack(
            "Meso C1",
            Relic.Era.MESO,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_CRYOTIC_EXTRACTOR), PrimeComponents.createControlUnit(PRIME_PUTRIFIER), PrimeComponents.createControlUnit(PRIME_TERRACOTTA_GEN)},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_SULFATE_PRODUCER), SlimefunItems.ARMOR_AUTO_CRAFTER},
            PrimeComponents.createCoreModule(PRIME_CONCRETE_GENERATOR)
    );

    public static final RelicItemStack NEO_P1 = new RelicItemStack(
            "Neo P1",
            Relic.Era.NEO,
            new SlimefunItemStack[]{PrimeComponents.createControlUnit(PRIME_CONCRETE_GENERATOR), PrimeComponents.createControlUnit(PRIME_SULFATE_PRODUCER), SlimefunItems.CHARGING_BENCH},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_TERRACOTTA_GEN), SlimefunItems.LAVA_GENERATOR_2},
            PrimeComponents.createCoreModule(PRIME_PUTRIFIER)
    );

    public static final RelicItemStack AXI_T1 = new RelicItemStack(
            "Axi T1",
            Relic.Era.AXI,
            new SlimefunItemStack[]{UtilsComponents.createVoidShard(PRIME_NOSAM_PICK), PrimeComponents.createControlUnit(ASTRAL_PRIME_GENERATOR), SlimefunItems.EARTH_RUNE},
            new SlimefunItemStack[]{PrimeComponents.createPowerCell(PRIME_PUTRIFIER), PrimeComponents.createPowerCell(PRIME_CONCRETE_GENERATOR)},
            PrimeComponents.createCoreModule(PRIME_TERRACOTTA_GEN)
    );

    static {
        for (Field declaredField : SFrameStacks.class.getDeclaredFields()) {
            try {
                if (declaredField.getType() != RelicItemStack.class) continue;
                RelicItemStack relicItemStack = (RelicItemStack) declaredField.get(null);
                switch (relicItemStack.getRelicEra()) {
                    case LITH -> RANDOM_LITH_RELICS.add(relicItemStack);
                    case MESO -> RANDOM_MESO_RELICS.add(relicItemStack);
                    case NEO -> RANDOM_NEO_RELICS.add(relicItemStack);
                    default -> RANDOM_AXI_RELICS.add(relicItemStack);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    // </editor-fold>

}