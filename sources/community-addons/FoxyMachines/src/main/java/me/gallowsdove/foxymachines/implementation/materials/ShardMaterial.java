package me.gallowsdove.foxymachines.implementation.materials;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemDropHandler;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.implementation.items.SimpleSlimefunItem;
import me.gallowsdove.foxymachines.listeners.SacrificialAltarListener;
import me.gallowsdove.foxymachines.utils.QuestUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class ShardMaterial extends SimpleSlimefunItem<ItemDropHandler> {
    private final ChatColor color;
    public ShardMaterial(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ChatColor color) {
        super(itemGroup, item, recipeType, recipe);

        this.color = color;
    }

    @Nonnull
    @Override
    public ItemDropHandler getItemHandler() {
        return (e, p, item) -> {
            if (!isItem(item.getItemStack())) {
                return false;
            }

            Slimefun.runSync(() -> {
                if (!QuestUtils.hasActiveQuest(p)) {
                    p.sendMessage(this.color + "You should check your quest with " + ChatColor.LIGHT_PURPLE + "/foxy quest " + this.color + "first!");
                    return;
                }

                if (SacrificialAltarListener.findAltar(item.getLocation().getBlock()) == null) {
                    return;
                }

                p.sendMessage(this.color + "Reset active quest!");
                QuestUtils.resetQuestLine(p);
                SacrificialAltarListener.particleAnimation(item.getLocation());

                if (item.getItemStack().getAmount() == 1) {
                    item.remove();
                } else {
                    ItemStack i = item.getItemStack();
                    i.setAmount(i.getAmount() - 1);
                    item.setItemStack(i);
                }
            }, 20L);

            return true;
        };
    }
}
