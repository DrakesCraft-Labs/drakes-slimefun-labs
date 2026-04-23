package me.voper.slimeframe.implementation.items.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.inventory.InvUtils;
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils;
import com.github.drakescraft_labs.slimefun4.utils.itemstack.ItemStackWrapper;

import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.abstracts.AbstractProcessorMachine;
import me.voper.slimeframe.implementation.items.multiblocks.Foundry;
import me.voper.slimeframe.utils.MachineUtils;
import me.voper.slimeframe.utils.RandomItemStacks;

import lombok.Setter;
import lombok.experimental.Accessors;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;

@Accessors(chain = true)
public class Putrifier extends AbstractProcessorMachine implements RecipeDisplayItem {

    private static final RandomItemStacks<ItemStack> SUSPICIOUS_STEWS = new RandomItemStacks<>();

    @Setter
    private int production = 1;

    public Putrifier(SlimefunItemStack item, ItemStack[] recipe) {
        super(Groups.MACHINES, item, Foundry.RECIPE_TYPE, recipe);
    }

    @Override
    protected MachineRecipe findNextRecipe(@Nonnull BlockMenu menu) {
        // Creates a slot-ItemStack map according to the input slots
        Map<Integer, ItemStack> inv = new HashMap<>();

        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);

            if (item != null) {
                inv.put(slot, ItemStackWrapper.wrap(item));
            }
        }

        // If the output slots are full, return null
        int maxedSlots = 0;
        for (int slot : getOutputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            if (item != null && item.getAmount() == item.getMaxStackSize()) {
                maxedSlots += 1;
            }
        }
        if (maxedSlots == getOutputSlots().length) {
            return null;
        }

        // For each recipe, we must check if the input actually matches the recipe input
        Map<Integer, Integer> found = new HashMap<>();
        for (MachineRecipe recipe : recipes) {
            for (ItemStack input : recipe.getInput()) {
                for (int slot : getInputSlots()) {
                    // It also checks the amount of both items
                    if (SlimefunUtils.isItemSimilar(inv.get(slot), input, true)) {
                        found.put(slot, input.getAmount());
                        break;
                    }
                }
            }

            if (found.size() == recipe.getInput().length) {

                for (ItemStack stack : recipe.getInput()) {
                    if (stack.getType() == Material.MUSHROOM_STEW) {
                        recipe = suspiciousStewRecipe();
                        break;
                    }
                }

                if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                    MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.NO_SPACE);
                    return null;
                }

                for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                    menu.consumeItem(entry.getKey(), entry.getValue());
                }

                return recipe;
            } else {
                found.clear();
            }
        }

        return null;
    }

    private MachineRecipe suspiciousStewRecipe() {
        return new MachineRecipe(5, new ItemStack[]{new ItemStack(Material.MUSHROOM_STEW)}, new ItemStack[]{SUSPICIOUS_STEWS.getRandom()});
    }

    @Override
    public void postRegister() {
        registerRecipe(5, new ItemStack(Material.SPIDER_EYE, 3), new ItemStack(Material.FERMENTED_SPIDER_EYE, production));
        registerRecipe(5, new ItemStack(Material.POTATO, 5), new ItemStack(Material.POISONOUS_POTATO, production));
        registerRecipe(5, new ItemStack(Material.BAKED_POTATO, 5), new ItemStack(Material.POISONOUS_POTATO, production));
        registerRecipe(2, new ItemStack(Material.BEEF), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.COOKED_BEEF), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.PORKCHOP), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.COOKED_PORKCHOP), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.CHICKEN), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.COOKED_CHICKEN), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.RABBIT), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.COOKED_RABBIT), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.MUTTON), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.COOKED_MUTTON), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.COD), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.COOKED_COD), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.SALMON), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.COOKED_SALMON), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.TROPICAL_FISH), new ItemStack(Material.ROTTEN_FLESH, production));
        registerRecipe(2, new ItemStack(Material.PUFFERFISH), new ItemStack(Material.ROTTEN_FLESH, production));
        // TODO: Update the recipes system
        registerRecipe(5, new ItemStack(Material.MUSHROOM_STEW), new ItemStack(Material.SUSPICIOUS_STEW));
    }

    @Override
    protected ItemStack getProgressBar() {
        return new ItemStack(Material.SUSPICIOUS_STEW);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        final List<ItemStack> displayRecipes = new ArrayList<>(
                recipes.subList(0, recipes.size() - 2).stream()
                        .flatMap(r -> Stream.of(r.getInput()[0], r.getOutput()[0]))
                        .toList());

        SUSPICIOUS_STEWS.getItems().forEach(item -> {
            displayRecipes.add(new ItemStack(Material.MUSHROOM_STEW));
            displayRecipes.add(item);
        });

        return displayRecipes;
    }

    static {
        // Saturation
        createSuspiciousStew(PotionEffectType.SATURATION, 10, 1);
        // Night vision 5s
        createSuspiciousStew(PotionEffectType.NIGHT_VISION, 5, 1);
        // Fire resistance 4s
        createSuspiciousStew(PotionEffectType.FIRE_RESISTANCE, 4, 1);
        // Blindness 8s
        createSuspiciousStew(PotionEffectType.BLINDNESS, 8, 1);
        // Weakness 9s
        createSuspiciousStew(PotionEffectType.WEAKNESS, 9, 1);
        // Regeneration 8s
        createSuspiciousStew(PotionEffectType.REGENERATION, 8, 1);
        // Jump boost 6s
        createSuspiciousStew(PotionEffectType.JUMP, 6, 1);
        // Poison 12s
        createSuspiciousStew(PotionEffectType.POISON, 12, 1);
        // Wither 8s
        createSuspiciousStew(PotionEffectType.WITHER, 8, 1);
    }

    private static void createSuspiciousStew(PotionEffectType type, int duration, int amplifier) {
        ItemStack stew = new ItemStack(Material.SUSPICIOUS_STEW);
        SuspiciousStewMeta meta = (SuspiciousStewMeta) stew.getItemMeta();
        if (meta != null) {
            meta.addCustomEffect(new PotionEffect(type, duration, amplifier), true);
            stew.setItemMeta(meta);
        }
        SUSPICIOUS_STEWS.add(stew);
    }

}
