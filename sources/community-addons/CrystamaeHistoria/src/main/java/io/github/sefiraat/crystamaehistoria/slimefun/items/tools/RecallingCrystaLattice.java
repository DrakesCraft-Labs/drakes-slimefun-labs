package io.github.sefiraat.crystamaehistoria.slimefun.items.tools;

import io.github.sefiraat.crystamaehistoria.slimefun.items.gadgets.Waystone;
import io.github.sefiraat.crystamaehistoria.utils.GeneralUtils;
import io.github.sefiraat.crystamaehistoria.utils.Keys;
import io.github.sefiraat.crystamaehistoria.utils.datatypes.PersistentLocationDataType;
import io.github.sefiraat.crystamaehistoria.utils.theme.ThemeType;
import com.github.drakescraft_labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.utils.ChatUtils;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.MessageFormat;
import java.util.Optional;

public class RecallingCrystaLattice extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public RecallingCrystaLattice(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(onItemUse());
    }

    private ItemUseHandler onItemUse() {
        return e -> {
            if (e.getPlayer().isSneaking()) {
                setLocation(e);
            } else {
                teleport(e);
            }
        };
    }

    @ParametersAreNonnullByDefault
    private void setLocation(PlayerRightClickEvent event) {
        final Optional<Block> blockOptional = event.getClickedBlock();

        if (blockOptional.isPresent()) {
            final Block block = blockOptional.get();
            final SlimefunItem slimefunItem = BlockStorage.check(block);
            final Location location = block.getLocation();
            final Player player = event.getPlayer();

            if (slimefunItem instanceof Waystone
                && GeneralUtils.hasPermission(player, location, Interaction.PLACE_BLOCK)
            ) {
                final ItemStack itemStack = event.getItem();
                final ItemMeta itemMeta = itemStack.getItemMeta();
                final PersistentDataContainer container = itemMeta.getPersistentDataContainer();

                container.set(Keys.newKey("location"), PersistentLocationDataType.TYPE, location);
                itemStack.setItemMeta(itemMeta);
                player.sendMessage(
                    MessageFormat.format("{0}Type the name of this Waystone into chat.", ChatColor.LIGHT_PURPLE)
                );
                ChatUtils.awaitInput(player, s -> renameItem(s, itemStack));
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void teleport(PlayerRightClickEvent event) {
        final ItemStack itemStack = event.getItem();
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (container.has(Keys.newKey("location"), PersistentLocationDataType.TYPE)) {
            final Location location = container.get(Keys.newKey("location"), PersistentLocationDataType.TYPE);
            final Block block = location.getBlock();
            final SlimefunItem slimefunItem = BlockStorage.check(block);

            if (slimefunItem instanceof Waystone
                && GeneralUtils.hasPermission(event.getPlayer(), location, Interaction.PLACE_BLOCK)
            ) {
                event.getPlayer().teleportAsync(location.add(1, 1, 1), PlayerTeleportEvent.TeleportCause.PLUGIN);
            } else {
                event.getPlayer().sendActionBar(
                    Component.text("Waystone connection isn't functional")
                        .color(TextColor.color(200, 30, 40))
                );
            }
        } else {
            event.getPlayer().sendMessage(
                MessageFormat.format("{0}Bind the Lattice to a Waystone using Shift + Right Click.", ChatColor.RED)
            );
        }
    }

    @ParametersAreNonnullByDefault
    private void renameItem(String s, @Nullable ItemStack itemStack) {
        if (itemStack != null) {
            final ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(ThemeType.TOOL.getColor() + s);
            itemStack.setItemMeta(itemMeta);
        }
    }
}
