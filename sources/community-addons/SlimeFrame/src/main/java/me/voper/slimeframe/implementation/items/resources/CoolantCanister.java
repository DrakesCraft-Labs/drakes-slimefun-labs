package me.voper.slimeframe.implementation.items.resources;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.core.handlers.ItemConsumptionHandler;
import com.github.drakescraft-labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;

import me.voper.slimeframe.implementation.SFrameStacks;
import me.voper.slimeframe.implementation.groups.Groups;

import net.md_5.bungee.api.ChatColor;

public class CoolantCanister extends SimpleSlimefunItem<ItemConsumptionHandler> {

    public CoolantCanister() {
        super(Groups.RESOURCES, SFrameStacks.COOLANT_CANISTER, MobDropItem.RECIPE_TYPE, new ItemStack[]{
                null, null, null, null,
                new CustomItemStack(Material.CAVE_SPIDER_SPAWN_EGG, ChatColor.BLUE + "Coolant Raknoid",
                        "",
                        ChatColor.GRAY + "A cave spider with blue particles",
                        ChatColor.GRAY + "swirling around it")
        });
    }

    @Nonnull
    @Override
    public ItemConsumptionHandler getItemHandler() {
        return ((event, player, itemStack) -> {
            player.sendMessage(ChatColor.BLUE + "Your body starts to freeze...");
            player.setFreezeTicks(140 + 10 * 20);
            player.setHealth(player.getHealth() / 10);
        });
    }
}
