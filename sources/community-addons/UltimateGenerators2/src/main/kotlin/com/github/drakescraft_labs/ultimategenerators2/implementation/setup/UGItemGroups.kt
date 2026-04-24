package net.guizhanss.ultimategenerators2.implementation.setup

import com.github.drakescraft_labs.slimefun4.api.items.groups.NestedItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.groups.SubItemGroup
import net.guizhanss.ultimategenerators2.UltimateGenerators2
import net.guizhanss.ultimategenerators2.utils.consts.Heads
import net.guizhanss.ultimategenerators2.utils.consts.Keys
import org.bukkit.Material

object UGItemGroups {
    @JvmField
    val MAIN: NestedItemGroup = NestedItemGroup(
        Keys.ITEMGROUP_MAIN,
        UltimateGenerators2.localization().getItem(
            "_UG2_MAIN",
            Heads.ENDLESS_GENERATOR.texture
        )
    )

    @JvmField
    val MISC: SubItemGroup = SubItemGroup(
        Keys.ITEMGROUP_MISC,
        MAIN,
        UltimateGenerators2.localization().getItem(
            "_UG2_MISC",
            Material.COMPARATOR
        ),
        4
    )


    @JvmField
    val MACHINES: SubItemGroup = SubItemGroup(
        Keys.ITEMGROUP_MACHINES,
        MAIN,
        UltimateGenerators2.localization().getItem(
            "_UG2_MACHINES",
            Material.PAPER
        ),
        4
    )

    @JvmField
    val ELECTRICITY_STORAGE: SubItemGroup = SubItemGroup(
        Keys.ITEMGROUP_ELECTRICITY_STORAGE,
        MAIN,
        UltimateGenerators2.localization().getItem(
            "_UG2_ELECTRICITY_STORAGE",
            Material.LIGHT_GRAY_STAINED_GLASS
        ),
        4
    )

    @JvmField
    val SIMPLE_GENERATORS: SubItemGroup = SubItemGroup(
        Keys.ITEMGROUP_SIMPLE_GENERATORS,
        MAIN,
        UltimateGenerators2.localization().getItem(
            "_UG2_SIMPLE_GENERATORS",
            Material.TRIPWIRE_HOOK
        ),
        4
    )

    val MODULAR_GENERATORS: SubItemGroup = SubItemGroup(
        Keys.ITEMGROUP_MODULAR_GENERATORS,
        MAIN,
        UltimateGenerators2.localization().getItem(
            "_UG2_MODULAR_GENERATORS",
            Material.BLACK_SHULKER_BOX
        ),
        4
    )
}
