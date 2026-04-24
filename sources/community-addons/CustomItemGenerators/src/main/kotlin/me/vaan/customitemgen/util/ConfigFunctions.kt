package com.github.drakescraft_labs.customitemgen.util

import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe
import com.github.drakescraft_labs.customitemgen.CustomItemGenerators
import com.github.drakescraft_labs.customitemgen.data.GenEntry
import com.github.drakescraft_labs.customitemgen.data.Validator
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.ItemStack


fun FileConfiguration.getBlock(id: String): ItemStack {
    val blockString = this.getString("$id.block") ?: throw RuntimeException("$id no block found")

    val name = this.getString("$id.name")?.component() ?: Component.text("")
    val lore = this.getStringList("$id.lore").map { it.replace("&", "§").component() }

    val stack: ItemStack

    if (blockString.startsWith("SKULL")) {
        try {
            val skullIDString = blockString.split("_")[1]
            stack = SlimefunUtils.getCustomHead(skullIDString)
        } catch (e: Exception) {
            throw RuntimeException("Error while reading skullID from $id.block, format is: SKULL_ID")
        }
    } else {
        val blockMaterial =
            Material.getMaterial(blockString) ?: throw RuntimeException("$id machine block material not found")
        stack = ItemStack(blockMaterial, 1)
    }

    stack.editMeta {
        it.displayName(name)
        it.lore(lore)
    }

    return stack
}

fun FileConfiguration.getRecipe(id: String): Array<ItemStack?> {
    val stringGrid = this.getStringList("$id.recipe.grid").joinToString("")
    val grid = stringGrid.toCharArray()

    val mapperList = this.getList("$id.recipe.mapper") as List<List<Any>>
    val mapperMap = mapperList.associate {
        val key = (it[0] as String)[0]
        val stringValue = it[1] as String
        val value = stringValue.getItemStack()
        if (value.amount != 1) {
            CustomItemGenerators.instance.getJavaPlugin().logger.warning(
                """
                You created a recipe using an amount different than 1, 
                I am all for the f around and find out, but what you are trying to 
                do is unsupported behaviour, so if the recipe breaks don't blame me ¯\_(ツ)_/¯.""".trimIndent()
            )
        }

        key to value
    }

    return grid.map { mapperMap[it] }.toTypedArray()
}

fun FileConfiguration.getProduction(id: String): MutableList<GenEntry> {
    val productionList = this.getList("$id.production") as List<List<Any>>

    val genList = productionList.map {
        val resultString = it[0] as String
        val result = resultString.getItemStack()
        val time = it[1] as Int

        val recipe = MachineRecipe(time, arrayOf(result), arrayOf(result))

        val energy = it[2] as Int
        GenEntry(recipe, energy)
    }

    return ArrayList(genList)
}

fun FileConfiguration.getConditions(id: String): HashMap<String, Validator> {
    val conditionKeys = this.getConfigurationSection("$id.conditions")
    val validatorList = hashMapOf<String, Validator>()

    conditionKeys ?: return validatorList

    //region Process time range
    val timeRange = conditionKeys.getString("time-range")
    if (timeRange != null) {
        val validate: Validator = { (_, block) ->
            block.world.time in timeRange.parseTimeRange()
        }
        validatorList["time-range"] = validate
    }
    //endregion

    val requiresSkyAccess = conditionKeys.getBoolean("requires-sky-access", false)
    if (requiresSkyAccess) {
        val validate: Validator = { (_, block) ->
            block.y == block.world.getHighestBlockAt(block.location).y
        }

        validatorList["requires-sky-access"] = validate
    }


    return validatorList
}

// After some thinking, this feature might be too hard for users to implement and to test
/*
private fun addBlockValidator(validatorList: HashMap<String, Validator<*>>, conditionKeys: ConfigurationSection) {
    //region Process block relatives

    val blockRelatives = conditionKeys.getStringList("block-relatives")
    val list = mutableListOf<BlockRelative>()
    for (block in blockRelatives) {
        val (x, y, z, item) = block.split(":")
        val stack = item.getItemStack()

        list += BlockRelative(
            Vector(x.toInt(), y.toInt(), z.toInt()), stack.type,
            if (stack.isSlimefun()) {
                stack.getSlimefun<SlimefunItem>()?.id
            } else {
                null
            }
        )
    }


    if (list.isNotEmpty()) {
        val validate = fun(it: Location): Boolean {
            for (element in list) {
                if (!element.check(it)) {
                    return false
                }
            }

            return true
        }

        validatorList["block-relatives"] = validate
    }
    //endregion
}
*/