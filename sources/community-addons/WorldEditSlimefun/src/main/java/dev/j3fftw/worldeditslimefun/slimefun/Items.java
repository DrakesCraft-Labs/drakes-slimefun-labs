package dev.j3fftw.worldeditslimefun.slimefun;

import dev.j3fftw.worldeditslimefun.WorldEditSlimefun;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class Items {
    public static void init(WorldEditSlimefun plugin) {
        ItemGroup WESF_GROUP = new ItemGroup(
                new NamespacedKey(plugin, "world_edit_slimefun"),
                new CustomItemStack(Material.STONE_AXE, "&fWorld Edit Slimefun")
        ) {
            @Override
            public boolean isVisible(@Nonnull Player ignored) {
                return false;
            }
        };

        new SlimefunItem(
                WESF_GROUP,
                new SlimefunItemStack(
                        "WESF_WAND",
                        Material.STONE_AXE,
                        "&f(WESF) Selection Wand",
                        "&fLeft Click to select position 1",
                        "&fRight Click to select position 2"
                ),
                RecipeType.NULL,
                new ItemStack[9]
        ).register(plugin);
    }
}
