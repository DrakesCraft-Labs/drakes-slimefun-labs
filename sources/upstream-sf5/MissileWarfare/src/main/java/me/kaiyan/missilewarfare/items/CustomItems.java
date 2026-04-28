package me.kaiyan.missilewarfare.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.kaiyan.missilewarfare.MissileWarfare;
import me.kaiyan.missilewarfare.blocks.AntiElytraLauncher;
import me.kaiyan.missilewarfare.blocks.AntiMissileLauncher;
import me.kaiyan.missilewarfare.blocks.GroundMissileLauncher;
import me.kaiyan.missilewarfare.util.Translations;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Registers all custom Slimefun items, recipes, and researches for
 * the MissileWarfare addon.
 *
 * @author MissileWarfare contributors
 */
public class CustomItems {

    /**
     * Creates and registers all MissileWarfare items, recipes, and researches
     * with the Slimefun registry.
     */
    public static void setup() {
        //Add section to guide
        NamespacedKey categoryId = new NamespacedKey(MissileWarfare.getInstance(), "missile_warfare");
        ItemStack categoryItem = CustomItemStack.create(Material.GUNPOWDER, Translations.get("missilegroupname"));

        ItemGroup group = new ItemGroup(categoryId, categoryItem);

        //Add Items
        //<editor-fold desc="CREATE ITEMSTACKS">
        //<editor-fold desc="SUGARFUEL">
        SlimefunItemStack sugarfuelstack = new SlimefunItemStack("SUGARFUEL", Material.SUGAR, Translations.getMaterialName("sugarfuel"), Translations.getMaterialLore("sugarfuel"));
        ItemStack[] sugarfuelrecipe = {
                null, SlimefunItems.MAGNESIUM_DUST.item(), null,
                SlimefunItems.MAGNESIUM_DUST.item(), new ItemStack(Material.COAL), SlimefunItems.MAGNESIUM_DUST.item(),
                null, SlimefunItems.MAGNESIUM_DUST.item(), null
        };

        SlimefunItem sugarfuel = new SlimefunItem(group, sugarfuelstack, RecipeType.ENHANCED_CRAFTING_TABLE, sugarfuelrecipe);
        //</editor-fold>
        //<editor-fold desc="GUIDEBOOK">
        SlimefunItemStack guidestack = new SlimefunItemStack("GUIDEBOOK", Material.WRITTEN_BOOK, Translations.getMaterialName("guidebook"), Translations.getMaterialLore("guidebook"));
        ItemStack[] guiderecipe = {
                null, sugarfuelstack.item(), null,
                sugarfuelstack.item(), new ItemStack(Material.BOOK), sugarfuelstack.item(),
                null, sugarfuelstack.item(), null
        };

        GuideBook guide = new GuideBook(group, guidestack, RecipeType.ENHANCED_CRAFTING_TABLE, guiderecipe);
        //</editor-fold>
        //<editor-fold desc="EXPLOSIVEPOWDER">
        SlimefunItemStack explosivepowderstack = new SlimefunItemStack("EXPLOSIVEPOWDER", Material.GLOWSTONE_DUST, Translations.getMaterialName("explosivepowder"), Translations.getMaterialLore("explosivepowder"));
        ItemStack[] explosivepowderrecipe = {
                SlimefunItems.MAGNESIUM_DUST.item(), new ItemStack(Material.COAL), SlimefunItems.MAGNESIUM_DUST.item(),
                new ItemStack(Material.COAL), new ItemStack(Material.GUNPOWDER), new ItemStack(Material.COAL),
                SlimefunItems.MAGNESIUM_DUST.item(), new ItemStack(Material.COAL), SlimefunItems.MAGNESIUM_DUST.item(),
        };

        SlimefunItem explosivepowder = new SlimefunItem(group, explosivepowderstack, RecipeType.ENHANCED_CRAFTING_TABLE, explosivepowderrecipe);


        //</editor-fold>
        //<editor-fold desc="COMPRESSEDEXPLOSIVES">
        SlimefunItemStack compressedpowderstack = new SlimefunItemStack("COMPRESSEDEXPLOSIVES", Material.YELLOW_CONCRETE, Translations.getMaterialName("compressedexplosives"), Translations.getMaterialLore("compressedexplosives"));
        ItemStack[] compressedpowderrecipe = {
                null, new ItemStack(Material.GUNPOWDER), null,
                explosivepowderstack.item(), explosivepowderstack.item(), explosivepowderstack.item(),
                null, new ItemStack(Material.GUNPOWDER), null,
        };
        SlimefunItem compressedpowder = new SlimefunItem(group, compressedpowderstack, RecipeType.ENHANCED_CRAFTING_TABLE, compressedpowderrecipe);
        //</editor-fold>
        //<editor-fold desc="ULTRALITE_INGOT">
        SlimefunItemStack ultraliteingotstack = new SlimefunItemStack("ULTRALITE_INGOT", Material.BRICK, Translations.getMaterialName("ultraliteingot"), Translations.getMaterialLore("ultraliteingot"));
        ItemStack[] ultraliteingotrecipe = {
                SlimefunItems.IRON_DUST.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.COPPER_DUST.item(),
                SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), null, null,
                null, null, null
        };

        SlimefunItem ultraliteingot = new SlimefunItem(group, ultraliteingotstack, RecipeType.SMELTERY, ultraliteingotrecipe);
        //</editor-fold>
        //<editor-fold desc="ULTRALITE_PLATE">
        SlimefunItemStack ultraliteplatestack = new SlimefunItemStack("ULTRALITE_PLATE", Material.IRON_INGOT, Translations.getMaterialName("ultraliteplate"), Translations.getMaterialLore("ultraliteplate"));
        ItemStack[] ultraliteplaterecipe = {
                ultraliteingotstack.item(), null, ultraliteingotstack.item(),
                null, new ItemStack(Material.COAL), null,
                ultraliteingotstack.item(), null, ultraliteingotstack.item()
        };

        SlimefunItem ultraliteplate = new SlimefunItem(group, ultraliteplatestack, RecipeType.ENHANCED_CRAFTING_TABLE, ultraliteplaterecipe);
        //</editor-fold>
        //<editor-fold desc="SIMPLE_FLIGHT_COMPUTER">
        SlimefunItemStack simpleflightcomputerstacks = new SlimefunItemStack("SIMPLEFLIGHTCOMPUTER", Material.POWERED_RAIL, Translations.getMaterialName("simpleflightcomputer"), Translations.getMaterialLore("simpleflightcomputer"));
        SlimefunItemStack simpleflightcomputerstack = (SlimefunItemStack) simpleflightcomputerstacks.clone();
        simpleflightcomputerstacks.setAmount(8);
        simpleflightcomputerstack.setAmount(1);
        ItemStack[] simpleflightcomputerrecipe = {
                ultraliteingotstack.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), ultraliteingotstack.item(),
                new ItemStack(Material.REDSTONE), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), new ItemStack(Material.REDSTONE),
                ultraliteingotstack.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), ultraliteingotstack.item()
        };

        SlimefunItem simpleflightcomputer = new SlimefunItem(group, simpleflightcomputerstack, RecipeType.ENHANCED_CRAFTING_TABLE, simpleflightcomputerrecipe);
        simpleflightcomputer.setRecipeOutput(simpleflightcomputerstacks.item());
        //</editor-fold>
        //<editor-fold desc="RADAR">
        SlimefunItemStack radarstack = new SlimefunItemStack("RADAR", Material.ACTIVATOR_RAIL, Translations.getMaterialName("radar"), Translations.getMaterialLore("radar"));
        ItemStack[] radarrecipe = {
                null, ultraliteplatestack.item(), null,
                ultraliteplatestack.item(), simpleflightcomputerstack.item(), ultraliteplatestack.item(),
                new ItemStack(Material.REDSTONE), null, new ItemStack(Material.REDSTONE)
        };

        SlimefunItem radar = new SlimefunItem(group, radarstack, RecipeType.ENHANCED_CRAFTING_TABLE, radarrecipe);
        //</editor-fold>
        //<editor-fold desc="ROCKETFUEL">
        SlimefunItemStack rocketfuelstack = new SlimefunItemStack("ROCKETFUEL", Material.GUNPOWDER, Translations.getMaterialName("rocketfuel"), Translations.getMaterialLore("rocketfuel"));
        ItemStack[] rocketfuelrecipe = {
                explosivepowderstack.item(), new ItemStack(Material.GUNPOWDER), explosivepowderstack.item(),
                null, SlimefunItems.ALUMINUM_DUST.item(), null,
                explosivepowderstack.item(), new ItemStack(Material.GUNPOWDER), explosivepowderstack.item()
        };
        SlimefunItem rocketfuel = new SlimefunItem(group, rocketfuelstack, RecipeType.ENHANCED_CRAFTING_TABLE, rocketfuelrecipe);
        //</editor-fold>
        //<editor-fold desc="SMALLWARHEAD">
        SlimefunItemStack smallwarheadstack = new SlimefunItemStack("SMALLWARHEAD", Material.TNT, Translations.getMaterialName("smallwarhead"), Translations.getMaterialLore("smallwarhead"));
        ItemStack[] smallwarheadrecipe = {
                null, SlimefunItems.ALUMINUM_INGOT.item(), null,
                SlimefunItems.ALUMINUM_INGOT.item(), explosivepowderstack.item(), SlimefunItems.ALUMINUM_INGOT.item(),
                null, null, null
        };

        SlimefunItem smallwarhead = new SlimefunItem(group, smallwarheadstack, RecipeType.ENHANCED_CRAFTING_TABLE, smallwarheadrecipe);
        //</editor-fold>
        //<editor-fold desc="SMALLBODY">
        SlimefunItemStack smallbodystack = new SlimefunItemStack("SMALLBODY", Material.IRON_BLOCK, Translations.getMaterialName("smallbody"), Translations.getMaterialLore("smallbody"));
        ItemStack[] smallbodyrecipe = {
                SlimefunItems.ALUMINUM_INGOT.item(), null, SlimefunItems.ALUMINUM_INGOT.item(),
                ultraliteingotstack.item(), simpleflightcomputerstack.item(), ultraliteingotstack.item(),
                SlimefunItems.ALUMINUM_INGOT.item(), null, SlimefunItems.ALUMINUM_INGOT.item()
        };

        SlimefunItem smallbody = new SlimefunItem(group, smallbodystack, RecipeType.ENHANCED_CRAFTING_TABLE, smallbodyrecipe);
        //</editor-fold>
        //<editor-fold desc="SMALLFIN">
        SlimefunItemStack smallfinstack = new SlimefunItemStack("SMALLFIN", Material.IRON_BOOTS, Translations.getMaterialName("smallfin"), Translations.getMaterialLore("smallfin"));
        ItemStack[] smallfinrecipe = {
                null, null, null,
                null, simpleflightcomputerstack.item(), null,
                SlimefunItems.ALUMINUM_INGOT.item(), null, SlimefunItems.ALUMINUM_INGOT.item()
        };

        SlimefunItem smallfin = new SlimefunItem(group, smallfinstack, RecipeType.ENHANCED_CRAFTING_TABLE, smallfinrecipe);
        //</editor-fold>
        //<editor-fold desc="MINE">
        SlimefunItemStack minestack = new SlimefunItemStack("MINE", Material.TNT, Translations.getMaterialName("mine"), Translations.getMaterialLore("mine"));
        ItemStack[] minerecipe = {
                SlimefunItems.SILVER_INGOT.item(), new ItemStack(Material.STONE_PRESSURE_PLATE), SlimefunItems.SILVER_INGOT.item(),
                SlimefunItems.SILVER_INGOT.item(), rocketfuelstack.item(), SlimefunItems.SILVER_INGOT.item(),
                SlimefunItems.SILVER_INGOT.item(), explosivepowderstack.item(), SlimefunItems.SILVER_INGOT.item()
        };

        Mine mine = new Mine(group, minestack, RecipeType.ENHANCED_CRAFTING_TABLE, minerecipe);
        //</editor-fold>
        //<editor-fold desc="WARHEAD">
        SlimefunItemStack warheadstack = new SlimefunItemStack("WARHEAD", Material.TNT, Translations.getMaterialName("warhead"), Translations.getMaterialLore("warhead"));
        ItemStack[] warheadrecipe = {
                null, ultraliteingotstack.item(), null,
                ultraliteingotstack.item(), compressedpowderstack.item(), ultraliteingotstack.item(),
                null, null, null
        };

        SlimefunItem warhead = new SlimefunItem(group, warheadstack, RecipeType.ENHANCED_CRAFTING_TABLE, warheadrecipe);
        //</editor-fold>
        Translations.setType("smallmissile");
        //<editor-fold desc="SMALLMISSILE">
        SlimefunItemStack smallmissilestack = new SlimefunItemStack("SMALLMISSILE", Material.IRON_SWORD, Translations.getMissileName("normal"), Translations.getTypeLore(), Translations.getMissileVariant("normal"));
        ItemStack[] smallmissilerecipe = {
                explosivepowderstack.item(), smallwarheadstack.item(), explosivepowderstack.item(),
                sugarfuelstack.item(), smallbodystack.item(), sugarfuelstack.item(),
                sugarfuelstack.item(), smallfinstack.item(), sugarfuelstack.item()
        };

        MissileItem smallmissile = new MissileItem(group, smallmissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, smallmissilerecipe, 1, Translations.getTypeLore());
        //</editor-fold>
        //<editor-fold desc="SMALLMISSILEHE">
        SlimefunItemStack smallmissilestackHE = new SlimefunItemStack("SMALLMISSILEHE", Material.IRON_SWORD, Translations.getMissileName("he"), Translations.getTypeLore(), Translations.getMissileVariant("he"));
        ItemStack[] smallmissilerecipeHE = {
                explosivepowderstack.item(), smallwarheadstack.item(), explosivepowderstack.item(),
                explosivepowderstack.item(), smallbodystack.item(), sugarfuelstack.item(),
                sugarfuelstack.item(), smallfinstack.item(), sugarfuelstack.item()
        };

        MissileItem smallmissileHE = new MissileItem(group, smallmissilestackHE, RecipeType.ENHANCED_CRAFTING_TABLE, smallmissilerecipeHE, 2, Translations.getTypeLore());
        //</editor-fold>
        //<editor-fold desc="SMALLMISSILELR">
        SlimefunItemStack smallmissileLRstack = new SlimefunItemStack("SMALLMISSILELR", Material.IRON_SWORD, Translations.getMissileName("lr"), Translations.getTypeLore(), Translations.getMissileVariant("lr"));
        ItemStack[] smallmissileLRrecipe = {
                explosivepowderstack.item(), smallwarheadstack.item(), explosivepowderstack.item(),
                sugarfuelstack.item(), smallbodystack.item(), sugarfuelstack.item(),
                rocketfuelstack.item(), smallfinstack.item(), rocketfuelstack.item()
        };
        MissileItem smallmissileLR = new MissileItem(group, smallmissileLRstack, RecipeType.ENHANCED_CRAFTING_TABLE, smallmissileLRrecipe, 3, Translations.getMissileLore("lr"));
        //</editor-fold>
        //<editor-fold desc="SMALLMISSILEAC">
        SlimefunItemStack smallmissileACstack = new SlimefunItemStack("SMALLMISSILEAC", Material.IRON_SWORD, Translations.getMissileName("ac"), Translations.getTypeLore(), Translations.getMissileVariant("ac"));
        ItemStack[] smallmissileACrecipe = {
                explosivepowderstack.item(), smallwarheadstack.item(), explosivepowderstack.item(),
                sugarfuelstack.item(), smallbodystack.item(), sugarfuelstack.item(),
                smallfinstack.item(), rocketfuelstack.item(), smallfinstack.item()
        };
        MissileItem smallmissileAC = new MissileItem(group, smallmissileACstack, RecipeType.ENHANCED_CRAFTING_TABLE, smallmissileACrecipe, 4, Translations.getMissileLore("ac"));
        //</editor-fold>
        //<editor-fold desc="GROUNDLAUNCHER">
        SlimefunItemStack groundlauncherstack = new SlimefunItemStack("GROUNDLAUNCHER", Material.DISPENSER, Translations.get("launchers.ground.name"), Translations.pack.getStringList("launchers.ground.lore").toArray(new String[0]));
        ItemStack[] groundlauncherrecipe = {
                SlimefunItems.REINFORCED_PLATE.item(), null, SlimefunItems.REINFORCED_PLATE.item(),
                SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.REINFORCED_PLATE.item(),
                SlimefunItems.REINFORCED_PLATE.item(), new ItemStack(Material.FLINT_AND_STEEL), SlimefunItems.REINFORCED_PLATE.item()
        };
        GroundMissileLauncher groundlauncher = new GroundMissileLauncher(group, groundlauncherstack, RecipeType.ENHANCED_CRAFTING_TABLE, groundlauncherrecipe);
        //</editor-fold>
        //<editor-fold desc="MISSILEBODY">
        SlimefunItemStack missilebodystack = new SlimefunItemStack("MISSILEBODY", Material.SMOOTH_STONE, Translations.getMaterialName("missilebody"), Translations.getMaterialLore("missilebody"));
        ItemStack[] missilebodyrecipe = {
                ultraliteplatestack.item(), simpleflightcomputerstack.item(), ultraliteplatestack.item(),
                ultraliteplatestack.item(), rocketfuelstack.item(), ultraliteplatestack.item(),
                ultraliteplatestack.item(), rocketfuelstack.item(), ultraliteplatestack.item()
        };
        SlimefunItemStack missilebodystacks = (SlimefunItemStack) missilebodystack.clone();
        missilebodystacks.setAmount(4);

        SlimefunItem missilebody = new SlimefunItem(group, missilebodystack, RecipeType.ENHANCED_CRAFTING_TABLE, missilebodyrecipe);

        missilebody.setRecipeOutput(missilebodystacks.item());

        //</editor-fold>
        //<editor-fold desc="MISSILEFINS">
        SlimefunItemStack finsstack = new SlimefunItemStack("MISSILEFINS", Material.GOLDEN_BOOTS, Translations.getMaterialName("missilefins"), Translations.getMaterialLore("missilefins"));
        ItemStack[] finsrecipe = {
                null, null, null,
                ultraliteplatestack.item(), null, ultraliteplatestack.item(),
                ultraliteplatestack.item(), null, ultraliteplatestack.item()
        };

        SlimefunItem fins = new SlimefunItem(group, finsstack, RecipeType.ENHANCED_CRAFTING_TABLE, finsrecipe);
        //</editor-fold>
        Translations.setType("anti-missiles");
        //<editor-fold desc="ANTIAIRMISSILE">
        SlimefunItemStack antiAirMissilestack = new SlimefunItemStack("ANTIAIRMISSILE", Material.IRON_SWORD, Translations.getMissileName("missile"), Translations.getTypeLore(), Translations.getMissileVariant("missile"));
        ItemStack[] antiAirMissilerecipe = {
                null, radarstack.item(), null,
                explosivepowderstack.item(), missilebodystack.item(), explosivepowderstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem antiAirMissile = new MissileItem(group, antiAirMissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, antiAirMissilerecipe, 5, Translations.getMissileLore("missile"));
        //</editor-fold>
        //<editor-fold desc="ANTIELYTRAMISSILE">
        SlimefunItemStack antielytramissilestack = new SlimefunItemStack("ANTIELYTRAMISSILE", Material.GOLDEN_SWORD, Translations.getMissileName("elytra"), Translations.getTypeLore(), Translations.getMissileVariant("missile"));
        ItemStack[] antielytramissilerecipe = {
                radarstack.item(), smallwarheadstack.item(), radarstack.item(),
                explosivepowderstack.item(), missilebodystack.item(), explosivepowderstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem antielytramissile = new MissileItem(group, antielytramissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, antielytramissilerecipe, 23, Translations.getMissileLore("missile"));
        //</editor-fold>
        //<editor-fold desc="ANTIMISSILELAUNCHER">
        SlimefunItemStack antiairlauncherstack = new SlimefunItemStack("ANTIMISSILELAUNCHER", Material.DISPENSER, Translations.get("launchers.antimissile.name"), Translations.pack.getStringList("launchers.antimissile.lore").toArray(new String[0]));
        ItemStack[] antiairlauncherrecipe = {
                SlimefunItems.SILVER_INGOT.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.SILVER_INGOT.item(),
                SlimefunItems.SILVER_INGOT.item(), null, SlimefunItems.SILVER_INGOT.item(),
                SlimefunItems.LEAD_INGOT.item(), new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.LEAD_INGOT.item()
        };
        AntiMissileLauncher antiairlauncher = new AntiMissileLauncher(group, antiairlauncherstack, RecipeType.ENHANCED_CRAFTING_TABLE, antiairlauncherrecipe);
        //</editor-fold>
        //<editor-fold desc="MANPAD">
        SlimefunItemStack manpadstack = new SlimefunItemStack("MANPAD", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiNmVlNWJiZTVhZDQyOTY4MGMxYzE1Y2Y0MjBmOTgxMWUxMTRiNzY4NTRmODk5ZjBlZjA4ZmRlMzMyNzk4YyJ9fX0=", Translations.get("other.manpad.name"), Translations.pack.getStringList("other.manpad.lore").toArray(new String[0]));
        ItemStack[] manpadrecipe = {
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT),
                explosivepowderstack.item(), sugarfuelstack.item(), sugarfuelstack.item(),
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT)
        };
        ManPad manpad = new ManPad(group, manpadstack, RecipeType.ENHANCED_CRAFTING_TABLE, manpadrecipe);
        //</editor-fold>
        Translations.setType("normalmissile");
        //<editor-fold desc="MISSILE">
        SlimefunItemStack missilestack = new SlimefunItemStack("MISSILE", Material.GOLDEN_SWORD, Translations.getMissileName("normal"), Translations.getTypeLore(), Translations.getMissileVariant("normal"));
        ItemStack[] missilerecipe = {
                null, warheadstack.item(), null,
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), smallfinstack.item(), rocketfuelstack.item(),
        };
        MissileItem missile = new MissileItem(group, missilestack, RecipeType.ENHANCED_CRAFTING_TABLE, missilerecipe, 6, Translations.getMissileLore("normal"));
        //</editor-fold>
        //<editor-fold desc="MISSILEHE">
        SlimefunItemStack missileHEstack = new SlimefunItemStack("MISSILEHE", Material.GOLDEN_SWORD, Translations.getMissileName("he"), Translations.getTypeLore(), Translations.getMissileVariant("he"));
        ItemStack[] missileHErecipe = {
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), smallfinstack.item(), rocketfuelstack.item(),
        };
        MissileItem missileHE = new MissileItem(group, missileHEstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileHErecipe, 7, Translations.getMissileLore("he"));
        //</editor-fold>
        //<editor-fold desc="MISSILELR">
        SlimefunItemStack missileLRstack = new SlimefunItemStack("MISSILELR", Material.GOLDEN_SWORD, Translations.getMissileName("lr"), Translations.getTypeLore(), Translations.getMissileVariant("lr"));
        ItemStack[] missileLRrecipe = {
                rocketfuelstack.item(), warheadstack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item(),
        };
        MissileItem missileLR = new MissileItem(group, missileLRstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileLRrecipe, 8, Translations.getMissileLore("lr"));
        //</editor-fold>
        //<editor-fold desc="MISSILEAC">
        SlimefunItemStack missileACstack = new SlimefunItemStack("MISSILEAC", Material.GOLDEN_SWORD, Translations.getMissileName("ac"), Translations.getTypeLore(), Translations.getMissileVariant("ac"));
        ItemStack[] missileACrecipe = {
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                finsstack.item(), rocketfuelstack.item(), finsstack.item(),
        };
        MissileItem missileAC = new MissileItem(group, missileACstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileACrecipe, 9, Translations.getMissileLore("ac"));
        //</editor-fold>
        //<editor-fold desc="PLAYERLIST">
        SlimefunItemStack playerliststack = new SlimefunItemStack("PLAYERLIST", Material.ENCHANTED_BOOK, Translations.get("other.playerlist.name"), Translations.pack.getStringList("other.playerlist.lore").toArray(new String[0]));
        ItemStack[] playerlistrecipe = {
                null, ultraliteingotstack.item(), null,
                ultraliteingotstack.item(), new ItemStack(Material.BOOK), ultraliteingotstack.item(),
                null, ultraliteingotstack.item(), null
        };
        PlayerList playerList = new PlayerList(group, playerliststack, RecipeType.ENHANCED_CRAFTING_TABLE, playerlistrecipe);
        //</editor-fold>
        //<editor-fold desc="ANTIELYTRA">
        SlimefunItemStack antielytrastack = new SlimefunItemStack("ANTIELYTRA", Material.DISPENSER, Translations.get("launchers.antielytra.name"), Translations.pack.getStringList("launchers.antielytra.lore").toArray(new String[0]));
        ItemStack[] antielytralauncherrecipe = {
                ultraliteplatestack.item(), null, ultraliteplatestack.item(),
                SlimefunItems.REINFORCED_PLATE.item(), null, SlimefunItems.REINFORCED_PLATE.item(),
                SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.REINFORCED_PLATE.item()
        };
        AntiElytraLauncher antiElytraLauncher = new AntiElytraLauncher(group, antielytrastack, RecipeType.ENHANCED_CRAFTING_TABLE, antielytralauncherrecipe);

        MissileWarfare main = MissileWarfare.getInstance();
        //</editor-fold>
        //<editor-fold desc="REINFORCEDWARHEAD">
        SlimefunItemStack warheadAPstack = new SlimefunItemStack("WARHEADAP", Material.TNT, Translations.getMaterialName("reinforcedwarhead"), Translations.getMaterialLore("reinforcedwarhead"));
        ItemStack[] warheadAPrecipe = {
                null, SlimefunItems.LEAD_INGOT.item(), null,
                ultraliteingotstack.item(), compressedpowderstack.item(), ultraliteingotstack.item(),
                null, null, null
        };

        SlimefunItem warheadAP = new SlimefunItem(group, warheadAPstack, RecipeType.ENHANCED_CRAFTING_TABLE, warheadAPrecipe);
        //</editor-fold>
        Translations.setType("armourpiercing");
        //<editor-fold desc="APMISSILET1">
        SlimefunItemStack missileAPstack = new SlimefunItemStack("MISSILEAPONE", Material.DIAMOND_SWORD, Translations.getMissileName("tierone"), Translations.getTypeLore(), Translations.getMissileVariant("tierone"));
        ItemStack[] missileAPrecipe = {
                compressedpowderstack.item(), smallwarheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                smallfinstack.item(), finsstack.item(), smallfinstack.item(),
        };
        MissileItem missileAP = new MissileItem(group, missileAPstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileAPrecipe, 10, Translations.getMissileLore("tierone"));
        //</editor-fold>
        //<editor-fold desc="APMISSILET2">
        SlimefunItemStack missileAPtstack = new SlimefunItemStack("MISSILEAPTWO", Material.DIAMOND_SWORD, Translations.getMissileName("tiertwo"), Translations.getTypeLore(), Translations.getMissileVariant("tiertwo"));
        ItemStack[] missileAPtrecipe = {
                ultraliteingotstack.item(), ultraliteplatestack.item(), ultraliteingotstack.item(),
                ultraliteingotstack.item(), missileAPstack.item(), ultraliteingotstack.item(),
                sugarfuelstack.item(), sugarfuelstack.item(), sugarfuelstack.item()
        };
        MissileItem missileAPt = new MissileItem(group, missileAPtstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileAPtrecipe, 11, Translations.getMissileLore("tiertwo"));
        //</editor-fold>
        //<editor-fold desc="APMISSILET3">
        SlimefunItemStack missileAPtrstack = new SlimefunItemStack("MISSILEAPTHR", Material.DIAMOND_SWORD, Translations.getMissileName("tierthree"), Translations.getTypeLore(), Translations.getMissileVariant("tierthree"));
        ItemStack[] missileAPtrrecipe = {
                ultraliteingotstack.item(), ultraliteplatestack.item(), ultraliteingotstack.item(),
                ultraliteingotstack.item(), missileAPtstack.item(), ultraliteingotstack.item(),
                sugarfuelstack.item(), sugarfuelstack.item(), sugarfuelstack.item()
        };
        MissileItem missileAPtr = new MissileItem(group, missileAPtrstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileAPtrrecipe, 12, Translations.getMissileLore("tierthree"));
        //</editor-fold>
        //<editor-fold desc="CHLORINE">
        SlimefunItemStack chlorinestack = new SlimefunItemStack("CHLORINE", Material.SUGAR, Translations.getMaterialName("chlorine"), Translations.getMaterialLore("chlorine"));
        ItemStack[] chlorinerecipe = {
                new ItemStack(Material.SOUL_SAND), null, null,
                null, null, null,
                null, null, null
        };
        SlimefunItem chlorine = new SlimefunItem(group, chlorinestack, RecipeType.ORE_WASHER, chlorinerecipe);
        RecipeType.ORE_WASHER.register(chlorinerecipe, chlorinestack.item());
        chlorine.addItemHandler((ItemUseHandler) playerRightClickEvent -> playerRightClickEvent.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 2)));
        //</editor-fold>
        //<editor-fold desc="CHLORINEPELLET">
        SlimefunItemStack chlorinepelletstack = new SlimefunItemStack("CHLORINEPELLET", Material.LIME_DYE, Translations.getMaterialName("chlorinepellet"), Translations.getMaterialLore("chlorinepellet"));
        ItemStack[] chlorinepelletrecipe = {
                chlorinestack.item(), SlimefunItems.SULFATE.item(), chlorinestack.item(),
                SlimefunItems.SULFATE.item(), SlimefunItems.SALT.item(), SlimefunItems.SULFATE.item(),
                chlorinestack.item(), SlimefunItems.SULFATE.item(), chlorinestack.item()
        };
        SlimefunItem chlorinepellet = new SlimefunItem(group, chlorinepelletstack, RecipeType.ENHANCED_CRAFTING_TABLE, chlorinepelletrecipe);
        chlorine.addItemHandler((ItemUseHandler) playerRightClickEvent -> playerRightClickEvent.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10, 2)));
        //</editor-fold>
        //<editor-fold desc="GASMISSILE">
        SlimefunItemStack missilegasstack = new SlimefunItemStack("MISSILEGAS", Material.GOLDEN_SWORD, Translations.getSpecialName("gasmissile"), Translations.getSpecialLore("gasmissile"));
        ItemStack[] missilegasrecipe = {
                chlorinepelletstack.item(), chlorinepelletstack.item(), chlorinepelletstack.item(),
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem missilegas = new MissileItem(group, missilegasstack, RecipeType.ENHANCED_CRAFTING_TABLE, missilegasrecipe, 13, Translations.getSpecialALore("gasmissile"));
        //</editor-fold>
        //<editor-fold desc="EXCAVATIONMISSILE">
        SlimefunItemStack excabmissilestack = new SlimefunItemStack("MISSILEEXCAV", Material.WOODEN_SWORD, Translations.getSpecialName("excavmissile"), Translations.getSpecialLore("excavmissile"));
        ItemStack[] excabmissilerecipe = {
                explosivepowderstack.item(), warheadstack.item(), explosivepowderstack.item(),
                warheadstack.item(), smallbodystack.item(), warheadstack.item(),
                sugarfuelstack.item(), smallfinstack.item(), sugarfuelstack.item()
        };
        MissileItem excavmissile = new MissileItem(group, excabmissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, excabmissilerecipe, 14, Translations.getSpecialALore("excavmissile"));
        //</editor-fold>
        //<editor-fold desc="COBWEBMISSILE">
        SlimefunItemStack stickymissilestack = new SlimefunItemStack("MISSILESTICK", Material.IRON_SWORD, Translations.getSpecialName("stickymissile"), Translations.getSpecialLore("stickymissile"));
        ItemStack[] stickymissilerecipe = {
                new ItemStack(Material.STRING), new ItemStack(Material.STRING), new ItemStack(Material.STRING),
                sugarfuelstack.item(), missilebodystack.item(), sugarfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem stickymissile = new MissileItem(group, stickymissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, stickymissilerecipe, 15, Translations.getSpecialALore("stickymissile"));
        //</editor-fold>
        //<editor-fold desc="ADVANCEDMISSILEBODY">
        SlimefunItemStack advancedmissilebodystack = new SlimefunItemStack("ADVANCEDMISSILEBODY", Material.GRAY_CONCRETE, Translations.getMaterialName("advancedbody"), Translations.getMaterialLore("advancedbody"));
        ItemStack[] advancedmissilebodyrecipe = {
                rocketfuelstack.item(), simpleflightcomputerstack.item(), rocketfuelstack.item(),
                ultraliteplatestack.item(), missilebodystack.item(), ultraliteplatestack.item(),
                rocketfuelstack.item(), simpleflightcomputerstack.item(), rocketfuelstack.item()
        };

        SlimefunItem advancedmissilebody = new SlimefunItem(group, advancedmissilebodystack, RecipeType.ENHANCED_CRAFTING_TABLE, advancedmissilebodyrecipe);

        missilebody.setRecipeOutput(missilebodystacks.item());

        //</editor-fold>
        //<editor-fold desc="LARGEWARHEAD">
        SlimefunItemStack heavywarheadstack = new SlimefunItemStack("HEAVYWARHEAD", Material.RED_CONCRETE, Translations.getMaterialName("heavywarhead"), Translations.getMaterialLore("heavywarhead"));
        ItemStack[] heavywarheadrecipe = {
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item(),
                warheadstack.item(), ultraliteplatestack.item(), warheadstack.item(),
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item()
        };

        SlimefunItem heavywarhead = new SlimefunItem(group, heavywarheadstack, RecipeType.ENHANCED_CRAFTING_TABLE, heavywarheadrecipe);
        //</editor-fold>
        //<editor-fold desc="ICBMMISSILEBODY">
        SlimefunItemStack icbmmissilebodystack = new SlimefunItemStack("ICBMMISSILEBODY", Material.GREEN_CONCRETE, Translations.getMaterialName("icbmbody"), Translations.getMaterialLore("icbmbody"));
        ItemStack[] icbmmissilebodyrecipe = {
                compressedpowderstack.item(), SlimefunItems.NEPTUNIUM.item(), compressedpowderstack.item(),
                SlimefunItems.NEPTUNIUM.item(), advancedmissilebodystack.item(), SlimefunItems.NEPTUNIUM.item(),
                compressedpowderstack.item(), SlimefunItems.NEPTUNIUM.item(), compressedpowderstack.item()
        };

        SlimefunItem icbmmissilebody = new SlimefunItem(group, icbmmissilebodystack, RecipeType.ENHANCED_CRAFTING_TABLE, icbmmissilebodyrecipe);

        missilebody.setRecipeOutput(missilebodystacks.item());

        //</editor-fold>
        //<editor-fold desc="ICBM">
        SlimefunItemStack icbmstack = new SlimefunItemStack("MISSILEICBM", Material.DIAMOND_SWORD, Translations.getSpecialName("icbm"), Translations.getSpecialLore("icbm"));
        ItemStack[] icbmrecipe = {
                rocketfuelstack.item(), heavywarheadstack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), icbmmissilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), SlimefunItems.STEEL_THRUSTER.item(), rocketfuelstack.item()
        };
        MissileItem icbm = new MissileItem(group, icbmstack, RecipeType.ENHANCED_CRAFTING_TABLE, icbmrecipe, 16, Translations.getSpecialALore("icbm"));
        //</editor-fold>
        //<editor-fold desc="CLUSTERMISSILE">
        SlimefunItemStack clusterstack = new SlimefunItemStack("MISSILECLUSTER", Material.GOLDEN_SWORD, Translations.getSpecialName("clustermissile"), Translations.getSpecialLore("clustermissile"));
        ItemStack[] clusterrecipe = {
                warheadstack.item(), heavywarheadstack.item(), warheadstack.item(),
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem cluster = new MissileItem(group, clusterstack, RecipeType.ENHANCED_CRAFTING_TABLE, clusterrecipe, 17, Translations.getSpecialALore("clustermissile"));
        //</editor-fold>
        //<editor-fold desc="NAPALMMISSILE">
        SlimefunItemStack napalmmissilestack = new SlimefunItemStack("MISSILENAPALM", Material.GOLDEN_SWORD, Translations.getSpecialName("napalmmissile"), Translations.getSpecialLore("napalmmissile"));
        ItemStack[] napalmmissilerecipe = {
                new ItemStack(Material.FIRE_CHARGE), heavywarheadstack.item(), new ItemStack(Material.FIRE_CHARGE),
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem napalmmissile = new MissileItem(group, napalmmissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, napalmmissilerecipe, 18, Translations.getSpecialALore("napalmmissile"));
        //</editor-fold>
        Translations.setType("advanced");
        //<editor-fold desc="ADVANCEDMISSILE">
        SlimefunItemStack advmissilestack = new SlimefunItemStack("MISSILEADV", Material.DIAMOND_SWORD, Translations.getMissileName("normal"), Translations.getTypeLore(), Translations.getMissileVariant("normal"));
        ItemStack[] advmissilerecipe = {
                null, warheadstack.item(), null,
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item(),
        };
        MissileItem advmissile = new MissileItem(group, advmissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, advmissilerecipe, 19, Translations.getMissileLore("normal"));
        //</editor-fold>
        //<editor-fold desc="ADVANCEDMISSILEHE">
        SlimefunItemStack advmissileHEstack = new SlimefunItemStack("MISSILEHEADV", Material.DIAMOND_SWORD, Translations.getMissileName("he"), Translations.getTypeLore(), Translations.getMissileVariant("he"));
        ItemStack[] advmissileHErecipe = {
                compressedpowderstack.item(), heavywarheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item(),
        };
        MissileItem advmissileHE = new MissileItem(group, advmissileHEstack, RecipeType.ENHANCED_CRAFTING_TABLE, advmissileHErecipe, 20, Translations.getMissileLore("he"));
        //</editor-fold>
        //<editor-fold desc="ADVANCEDMISSILELR">
        SlimefunItemStack advmissileLRstack = new SlimefunItemStack("MISSILELRADV", Material.DIAMOND_SWORD, Translations.getMissileName("lr"), Translations.getTypeLore(), Translations.getMissileVariant("lr"));
        ItemStack[] advmissileLRrecipe = {
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                SlimefunItems.BASIC_CIRCUIT_BOARD.item(), finsstack.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(),
        };
        MissileItem advmissileLR = new MissileItem(group, advmissileLRstack, RecipeType.ENHANCED_CRAFTING_TABLE, advmissileLRrecipe, 21, Translations.getMissileLore("lr"));
        //</editor-fold>
        //<editor-fold desc="ADVANCEDMISSILEAC">
        SlimefunItemStack advmissileACstack = new SlimefunItemStack("MISSILEACADV", Material.DIAMOND_SWORD, Translations.getMissileName("ac"), Translations.getTypeLore(), Translations.getMissileVariant("ac"));
        ItemStack[] advmissileACrecipe = {
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                finsstack.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), finsstack.item(),
        };
        MissileItem advmissileAC = new MissileItem(group, advmissileACstack, RecipeType.ENHANCED_CRAFTING_TABLE, advmissileACrecipe, 22, Translations.getMissileLore("ac"));
        //</editor-fold>
        //</editor-fold>
        //Register All
        //<editor-fold desc="== Register Items ==">
        //Guides
        guide.register(main);
        playerList.register(main);
        //Missile Launchers
        groundlauncher.register(main);
        antiElytraLauncher.register(main);
        antiairlauncher.register(main);
        //Other Blocks
        ItemStack[] missileradarrecipe = new ItemStack[]{
                simpleflightcomputerstack.item(), ultraliteplatestack.item(), simpleflightcomputerstack.item(),
                ultraliteplatestack.item(), radarstack.item(), ultraliteplatestack.item(),
                simpleflightcomputerstack.item(), ultraliteplatestack.item(), simpleflightcomputerstack.item(),
        };
        new MissileRadar(group, missileradarrecipe).register(main);
        mine.register(main);
        //Handhelds
        manpad.register(main);
        // Materials
        sugarfuel.register(main);
        explosivepowder.register(main);
        compressedpowder.register(main);
        ultraliteingot.register(main);
        ultraliteplate.register(main);
        chlorine.register(main);
        chlorinepellet.register(main);
        simpleflightcomputer.register(main);
        radar.register(main);
        rocketfuel.register(main);
        //Missile Parts
        smallwarhead.register(main);
        warhead.register(main);
        warheadAP.register(main);
        smallbody.register(main);
        smallfin.register(main);
        missilebody.register(main);
        fins.register(main);
        advancedmissilebody.register(main);
        heavywarhead.register(main);
        icbmmissilebody.register(main);
        //Missile Types
        antiAirMissile.register(main);
        antielytramissile.register(main);
        smallmissile.register(main);
        smallmissileHE.register(main);
        smallmissileLR.register(main);
        smallmissileAC.register(main);
        missile.register(main);
        missileHE.register(main);
        missileLR.register(main);
        missileAC.register(main);
        missileAP.register(main);
        missileAPt.register(main);
        missileAPtr.register(main);
        missilegas.register(main);
        excavmissile.register(main);
        stickymissile.register(main);
        icbm.register(main);
        cluster.register(main);
        napalmmissile.register(main);
        advmissile.register(main);
        advmissileHE.register(main);
        advmissileLR.register(main);
        advmissileAC.register(main);

        //</editor-fold>

        //ADD RESEARCH
        //<editor-fold desc="RESEARCH">
        NamespacedKey basicfuelkey = new NamespacedKey(main, "basic_fuel");
        Research basicfuel = new Research(basicfuelkey, 3467341, "Inedible Sugar", 10);
        basicfuel.addItems(sugarfuel);
        basicfuel.register();

        NamespacedKey explosiveskey = new NamespacedKey(main, "explosives");
        Research explosives = new Research(explosiveskey, 3447321, "Explosive Diarrhea", 15);
        explosives.addItems(explosivepowder, compressedpowder);
        explosives.register();

        NamespacedKey chlorinekey = new NamespacedKey(main, "chlorine");
        Research chlorineres = new Research(chlorinekey, 214141, "Cleaner Pools!", 15);
        chlorineres.addItems(chlorine, chlorinepellet);
        chlorineres.register();

        NamespacedKey groundlauncherskey = new NamespacedKey(main, "groundlauncher");
        Research groundlauncherres = new Research(groundlauncherskey, 34117322, "Ground Missile Launcher", 15);
        groundlauncherres.addItems(groundlauncher);
        groundlauncherres.register();

        NamespacedKey antimissilekey = new NamespacedKey(main, "antimissilelauncher");
        Research antimissileres = new Research(antimissilekey, 3424321, "Iron Dome.", 15);
        antimissileres.addItems(antiairlauncher, manpad, antiAirMissile);
        antimissileres.register();

        NamespacedKey antielytramissilekey = new NamespacedKey(main, "antielytramissilelauncher");
        Research antielytramissileres = new Research(antielytramissilekey, 34213253, "Ender Dome?", 20);
        antielytramissileres.addItems(antiElytraLauncher, antielytramissile);
        antielytramissileres.register();

        NamespacedKey smallgmissilepartskey = new NamespacedKey(main, "smallgmissileparts");
        Research smallgmissileparts = new Research(smallgmissilepartskey, 2667313, "Missile with extra steps", 15);
        smallgmissileparts.addItems(smallwarhead, smallbody, smallfin);
        smallgmissileparts.register();

        NamespacedKey gmissilepartskey = new NamespacedKey(main, "gmissileparts");
        Research gmissileparts = new Research(gmissilepartskey, 2667313, "Missile with extra steps", 15);
        gmissileparts.addItems(warhead, warheadAP, missilebody, fins);
        gmissileparts.register();

        NamespacedKey smallgmissilekey = new NamespacedKey(main, "smallgmissile");
        Research smallgmissile = new Research(smallgmissilekey, 35673323, "5 Shades Of Gray", 20);
        smallgmissile.addItems(smallmissile, smallmissileHE, smallmissileLR, smallmissileLR, smallmissileAC);
        smallgmissile.register();

        NamespacedKey advancedfuelkey = new NamespacedKey(main, "advancedfuel");
        Research advancedfuel = new Research(advancedfuelkey, 3461423, "Advanced (and even less edible) Fuels!", 20);
        advancedfuel.addItems(rocketfuel);
        advancedfuel.register();

        NamespacedKey missilepartskey = new NamespacedKey(main, "missileparts");
        Research missileparts = new Research(missilepartskey, 4461423, "Missile with even more steps", 25);
        missileparts.addItems(ultraliteingot, ultraliteplate, simpleflightcomputer, radar);
        missileparts.register();

        NamespacedKey gmissilekey = new NamespacedKey(main, "gmissile");
        Research gmissile = new Research(gmissilekey, 341243, "The Colors Of The Rainbow!", 20);
        gmissile.addItems(missile, missileHE, missileLR, missileAC);

        NamespacedKey gmissileAPkey = new NamespacedKey(main, "gmissileAP");
        Research gmissileAP = new Research(gmissileAPkey, 341246, "The Penetrator Trio", 15);
        gmissileAP.addItems(missileAP, missileAPt, missileAPtr);

        NamespacedKey gmissileGASkey = new NamespacedKey(main, "gmissileGAS");
        Research gmissileGAS = new Research(gmissileGASkey, 341226, "One Ball Man", 15);
        gmissileGAS.addItems(missilegas);
        //</editor-fold>
    }
}
