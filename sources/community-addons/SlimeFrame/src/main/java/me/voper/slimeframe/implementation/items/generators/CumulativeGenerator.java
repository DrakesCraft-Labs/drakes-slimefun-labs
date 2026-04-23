package me.voper.slimeframe.implementation.items.generators;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;

import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.abstracts.AbstractEnergyGenerator;
import me.voper.slimeframe.utils.MachineUtils;

import lombok.Getter;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenuPreset;

@Getter
public class CumulativeGenerator extends AbstractEnergyGenerator {

    private int bonusEnergy = 0;
    private final List<BlockFace> facesToCheck = new ArrayList<>(List.of(
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.UP,
            BlockFace.DOWN
    ));

    public CumulativeGenerator(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(Groups.GENERATORS, item, recipeType, recipe);
    }

    @Override
    protected void createMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
        preset.addItem(4, MachineUtils.STATUS);
    }

    @Override
    public int getGeneratedOutput(BlockMenu menu, Block block) {
        return checkRelatives(block) + energyGenerated;
    }

    private int checkRelatives(Block block) {
        int bonus = 0;

        for (BlockFace face : facesToCheck) {
            SlimefunItem check = BlockStorage.check(block.getRelative(face));
            if (check == null) continue;
            if (check.getId().equals(this.getId())) {
                bonus += bonusEnergy;
            }
        }

        return bonus;
    }

    @Override
    public int getStatusSlot() {
        return 4;
    }

    public CumulativeGenerator setBonusEnergy(int bonusEnergy) {
        Preconditions.checkArgument(bonusEnergy > 0, "Bonus energy must be greater than zero");
        this.bonusEnergy = bonusEnergy;
        return this;
    }

    @Override
    public void register(@Nonnull SlimefunAddon addon) {
        if (energyCapacity == -1) energyCapacity = (energyGenerated + (bonusEnergy * facesToCheck.size())) * 10;
        if (bonusEnergy * facesToCheck.size() + energyGenerated > energyCapacity) {
            warn("The energy generated with all the bonuses exceeds the energy capacity. The Item was disabled");
            return;
        }
        super.register(addon);
    }

}