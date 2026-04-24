package me.lucasgithuber.elementmanipulation.machines;

import dev.drake.infinitylib.machines.MachineLayout;
import dev.drake.infinitylib.machines.MachineRecipeType;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import dev.drake.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import me.lucasgithuber.elementmanipulation.category.DrugsGroup;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class DrugsTable extends dev.drake.infinitylib.machines.CraftingBlock implements EnergyNetComponent {

    public static final int[] INPUT_SLOTS = {
            2,3,4,5,6,7,
            11,12,13,14,15,16,
            20,21,22,23,24,25,
            29,30,31,32,33,34,
            38,39,40,41,42,43,
            47,48,49,50,51,52
    };
    private static final int RECIPE_SLOT = 0;
    public static final MachineRecipeType TYPE = new MachineRecipeType("em_drugs_table",
            new CustomItemStack(Machines.DRUGS_TABLE, Machines.DRUGS_TABLE.getDisplayName(),
                    "", "&cUse the drugs category to see the correct recipe!"));

    private final int energy;

    public DrugsTable(ItemGroup category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe, int energy) {
        super(category, item, type, recipe);
        addRecipesFrom(TYPE);
        setLayout(new MachineLayout()
                .inputSlots(INPUT_SLOTS)
                .outputSlots(new int[]{
                        45
                })
                .statusSlot(27)
                .inputBorder(new int[]{
                        1,10,19,28,37,46,8,17,26,35,44,53
                })
                .outputBorder(new int[]{
                        36
                }).background(new int[]{9,18})
        );
        this.energy = energy;
    }

    @Override
    protected void setup(BlockMenuPreset preset) {
        super.setup(preset);
        preset.addItem(0, new CustomItemStack(Material.KNOWLEDGE_BOOK, ChatColor.GREEN + "Recipes"), ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    protected void onNewInstance(BlockMenu menu, Block b) {
        super.onNewInstance(menu, b);
        menu.addMenuClickHandler(RECIPE_SLOT, (p, slot, item, action) -> {
            DrugsGroup.open(p, menu);
            return false;
        });
    }

    @Override
    protected void craft(Block b, BlockMenu menu, Player p) {
        int charge = getCharge(menu.getLocation());
        if (charge < this.energy) {
            p.sendMessage(
                    ChatColor.RED + "Not enough energy!",
                    ChatColor.GREEN + "Charge: " + ChatColor.RED + charge + ChatColor.GREEN + "/" + this.energy + " J"
            );
        } else {
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
