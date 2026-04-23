package me.voper.slimeframe.implementation.tasks;

import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.HashedArmorpiece;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft-labs.slimefun4.core.attributes.ProtectiveArmor;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import com.github.drakescraft-labs.slimefun4.utils.itemstack.ItemStackWrapper;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.core.attributes.FreezingItem;
import me.voper.slimeframe.core.attributes.FreezingProtection;
import me.voper.slimeframe.core.managers.SettingsManager;
import me.voper.slimeframe.utils.ChatUtils;

public class ArmorMonitorTask implements Runnable {

    private final SlimeFrame plugin;

    public ArmorMonitorTask(@Nonnull SlimeFrame plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, 2 * 20L);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.isValid() || p.isDead()) continue;
            PlayerProfile.get(p, profile -> checkForFreezingItems(p, profile));
        }
    }

    private void checkForFreezingItems(@Nonnull Player p, PlayerProfile profile) {
        if (!hasFullFreezingProtection(profile)) {
            for (ItemStack item : p.getInventory()) {
                if (checkAndApplyFreezing(p, item)) break;
            }
        } else if (p.isFrozen()) {
            p.setFreezeTicks(0);
        }
    }

    private boolean checkAndApplyFreezing(@Nonnull Player p, @Nullable ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        Set<SlimefunItem> freezingItems = SlimeFrame.getFreezingItems();
        ItemStack itemStack = item;

        if (!(item instanceof SlimefunItemStack) && freezingItems.size() >= 1) {
            itemStack = ItemStackWrapper.wrap(item);
        }

        for (SlimefunItem freezingItem : freezingItems) {
            if (freezingItem.isItem(itemStack) && !freezingItem.isDisabledIn(p.getWorld())) {
                ChatUtils.sendMessage(p, SlimeFrame.getSettingsManager().getStringList(SettingsManager.ConfigField.FREEZING_ITEM));
                Slimefun.runSync(() -> {
                    p.setFreezeTicks(140 + 10 * 20);
                    double resultHealth = p.getHealth() - ((FreezingItem) freezingItem).getFreezingDamage();
                    p.setHealth(resultHealth < 0 ? 0 : resultHealth);
                });
                return true;
            }
        }

        return false;
    }

    private boolean hasFullFreezingProtection(PlayerProfile profile) {
        int armorCount = 0;
        NamespacedKey setId = null;
        HashedArmorpiece[] cachedArmor = profile.getArmor();

        for (HashedArmorpiece armorPiece : cachedArmor) {
            Optional<SlimefunArmorPiece> armorPieceOptional = armorPiece.getItem();
            if (armorPieceOptional.isEmpty()) {
                setId = null;
            } else if (armorPieceOptional.get() instanceof ProtectiveArmor protectedArmor) {
                if (protectedArmor instanceof FreezingProtection) {
                    if (setId == null && protectedArmor.isFullSetRequired()) {
                        setId = protectedArmor.getArmorSetId();
                    }
                    if (setId == null) {
                        return true;
                    } else if (setId.equals(protectedArmor.getArmorSetId())) {
                        armorCount++;
                    }
                }
            }
        }
        return armorCount == 4;
    }

}
