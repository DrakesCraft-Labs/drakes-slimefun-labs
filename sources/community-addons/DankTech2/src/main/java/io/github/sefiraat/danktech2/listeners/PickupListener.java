package io.github.sefiraat.danktech2.listeners;

import io.github.sefiraat.danktech2.managers.ConfigManager;
import io.github.sefiraat.danktech2.DankTech2;
import io.github.sefiraat.danktech2.core.DankPackInstance;
import io.github.sefiraat.danktech2.core.TrashPackInstance;
import io.github.sefiraat.danktech2.slimefun.packs.DankPack;
import io.github.sefiraat.danktech2.slimefun.packs.TrashPack;
import io.github.sefiraat.danktech2.theme.ThemeType;
import io.github.sefiraat.danktech2.utils.Keys;
import io.github.sefiraat.danktech2.utils.ParticleUtils;
import io.github.sefiraat.danktech2.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.danktech2.utils.datatypes.PersistentDankInstanceType;
import io.github.sefiraat.danktech2.utils.datatypes.PersistentTrashInstanceType;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;

public class PickupListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onItemPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();

            if (ConfigManager.getBlacklistedWorldsPickup().contains(player.getWorld().getName())) {
                return;
            }

            final Item item = e.getItem();

            for (ItemStack itemStack : player.getInventory().getContents()) {
                final SlimefunItem slimefunItem = SlimefunItem.getByItem(itemStack);

                // Try to pick up the item into a dank first
                if (!item.isDead() && slimefunItem instanceof DankPack) {
                    tryPickupItem(player, itemStack, item);
                }
                // Then trash packs for voiding the rest
                if (!item.isDead() && slimefunItem instanceof TrashPack) {
                    tryVoidItem(itemStack, item);
                }
                // If the item has been marked for removal, cancel the give up on the remaining packs
                if (item.isDead()) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    public void tryPickupItem(Player player, ItemStack pack, Item item) {
        final DankPack dankPack = (DankPack) SlimefunItem.getByItem(pack);
        final ItemMeta packMeta = pack.getItemMeta();

        final ItemStack itemStack = item.getItemStack();
        final DankPackInstance instance = DataTypeMethods.getCustom(packMeta, Keys.DANK_INSTANCE, PersistentDankInstanceType.TYPE);

        if (ConfigManager.getInstance().checkDankDeletion(instance.getId())) {
            player.sendMessage(MessageFormat.format(
                "{0}A Dank Pack you have has been duped or deleted. Removing",
                ThemeType.ERROR.getColor())
            );
            pack.setAmount(0);
            return;
        }

        for (int i = 0; i < dankPack.getSlots(); i++) {
            final ItemStack testStack = instance.getItem(i);

            if (testStack == null) {
                continue;
            }

            final int itemAmount = DankTech2.getSupportedPluginManager().getStackAmount(item);
            final int maxAmount = dankPack.getCapacityPerSlot().getValue();
            final int amount = instance.getAmount(i);

            if (testStack.isSimilar(itemStack)) {
                final long testAmount = (long) amount + itemAmount;
                final boolean overflowing = testAmount > maxAmount;
                final Particle.DustOptions dustOptions;

                if (overflowing) {
                    // Set to max value (we void overage) and set dust to a red to show overflow
                    instance.setAmount(i, maxAmount);
                    dustOptions = new Particle.DustOptions(Color.fromRGB(210, 48, 100), 1);
                } else {
                    // Set to value and set dust to a green to show full pickup
                    instance.setAmount(i, (int) testAmount);
                    dustOptions = new Particle.DustOptions(Color.fromRGB(100, 210, 48), 1);
                }
                ParticleUtils.displayParticleEffect(
                    item.getLocation().add(0, 0.2, 0),
                    0.4,
                    10,
                    dustOptions
                );
                item.remove();
                DataTypeMethods.setCustom(packMeta, Keys.DANK_INSTANCE, PersistentDankInstanceType.TYPE, instance);
                pack.setItemMeta(packMeta);
                ConfigManager.getInstance().saveDankPack(pack);
                return;
            }
        }
    }

    public void tryVoidItem(ItemStack pack, Item item) {
        final TrashPack trashPack = (TrashPack) SlimefunItem.getByItem(pack);
        final ItemMeta packMeta = pack.getItemMeta();

        final ItemStack itemStack = item.getItemStack();
        final TrashPackInstance instance = DataTypeMethods.getCustom(packMeta, Keys.TRASH_INSTANCE, PersistentTrashInstanceType.TYPE);

        for (int i = 0; i < trashPack.getSlots(); i++) {
            final ItemStack testStack = instance.getItem(i);

            if (testStack == null) {
                continue;
            }

            if (testStack.isSimilar(itemStack)) {
                ParticleUtils.displayParticleEffect(
                    item.getLocation().add(0, 0.2, 0),
                    0.4,
                    10,
                    new Particle.DustOptions(Color.fromRGB(252, 186, 3), 1)
                );
                item.remove();
                return;
            }
        }
    }


}
