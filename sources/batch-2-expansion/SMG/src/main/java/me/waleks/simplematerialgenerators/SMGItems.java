package me.waleks.simplematerialgenerators;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import dev.drake.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public final class SMGItems {

    private SMGItems() {}

    public static final ItemGroup SMG_ITEM_CATEGORY = new ItemGroup(
        new NamespacedKey(SimpleMaterialGenerators.getInstance(), "simplematerialgenerators"),
        new CustomItemStack(Material.SMOOTH_STONE, "&9Simple Material Generators")
    );

    public static final SlimefunItemStack SMG_GENERATOR_MULTIBLOCK = new SlimefunItemStack(
        "SMG_GENERATOR_MULTIBLOCK",
        Material.BEDROCK,
        "&9Generator multiblock",
        "",
        "&dBuild any of this addon's",
        "&dgenerators like this.",
        "&aThey will only output to a chest",
        "&adirectly above it."
    );
	
	/*	Template for adding more generator items
	public static final SlimefunItemStack SMG_GENERATOR_ = new SlimefunItemStack(
		"SMG_GENERATOR_",
		Material.,
		"& generator",
		"&6Rate: &e ticks",
		"",
		"&9&oSimpleMaterialGenerators"
	);
	*/

    public static final SlimefunItemStack SMG_GENERATOR_COBBLESTONE = new SlimefunItemStack(
        "SMG_GENERATOR_COBBLESTONE",
        Material.COBBLESTONE,
        "&7Cobblestone generator",
        "&6Rate: &e4 ticks",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_STONE_BROKEN = new SlimefunItemStack(
        "SMG_GENERATOR_STONE_BROKEN",
        Material.STONE,
        "&7Stone generator &8(Broken)",
        "&8Needs to be repaired",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_STONE = new SlimefunItemStack(
        "SMG_GENERATOR_STONE",
        Material.STONE,
        "&7Stone generator",
        "&6Rate: &e8 ticks",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_SMOOTH_STONE_BROKEN = new SlimefunItemStack(
        "SMG_GENERATOR_SMOOTH_STONE_BROKEN",
        Material.SMOOTH_STONE,
        "&7Smooth stone generator &8(Broken)",
        "&8Needs to be repaired",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_SMOOTH_STONE = new SlimefunItemStack(
        "SMG_GENERATOR_SMOOTH_STONE",
        Material.SMOOTH_STONE,
        "&7Smooth stone generator",
        "&6Rate: &e12 ticks",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_GRAVEL_BROKEN = new SlimefunItemStack(
        "SMG_GENERATOR_GRAVEL_BROKEN",
        Material.GRAVEL,
        "&7Gravel generator &8(Broken)",
        "&8Needs to be repaired",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_GRAVEL = new SlimefunItemStack(
        "SMG_GENERATOR_GRAVEL",
        Material.ANDESITE,
        "&7Gravel generator",
        "&6Rate: &e6 ticks",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_SAND_BROKEN = new SlimefunItemStack(
        "SMG_GENERATOR_SAND_BROKEN",
        Material.SAND,
        "&eSand generator &8(Broken)",
        "&8Needs to be repaired",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_SAND = new SlimefunItemStack(
        "SMG_GENERATOR_SAND",
        Material.SANDSTONE,
        "&eSand generator",
        "&6Rate: &e8 ticks",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_GLASS = new SlimefunItemStack(
        "SMG_GENERATOR_GLASS",
        Material.GLASS,
        "&fGlass generator",
        "&6Rate: &e12 ticks",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_NETHERRACK = new SlimefunItemStack(
        "SMG_GENERATOR_NETHERRACK",
        Material.NETHERRACK,
        "&cNetherrack generator",
        "&6Rate: &e6 ticks",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_SOUL_SAND = new SlimefunItemStack(
        "SMG_GENERATOR_SOUL_SAND",
        Material.SOUL_SAND,
        "&8Soul sand generator",
        "&6Rate: &e8 ticks",
        "",
        "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_REDSTONE_BADLY_FORMED = new SlimefunItemStack(
            "SMG_GENERATOR_REDSTONE_BADLY_FORMED",
            Material.REDSTONE_BLOCK,
            "&cRedstone generator &8(Badly Formed)",
            "&8Needs to be reformed",
            "",
            "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_REDSTONE_BROKEN = new SlimefunItemStack(
            "SMG_GENERATOR_REDSTONE_BROKEN",
            Material.REDSTONE_BLOCK,
            "&cRedstone generator &8(Broken)",
            "&8Needs to be repaired",
            "",
            "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_REDSTONE = new SlimefunItemStack(
            "SMG_GENERATOR_REDSTONE",
            Material.REDSTONE_BLOCK,
            "&cRedstone generator",
            "&6Rate: &e24 ticks",
            "",
            "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_OBSIDIAN_BADLY_FORMED = new SlimefunItemStack(
            "SMG_GENERATOR_OBSIDIAN_BADLY_FORMED",
            Material.OBSIDIAN,
            "&5Obsidian generator &8(Badly Formed)",
            "&8Needs to be reformed",
            "",
            "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_OBSIDIAN_BROKEN = new SlimefunItemStack(
            "SMG_GENERATOR_OBSIDIAN_BROKEN",
            Material.OBSIDIAN,
            "&5Obsidian generator &8(Broken)",
            "&8Needs to be repaired",
            "",
            "&9&oSimpleMaterialGenerators"
    );

    public static final SlimefunItemStack SMG_GENERATOR_OBSIDIAN = new SlimefunItemStack(
            "SMG_GENERATOR_OBSIDIAN",
            Material.OBSIDIAN,
            "&5Obsidian generator",
            "&6Rate: &e128 ticks",
            "",
            "&9&oSimpleMaterialGenerators"
    );
}