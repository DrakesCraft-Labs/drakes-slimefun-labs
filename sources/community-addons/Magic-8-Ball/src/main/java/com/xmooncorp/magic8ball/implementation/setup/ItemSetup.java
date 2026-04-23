package com.xmooncorp.magic8ball.implementation.setup;

import com.xmooncorp.magic8ball.Magic8Ball;
import com.xmooncorp.magic8ball.implementation.Items;
import com.xmooncorp.magic8ball.implementation.items.Magic8BallItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class ItemSetup {
    private static boolean registeredItems = false;

    private ItemSetup() {
    }

    @SuppressWarnings("DataFlowIssue")
    public static void setup(@Nonnull Magic8Ball plugin) {
        if (registeredItems) {
            throw new UnsupportedOperationException("Magic 8 Ball Items can only be registered once!");
        } else {
            registeredItems = true;
            ItemGroups itemGroups = new ItemGroups();

            /* Materials */
            new SlimefunItem(itemGroups.main, Items.MAGIC_8_BALL_FRAGMENT, RecipeType.GEO_MINER, new ItemStack[9]).register(plugin);
            /* Tools */
            new Magic8BallItem(itemGroups.main, Items.MAGIC_8_BALL, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                    Items.MAGIC_8_BALL_FRAGMENT,        SlimefunItems.TALISMAN_WISE,       Items.MAGIC_8_BALL_FRAGMENT,
                    SlimefunItems.MAGICAL_BOOK_COVER,   SlimefunItems.MAGIC_EYE_OF_ENDER,                           SlimefunItems.MAGICAL_GLASS,
                    Items.MAGIC_8_BALL_FRAGMENT,        SlimefunItem.getById("ENDER_WISE_TALISMAN").getItem(),       Items.MAGIC_8_BALL_FRAGMENT
            }).register(plugin);
        }
    }
}
