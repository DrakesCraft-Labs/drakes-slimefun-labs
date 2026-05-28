package me.voper.slimeframe.implementation.groups;

import org.bukkit.Material;

import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.utils.Colors;
import me.voper.slimeframe.utils.HeadTextures;
import me.voper.slimeframe.utils.Keys;

public class Groups {

    private static final SlimeFrame ins = SlimeFrame.getInstance();

    // Title of each group
    public static final String RESOURCES_NAME = Colors.SILVER + "<" + Colors.CRAYOLA_BLUE + "SlimeFrame Resources" + Colors.SILVER + ">";
    public static final String MACHINES_NAME = Colors.SILVER + "<" + Colors.CRAYOLA_BLUE + "SlimeFrame Machines" + Colors.SILVER + ">";
    public static final String GENERATORS_NAME = Colors.SILVER + "<" + Colors.CRAYOLA_BLUE + "SlimeFrame Generators" + Colors.SILVER + ">";
    public static final String UTILS_AND_TOOLS_NAME = Colors.SILVER + "<" + Colors.CRAYOLA_BLUE + "SlimeFrame Utils & Tools" + Colors.SILVER + ">";
    public static final String GEAR_NAME = Colors.SILVER + "<" + Colors.CRAYOLA_BLUE + "SlimeFrame Gear" + Colors.SILVER + ">";
    public static final String PRIME_COMPONENTS_NAME = Colors.SILVER + "<" + Colors.CRAYOLA_BLUE + "Prime Components" + Colors.SILVER + ">";

    public static final MasterGroup MAIN_GROUP = new MasterGroup(
            Keys.CAT_MAIN,
            HeadTextures.MAIN_GROUP,
            "§x§5§d§3§4§e§7S§x§5§8§3§d§e§3l§x§5§3§4§7§d§fi§x§4§e§5§0§d§am§x§4§9§5§9§d§6e§x§4§3§6§3§d§2F§x§3§e§6§c§c§er§x§3§9§7§5§c§9a§x§3§4§7§f§c§5m§x§2§f§8§8§c§1e"
    );

    public static final TutorialsGroup TUTORIALS = new TutorialsGroup(
            Keys.createKey("wf_tutorials"),
            new CustomItemStack(Material.ENCHANTED_BOOK, Colors.CRAYOLA_BLUE + "Tutorials")
    );

    public static final MasterGroup RELICS = new MasterGroup(
            Keys.CAT_RELICS,
            MAIN_GROUP,
            new CustomItemStack(Material.ENDER_EYE, Colors.CRAYOLA_BLUE + "Relics"),
            Colors.CRAYOLA_BLUE + "Relics"
    );

    // Relics categories
    public static final ChildGroup LITH = new ChildGroup(
            Keys.CAT_LITH,
            RELICS,
            new CustomItemStack(HeadTextures.getSkull(HeadTextures.LITH_RELIC), "&fLith Era")
    );

    public static final ChildGroup MESO = new ChildGroup(
            Keys.CAT_MESO,
            RELICS,
            new CustomItemStack(HeadTextures.getSkull(HeadTextures.MESO_RELIC), "&eMeso Era")
    );

    public static final ChildGroup NEO = new ChildGroup(
            Keys.CAT_NEO,
            RELICS,
            new CustomItemStack(HeadTextures.getSkull(HeadTextures.NEO_RELIC), "&2Neo Era")
    );

    public static final ChildGroup AXI = new ChildGroup(
            Keys.CAT_AXI,
            RELICS,
            new CustomItemStack(HeadTextures.getSkull(HeadTextures.AXI_RELIC), "&dAxi Era")
    );

    public static final ChildGroup RESOURCES = new ChildGroup(
            Keys.CAT_RESOURCES,
            MAIN_GROUP,
            new CustomItemStack(Material.COAL, Colors.CRAYOLA_BLUE + "Resources")
    );

    public static final ChildGroup MULTIBLOCKS = new ChildGroup(
            Keys.CAT_MULTIBLOCKS,
            MAIN_GROUP,
            new CustomItemStack(HeadTextures.getSkull(HeadTextures.MULTIBLOCKS), Colors.CRAYOLA_BLUE + "Multiblocks")
    );

    public static final ChildGroup MACHINES = new ChildGroup(
            Keys.CAT_MACHINES,
            MAIN_GROUP,
            new CustomItemStack(Material.STONECUTTER, Colors.CRAYOLA_BLUE + "Machines")
    );

    public static final ChildGroup GENERATORS = new ChildGroup(
            Keys.CAT_GENERATORS,
            MAIN_GROUP,
            new CustomItemStack(Material.DAYLIGHT_DETECTOR, Colors.CRAYOLA_BLUE + "Generators")
    );

    public static final ChildGroup GEAR = new ChildGroup(
            Keys.CAT_GEAR,
            MAIN_GROUP,
            new CustomItemStack(Material.CHAINMAIL_CHESTPLATE, Colors.CRAYOLA_BLUE + "Gear")
    );

    public static final ChildGroup UTILS_AND_TOOLS = new ChildGroup(
            Keys.CAT_UTILS_AND_TOOLS,
            MAIN_GROUP,
            new CustomItemStack(Material.SHEARS, Colors.CRAYOLA_BLUE + "Utils & Tools")
    );

    public static final ChildGroup PRIME_COMPONENTS = new ChildGroup(
            Keys.CAT_PRIME_COMPONENTS,
            MAIN_GROUP,
            new CustomItemStack(Material.CONDUIT, Colors.CRAYOLA_BLUE + "Prime Components")
    );

}
