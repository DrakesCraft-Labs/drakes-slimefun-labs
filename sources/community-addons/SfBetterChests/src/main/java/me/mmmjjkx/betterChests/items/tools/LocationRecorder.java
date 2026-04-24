package me.mmmjjkx.betterChests.items.tools;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import dev.drake.dough.data.persistent.PersistentDataAPI;
import dev.drake.dough.protection.Interaction;
import me.mmmjjkx.betterChests.BCGroups;
import me.mmmjjkx.betterChests.BetterChests;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class LocationRecorder extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {
    private static final NamespacedKey WORLD = new NamespacedKey(BetterChests.INSTANCE, "world");
    private static final NamespacedKey X = new NamespacedKey(BetterChests.INSTANCE, "pos_x");
    private static final NamespacedKey Y = new NamespacedKey(BetterChests.INSTANCE, "pos_y");
    private static final NamespacedKey Z = new NamespacedKey(BetterChests.INSTANCE, "pos_z");

    public LocationRecorder(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(BCGroups.TOOLS, item, recipeType, recipe);
    }

    @Override
    public @NotNull ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();
            boolean record = p.isSneaking();
            ItemStack self = e.getItem();

            if (record) {
                Optional<Block> b = e.getClickedBlock();
                if (b.isPresent()) {
                    Block block = b.get();
                    Location loc = block.getLocation();

                    ItemMeta meta = self.getItemMeta();

                    PersistentDataAPI.setString(meta, WORLD, loc.getWorld().getName());
                    PersistentDataAPI.setInt(meta, X, loc.getBlockX());
                    PersistentDataAPI.setInt(meta, Y, loc.getBlockY());
                    PersistentDataAPI.setInt(meta, Z, loc.getBlockZ());

                    Component newLore = LegacyComponentSerializer
                            .legacyAmpersand()
                            .deserialize("&bX: &a" + loc.getBlockX() + " &bY: &a" + loc.getBlockY() + " &bZ: &a" + loc.getBlockZ())
                            .decoration(TextDecoration.ITALIC, false);

                    List<Component> lore = meta.lore();

                    if (lore != null && lore.size() < 3) {
                        lore.add(Component.empty());
                        lore.add(newLore);
                    }

                    meta.lore(lore);
                    self.setItemMeta(meta);

                    p.sendMessage("§aLocation recorded!");
                } else {
                    p.sendMessage("§cYou need to be right-clicking at a block to record its location.");
                }
            } else {
                ItemMeta meta = self.getItemMeta();

                Optional<String> world = PersistentDataAPI.getOptionalString(meta, WORLD);
                OptionalInt x = PersistentDataAPI.getOptionalInt(meta, X);
                OptionalInt y = PersistentDataAPI.getOptionalInt(meta, Y);
                OptionalInt z = PersistentDataAPI.getOptionalInt(meta, Z);

                if (world.isPresent() && x.isPresent() && y.isPresent() && z.isPresent()) {
                    Location loc = new Location(Bukkit.getWorld(world.get()), x.getAsInt(), y.getAsInt(), z.getAsInt());

                    if (!Slimefun.getProtectionManager().hasPermission(p, loc, Interaction.INTERACT_BLOCK)) {
                        p.sendMessage("§cYou do not have permission to access this location.");
                        return;
                    }

                    BlockMenu menu = BlockStorage.getInventory(loc);
                    if (menu != null) {
                        menu.open(p);
                    }
                } else {
                    p.sendMessage("§cYou haven't recorded a location yet.");
                }
            }
        };
    }
}
