package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.machine.sf;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft-labs.slimefun4.implementation.items.electric.machines.accelerators.AbstractGrowthAccelerator;
import com.github.drakescraft-labs.slimefun4.utils.SlimefunUtils;
import com.github.drakescraft-labs.slimefun4.utils.itemstack.ItemStackWrapper;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class AdvancedAnimalGrowthAccelerator extends AbstractGrowthAccelerator {
    private static final ItemStack organicFood = ItemStackWrapper.wrap(SlimefunItems.ORGANIC_FOOD.clone());

    private final int capacity;
    private final int radius;
    private final int energy_consumption;

    public AdvancedAnimalGrowthAccelerator(
            ItemGroup itemGroup,
            SlimefunItemStack item,
            RecipeType recipeType,
            ItemStack[] recipe,
            int capacity,
            int radius,
            int energy_consumption) {
        super(itemGroup, item, recipeType, recipe);

        this.capacity = capacity;
        this.radius = radius;
        this.energy_consumption = energy_consumption;
    }

    protected void tick(Block b) {
        BlockMenu inv = BlockStorage.getInventory(b);

        if (inv != null) {
            for (Entity n :
                    b.getWorld().getNearbyEntities(b.getLocation(), radius, radius, radius, this::isReadyToGrow)) {
                int[] var5 = this.getInputSlots();

                for (int slot : var5) {
                    if (SlimefunUtils.isItemSimilar(inv.getItemInSlot(slot), organicFood, false, false)) {
                        if (this.getCharge(b.getLocation()) < energy_consumption) {
                            return;
                        }

                        Ageable ageable = (Ageable) n;
                        this.removeCharge(b.getLocation(), energy_consumption);
                        inv.consumeItem(slot);
                        ageable.setAge(ageable.getAge() + 2000);
                        if (ageable.getAge() > 0) {
                            ageable.setAge(0);
                        }

                        n.getWorld()
                                .spawnParticle(
                                        Particle.HAPPY_VILLAGER,
                                        ((LivingEntity) n).getEyeLocation(),
                                        8,
                                        0.20000000298023224,
                                        0.20000000298023224,
                                        0.20000000298023224);
                        return;
                    }
                }
            }
        }
    }

    private boolean isReadyToGrow(Entity n) {
        if (n instanceof Ageable ageable) {
            return n.isValid() && !ageable.isAdult();
        }

        return false;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }
}
