package com.github.drakescraft_labs.customitemgen.file

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.customitemgen.CustomItemGenerators
import com.github.drakescraft_labs.customitemgen.generator.ItemGenerator
import com.github.drakescraft_labs.customitemgen.data.Options
import com.github.drakescraft_labs.customitemgen.registry.MachineRegistry
import com.github.drakescraft_labs.customitemgen.registry.RecipeRegistry
import com.github.drakescraft_labs.customitemgen.util.*
import org.bstats.charts.AdvancedPie
import org.bstats.charts.SimplePie
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File

object MachineLoader {

    fun load(file: File) {
        val machines = YamlConfiguration()
        machines.load(file)

        val keys = machines.getKeys(false)
        for (id in keys) {
            if (!machines.getBoolean("$id.enabled", true)) {
                continue
            }

            val stack = machines.getBlock("$id.item")
            val machineItem = SlimefunItemStack(id, stack)

            val energyCapacity = machines.getInt("$id.energy-capacity")

            val recipeTypeString = machines.getString("$id.recipe.type") ?: throw RuntimeException("$id machine recipe type not specified")
            val recipeType = RecipeRegistry[recipeTypeString] ?: throw RuntimeException("$id machine recipe type not found")

            val recipe = machines.getRecipe(id)
            if (recipe.isEmpty()) throw RuntimeException("$id has invalid/empty recipe")

            val production = machines.getProduction(id)
            if (production.isEmpty()) throw RuntimeException("$id has invalid/empty production list")

            val maxProduction = production.maxOf { it.energy }
            if (energyCapacity < maxProduction) throw RuntimeException("$id the energy-capacity must always be above the energy production cost")

            val options = loadOptions(machines, id)
            val generator = ItemGenerator(CustomItemGenerators.group, machineItem, recipeType, recipe, options, production)
            generator
                .setCapacity(energyCapacity)
                .register(CustomItemGenerators.addon)

            generator.load() //have to post load it myself
            MachineRegistry[id] = generator
        }

        registerMetrics()
    }

    private fun loadOptions(machines: YamlConfiguration, id: String) : Options {
        val progressBarString = machines.getString("$id.progress-bar") ?: throw RuntimeException("$id machine progress bar entry not found")
        val progressBarMaterial = Material.getMaterial(progressBarString) ?: throw RuntimeException("$id machine progress bar material not found")

        val entryRandomizer = machines.getBoolean("$id.entry-randomizer", false)
        val validators = machines.getConditions(id)

        return Options(
            entryRandomizer = entryRandomizer,
            progressBar = ItemStack(progressBarMaterial),
            validators = validators
        )
    }

    private fun registerMetrics() {
        val metrics = CustomItemGenerators.metrics

        metrics.addCustomChart(
            SimplePie("custom_generators") {
                MachineRegistry.size.toString()
            }
        )

        metrics.addCustomChart(
            SimplePie("production_entries") {
                MachineRegistry.map { it.value }.sumOf { it.production.size }.toString()
            }
        )

        metrics.addCustomChart(
            AdvancedPie("generated_items") {
                val hashMap = hashMapOf<String, Int>()

                for(entry in MachineRegistry.values) {
                    for (produceEntry in entry.production) {
                        val item = produceEntry.recipe.input[0]
                        val sfItem = SlimefunItem.getByItem(item)

                        val id = sfItem?.id
                                    ?.lowercase()
                                    ?.capitalizeWords('_')
                            ?: item.type.getDefaultName().content()
                        hashMap[id] = 1
                    }
                }

                hashMap
            }
        )
    }
}