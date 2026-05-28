package com.github.drakescraft_labs.gcereborn.items.chicken;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonObject;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Chicken;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.DistinctiveItem;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.ItemUtils;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.core.adapters.AnimalsAdapter;
import com.github.drakescraft_labs.gcereborn.core.genetics.DNA;
import com.github.drakescraft_labs.gcereborn.utils.Keys;

public class PocketChicken extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable, DistinctiveItem {

    public static final AnimalsAdapter<Chicken> ADAPTER = new AnimalsAdapter<>(Chicken.class);

    public PocketChicken(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Optional<Block> block = e.getClickedBlock();
            if (block.isEmpty()) {
                return;
            }

            Block b = block.get();
            Location location = b.getRelative(e.getClickedFace()).getLocation();
            Chicken entity = b.getWorld().spawn(location.toCenterLocation(), Chicken.class);

            ItemMeta meta = e.getItem().getItemMeta();
            JsonObject json = PersistentDataAPI.get(meta, Keys.POCKET_CHICKEN_ADAPTER, ADAPTER);
            ADAPTER.apply(entity, json);
            int[] dnaState = PersistentDataAPI.getIntArray(meta, Keys.POCKET_CHICKEN_DNA);
            DNA dna;
            if (dnaState != null) {
                dna = new DNA(dnaState);
            } else {
                dna = new DNA();
            }

            String dss = dna.getStateString();
            PersistentDataAPI.setString(entity, Keys.CHICKEN_DNA, dss);

            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                // Try to consume the actual item from the player's inventory (main/off hand).
                // This is more robust than relying on the event item reference which
                // in some contexts may be a copy and not mutate the inventory.
                var player = e.getPlayer();
                ItemStack main = player.getInventory().getItemInMainHand();
                ItemStack off = player.getInventory().getItemInOffHand();
                ItemStack used = e.getItem();

                try {
                    if (ItemUtils.canStack(main, used)) {
                        ItemUtils.consumeItem(main, false);
                        player.getInventory().setItemInMainHand(main);
                    } else if (ItemUtils.canStack(off, used)) {
                        ItemUtils.consumeItem(off, false);
                        player.getInventory().setItemInOffHand(off);
                    } else {
                        // Fallback: consume the event item (best-effort)
                        ItemUtils.consumeItem(used, false);
                    }
                } catch (Exception ex) {
                    // As a last resort, attempt to consume the provided item instance.
                    ItemUtils.consumeItem(used, false);
                }
            }

            if (GeneticChickengineering.getConfigService().isDisplayResources() && dna.isKnown()) {
                String name = ChatColor.WHITE + "(" + ChickenTypes.getDisplayName(dna.getTyping()) + ")";
                if (json != null && !json.get("_customName").isJsonNull()) {
                    name = json.get("_customName").getAsString() + " " + name;
                }
                entity.setCustomName(name);
                entity.setCustomNameVisible(true);
            }
        };
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canStack(ItemMeta meta1, ItemMeta meta2) {
        return meta1.getPersistentDataContainer().equals(meta2.getPersistentDataContainer());
    }
}
