package com.xmooncorp.magic8ball.implementation.setup;

import com.xmooncorp.magic8ball.Magic8Ball;
import com.xmooncorp.magic8ball.implementation.Items;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.researches.Research;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

public final class ResearchSetup {
    private static boolean alreadyRan = false;

    private ResearchSetup() {
    }

    public static void setupResearches() {
        if (alreadyRan) {
            throw new UnsupportedOperationException("Researches can only be registered once!");
        } else {
            alreadyRan = true;
            register("magic8ball_research", 726854259, "Magic 8 Ball Research", 88, Items.MAGIC_8_BALL);
        }
    }

    @SuppressWarnings("SameParameterValue")
    @ParametersAreNonnullByDefault
    private static void register(String key, int id, String name, int defaultCost, ItemStack... items) {
        Research research = new Research(new NamespacedKey(Magic8Ball.instance(), key), id, name, defaultCost);
        for (ItemStack item : items) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            if (sfItem != null) {
                research.addItems(sfItem);
            }
        }
        research.register();
    }
}
