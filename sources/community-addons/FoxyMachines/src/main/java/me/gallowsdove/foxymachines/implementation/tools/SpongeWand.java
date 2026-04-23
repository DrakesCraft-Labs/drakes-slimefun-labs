package me.gallowsdove.foxymachines.implementation.tools;

import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;
import dev.drake.dough.config.Config;
import dev.drake.dough.protection.Interaction;
import me.gallowsdove.foxymachines.FoxyMachines;
import me.gallowsdove.foxymachines.Items;
import me.gallowsdove.foxymachines.abstracts.AbstractWand;
import me.gallowsdove.foxymachines.utils.SimpleLocation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpongeWand extends AbstractWand {
    public SpongeWand() {
        super(Items.SPONGE_WAND, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.NUCLEAR_SALT, Items.WIRELESS_TRANSMITTER, Items.NUCLEAR_SALT,
                Items.DEMONIC_PLATE, SlimefunItems.PROGRAMMABLE_ANDROID_2, Items.DEMONIC_PLATE,
                Items.NUCLEAR_SALT, Items.COMPRESSED_SPONGE, Items.NUCLEAR_SALT
        });
    }

    @Override
    protected int getMaxBlocks() {
        return FoxyMachines.getInstance().getConfig().getInt("max-sponge-wand-blocks");
    }

    @Override
    protected boolean isRemoving() {return true;}

    @Override
    protected float getCostPerBlock() {
        return 0.24F;
    }

    @Override
    protected boolean blockPredicate(Player player, Block block) {
        return block.isLiquid() && Slimefun.getProtectionManager().hasPermission(player, block, Interaction.BREAK_BLOCK);
    }

    @Override
    public float getMaxItemCharge(ItemStack itemStack) {
        return 2000;
    }
}
