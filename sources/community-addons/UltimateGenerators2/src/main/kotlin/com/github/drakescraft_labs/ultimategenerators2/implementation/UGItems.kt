package net.guizhanss.ultimategenerators2.implementation

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import dev.drake.dough.items.CustomItemStack
import net.guizhanss.ultimategenerators2.UltimateGenerators2
import net.guizhanss.ultimategenerators2.utils.consts.Heads
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.FireworkEffectMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta

object UGItems {
    val ENDLESS_GENERATOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ENDLESS_GENERATOR",
        Heads.ENDLESS_GENERATOR.texture
    )

    val DIESEL_REFINERY: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "DIESEL_REFINERY",
        Material.PISTON
    )

    val DIESEL_BUCKET: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "DIESEL_BUCKET",
        Heads.DIESEL_BUCKET.texture
    )

    val DIESEL_GENERATOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "DIESEL_GENERATOR",
        Heads.DIESEL_GENERATOR.texture
    )

    val BIOMASS_EXTRACTION_MACHINE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "BIOMASS_EXTRACTION_MACHINE",
        Material.LIME_STAINED_GLASS
    )

    val BIOMASS_BUCKET: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "BIOMASS_BUCKET",
        Heads.BIOMASS_BUCKET.texture
    )

    val BIOFUEL_REFINERY: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "BIOFUEL_REFINERY",
        Material.PISTON
    )

    val BIOFUEL_BUCKET: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "BIOFUEL_BUCKET",
        Heads.BIOFUEL_BUCKET.texture
    )

    val BIOFUEL_GENERATOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "BIOFUEL_GENERATOR",
        Material.YELLOW_STAINED_GLASS
    )

    val DRAGON_BREATH_GENERATOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "DRAGON_BREATH_GENERATOR",
        Material.MAGENTA_STAINED_GLASS
    )

    val REACTION_GENERATOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "REACTION_GENERATOR",
        Material.GREEN_TERRACOTTA
    )

    val ENDER_CRYSTAL_GENERATOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ENDER_CRYSTAL_GENERATOR",
        Material.PURPLE_STAINED_GLASS
    )

    val ENDER_CRYSTAL_GENERATOR_BASE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ENDER_CRYSTAL_GENERATOR_BASE",
        Material.END_STONE_BRICKS
    )

    val ENDER_CRYSTAL_GENERATOR_STABILIZER: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ENDER_CRYSTAL_GENERATOR_STABILIZER",
        Material.OBSIDIAN
    )

    val ENDER_CRYSTAL_ENHANCER: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ENDER_CRYSTAL_ENHANCER",
        CustomItemStack(Material.POTION) { im: ItemMeta ->
            val meta = im as PotionMeta
            meta.clearCustomEffects()
            meta.color = Color.fromRGB(85, 0, 255)
        }
    )

    val MODULAR_GENERATOR_REGULATOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "MODULAR_GENERATOR_REGULATOR",
        Heads.MODULAR_GENERATOR_REGULATOR.texture
    )

    val SOLID_STORAGE_EXPANSION: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "SOLID_STORAGE_EXPANSION",
        Heads.SOLID_STORAGE_EXPANSION.texture
    )

    val LIQUID_STORAGE_EXPANSION: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "LIQUID_STORAGE_EXPANSION",
        Heads.LIQUID_STORAGE_EXPANSION.texture
    )

    val ELECTRICITY_STORAGE_UNIT: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ELECTRICITY_STORAGE_UNIT",
        Heads.ELECTRICITY_STORAGE_UNIT.texture
    )

    val ADVANCED_BATTERY: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ADVANCED_BATTERY",
        Heads.ADVANCED_BATTERY.texture
    )

    val ALPHA_BATTERY: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ALPHA_BATTERY",
        Heads.ALPHA_BATTERY.texture
    )

    val BETA_BATTERY: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "BETA_BATTERY",
        Heads.BETA_BATTERY.texture
    )

    val GAMMA_BATTERY: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "GAMMA_BATTERY",
        Heads.GAMMA_BATTERY.texture
    )

    val BASIC_ELECTRICITY_STORAGE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "BASIC_ELECTRICITY_STORAGE",
        Material.YELLOW_STAINED_GLASS
    )

    val ADVANCED_ELECTRICITY_STORAGE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ADVANCED_ELECTRICITY_STORAGE",
        Material.ORANGE_STAINED_GLASS
    )

    val ALPHA_ELECTRICITY_STORAGE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ALPHA_ELECTRICITY_STORAGE",
        Material.LIME_STAINED_GLASS
    )

    val BETA_ELECTRICITY_STORAGE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "BETA_ELECTRICITY_STORAGE",
        Material.BLUE_STAINED_GLASS
    )

    val GAMMA_ELECTRICITY_STORAGE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "GAMMA_ELECTRICITY_STORAGE",
        Material.MAGENTA_STAINED_GLASS
    )

    val LAMBDA_ELECTRICITY_STORAGE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "LAMBDA_ELECTRICITY_STORAGE",
        Material.PINK_STAINED_GLASS
    )

    val KAPA_ELECTRICITY_STORAGE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "KAPA_ELECTRICITY_STORAGE",
        Material.BLACK_STAINED_GLASS
    )

    val PHI_ELECTRICITY_STORAGE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "PHI_ELECTRICITY_STORAGE",
        Material.WHITE_STAINED_GLASS
    )

    val QUANTUM_SOLAR_GENERATOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "QUANTUM_SOLAR_GENERATOR",
        Material.DAYLIGHT_DETECTOR
    )

    val RUNE_COMPLEX_ENDER: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "RUNE_COMPLEX_ENDER",
        CustomItemStack(Material.FIREWORK_STAR) { im: ItemMeta ->
            val meta = im as FireworkEffectMeta
            meta.effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST)
                .withColor(Color.fromRGB(85, 0, 255)).build()
        }
    )

    val ENDER_LUMP_4: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "ENDER_LUMP_4",
        Material.GOLD_NUGGET
    )

    val HEAVY_WATER_BUCKET: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "HEAVY_WATER_BUCKET",
        Material.WATER_BUCKET
    )

    val HEAVY_WATER_REFINING_MACHINE: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "HEAVY_WATER_REFINING_MACHINE",
        Material.GRAY_STAINED_GLASS
    )

    val NEUTRON_MODERATOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "NEUTRON_MODERATOR",
        CustomItemStack(Material.POTION) { im: ItemMeta ->
            val meta = im as PotionMeta
            meta.clearCustomEffects()
            meta.color = Color.BLACK
        }
    )

    val THERMAL_NEUTRON_REACTOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "THERMAL_NEUTRON_REACTOR",
        Heads.THERMAL_NEUTRON_REACTOR.texture
    )

    val THERMAL_NEUTRON_REACTOR_COOLANT_CELL: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "THERMAL_NEUTRON_REACTOR_COOLANT_CELL",
        Heads.THERMAL_NEUTRON_REACTOR_COOLANT_CELL.texture
    )

    val RAINBOW_ALLOY: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "RAINBOW_ALLOY",
        Heads.RAINBOW_ALLOY.texture
    )

    val REINFORCED_RAINBOW_GLASS: SlimefunItemStack = glow(
        UltimateGenerators2.localization().getItem(
            "REINFORCED_RAINBOW_GLASS",
            Material.MAGENTA_STAINED_GLASS
        )
    )

    val RAINBOW_REACTOR: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "RAINBOW_REACTOR",
        Material.PINK_STAINED_GLASS
    )

    val GLASS_ELECTRICITY_TRANSMITTER: SlimefunItemStack = UltimateGenerators2.localization().getItem(
        "GLASS_ELECTRICITY_TRANSMITTER",
        Material.CYAN_STAINED_GLASS
    )

    private fun glow(sfItemStack: SlimefunItemStack): SlimefunItemStack {
        sfItemStack.addUnsafeEnchantment(Enchantment.UNBREAKING, 1)
        val im = sfItemStack.itemMeta
        im!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        sfItemStack.setItemMeta(im)
        return sfItemStack
    }
}
