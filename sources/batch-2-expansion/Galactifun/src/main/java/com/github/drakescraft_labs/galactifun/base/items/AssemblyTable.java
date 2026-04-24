package com.github.drakescraft_labs.galactifun.base.items;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.galactifun.base.BaseItems;
import com.github.drakescraft_labs.galactifun.core.CoreItemGroup;
import dev.drake.infinitylib.machines.CraftingBlock;
import dev.drake.infinitylib.machines.MachineLayout;
import dev.drake.infinitylib.machines.MachineRecipeType;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuide;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import dev.drake.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;

@ParametersAreNonnullByDefault
public final class AssemblyTable extends CraftingBlock implements EnergyNetComponent {

    public static final int[] INPUT_SLOTS = {
            0, 1, 2, 3, 4, 5,
            9, 10, 11, 12, 13, 14,
            18, 19, 20, 21, 22, 23,
            27, 28, 29, 30, 31, 32,
            36, 37, 38, 39, 40, 41,
            45, 46, 47, 48, 49, 50
    };
    private static final int RECIPE_SLOT = 7;
    public static final MachineRecipeType TYPE = new MachineRecipeType("assembly_table",
            new CustomItemStack(BaseItems.ASSEMBLY_TABLE, BaseItems.ASSEMBLY_TABLE.getDisplayName(),
                    "", "&cUse the assembly recipes category to see the correct recipe!"));

    private final int energy;

    public AssemblyTable(SlimefunItemStack item, ItemStack[] recipe, int energy) {
        super(CoreItemGroup.MACHINES, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        addRecipesFrom(TYPE);
        setLayout(new MachineLayout()
                .inputSlots(INPUT_SLOTS)
                .outputSlots(new int[] { 43 })
                .statusSlot(16)
                .inputBorder(new int[0])
                .outputBorder(new int[] {
                        33, 34, 35,
                        42, 44,
                        51, 52, 53
                }).background(new int[] {
                        6, 8, 15, 17, 24, 25, 26
                })
        );
        this.energy = energy;
    }

    @Override
    protected void setup(BlockMenuPreset preset) {
        super.setup(preset);
        preset.addItem(RECIPE_SLOT, new CustomItemStack(Material.BOOK, "&6Recipes"), ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    protected void onNewInstance(BlockMenu menu, Block b) {
        super.onNewInstance(menu, b);
        menu.addMenuClickHandler(RECIPE_SLOT, (p, slot, item, action) -> {
            Optional<PlayerProfile> profile = PlayerProfile.find(p);
            if (profile.isEmpty()) {
                PlayerProfile.request(p);
            }
            CoreItemGroup.ASSEMBLY_CATEGORY.open(p, PlayerProfile.find(p).get(), SlimefunGuide.getDefaultMode());
            return false;
        });
    }

    @Override
    protected void craft(Block b, BlockMenu menu, Player p) {
        int charge = getCharge(menu.getLocation());
        if (charge < this.energy) {
            p.sendMessage(
                    ChatColor.RED + "Not enough energy!\n" +
                    ChatColor.GREEN + "Charge: " + ChatColor.RED + charge + ChatColor.GREEN + "/" + this.energy + " J"
            );
        }
        else {
            super.craft(b, menu, p);
        }
    }

    @Override
    protected void onSuccessfulCraft(BlockMenu menu, ItemStack toOutput) {
        setCharge(menu.getLocation(), 0);
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return this.energy;
    }

}
