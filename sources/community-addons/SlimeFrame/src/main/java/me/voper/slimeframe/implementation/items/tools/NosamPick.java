package me.voper.slimeframe.implementation.items.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.jeff_media.customblockdata.CustomBlockData;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.ToolUseHandler;
import com.github.drakescraft-labs.slimefun4.implementation.items.SimpleSlimefunItem;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.resources.SpecialOre;
import me.voper.slimeframe.utils.Keys;

public class NosamPick extends SimpleSlimefunItem<ToolUseHandler> {

    private final Map<SpecialOre.Rarity, Integer> probabilities = new HashMap<>();

    @ParametersAreNonnullByDefault
    public NosamPick(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int commonProb, int uncommonProb, int rareProb) {
        super(Groups.UTILS_AND_TOOLS, item, recipeType, recipe);
        this.probabilities.put(SpecialOre.Rarity.COMMON, commonProb);
        this.probabilities.put(SpecialOre.Rarity.UNCOMMON, uncommonProb);
        this.probabilities.put(SpecialOre.Rarity.RARE, rareProb);
    }

    public int getProbability(@Nonnull SpecialOre ore) {
        return probabilities.get(ore.getRarity());
    }

    @Nonnull
    @Override
    public ToolUseHandler getItemHandler() {
        return ((event, tool, fortune, drops) -> {
            Block b = event.getBlock();
            SpecialOre specialOre = SpecialOre.getBySource(b.getType());

            PersistentDataContainer pdc = new CustomBlockData(b, SlimeFrame.getInstance());
            if (pdc.has(Keys.PLACED_BLOCK, PersistentDataType.BYTE) || specialOre == null || SlimeFrame.getPlacedBlocks().contains(b))
                return;

            int probability = getProbability(specialOre);
            int random = ThreadLocalRandom.current().nextInt(100) + 1;

            if (random <= probability) {
                b.getWorld().dropItemNaturally(b.getLocation(), specialOre.getItem().clone());
            }
        });
    }
}
