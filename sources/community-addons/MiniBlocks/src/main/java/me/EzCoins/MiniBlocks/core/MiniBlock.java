package me.EzCoins.MiniBlocks.core;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import me.EzCoins.MiniBlocks.utils.CustomHead;
import me.EzCoins.MiniBlocks.utils.MiniBlockUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

import static me.EzCoins.MiniBlocks.MiniBlocks.plugin;

public final class MiniBlock {

    @Nonnull
    public static SlimefunItemStack setup(@Nonnull CustomHead head) {
        SlimefunItemStack slimefunItemStack = new SlimefunItemStack(
            MiniBlockUtils.getMiniBlockID(head),
            head.getPlayerHead(),
            MiniBlockUtils.getMiniBlockDisplayName(head)
        );
        head.setSlimefunItemStack(slimefunItemStack);
        return slimefunItemStack;
    }

    public static void craftable(@Nonnull Material material, @Nonnull CustomHead head) {
        new SlimefunItem(Groups.BLOCKS, setup(head), MiniRecipeType.BlockReducer,
                new ItemStack[]{
                        null, null, null,
                        null, new ItemStack(material)})
                .register(plugin);

    }
}
