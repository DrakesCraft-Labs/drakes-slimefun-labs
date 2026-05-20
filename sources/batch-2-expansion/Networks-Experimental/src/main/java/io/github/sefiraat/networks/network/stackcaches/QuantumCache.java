package io.github.sefiraat.networks.network.stackcaches;

import io.github.sefiraat.networks.utils.Theme;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class QuantumCache extends ItemStackCache {

    @Nullable
    private final ItemMeta storedItemMeta;
    private final boolean supportsCustomMaxAmount;
    @Getter
    @Setter
    private int limit;
    @Setter
    @Getter
    private int amount;
    @Getter
    @Setter
    private boolean voidExcess;

    public QuantumCache(@Nullable ItemStack storedItem, int amount, int limit, boolean voidExcess, boolean supportsCustomMaxAmount) {
        super(storedItem);
        this.storedItemMeta = getItemStack() == null ? null : getItemStack().getItemMeta();
        this.amount = amount;
        this.limit = limit;
        this.voidExcess = voidExcess;
        this.supportsCustomMaxAmount = supportsCustomMaxAmount;
    }

    @Nullable
    public ItemMeta getStoredItemMeta() {
        return this.storedItemMeta;
    }

    public boolean supportsCustomMaxAmount() {
        return this.supportsCustomMaxAmount;
    }

    public int increaseAmount(int amount) {
        long total = (long) this.amount + (long) amount;
        if (total > this.limit) {
            this.amount = this.limit;
            if (!this.voidExcess) {
                return (int) (total - this.limit);
            }
        } else {
            this.amount = this.amount + amount;
        }
        return 0;
    }

    public void reduceAmount(int amount) {
        this.amount = this.amount - amount;
    }

    @Nullable
    public ItemStack withdrawItem(int amount) {
        if (this.getItemStack() == null) {
            return null;
        }
        final ItemStack clone = this.getItemStack().clone();
        clone.setAmount(Math.min(this.amount, amount));
        reduceAmount(clone.getAmount());
        return clone;
    }

    @Nullable
    public ItemStack withdrawItem() {
        if (this.getItemStack() == null) {
            return null;
        }
        return withdrawItem(this.getItemStack().getMaxStackSize());
    }

    public void addMetaLore(ItemMeta itemMeta) {
        final List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        lore.add("");
        lore.add(Theme.CLICK_INFO + "Holding: " +
                (this.getItemMeta() != null && this.getItemMeta().hasDisplayName() ? this.getItemMeta().getDisplayName() : this.getItemStack().getType().name())
        );
        lore.add(Theme.CLICK_INFO + "Amount: " + this.getAmount());
        if (this.supportsCustomMaxAmount) {
            lore.add(Theme.CLICK_INFO + "Current capacity limit: " + Theme.ERROR + this.getLimit());
        }

        itemMeta.setLore(lore);
    }

    public void updateMetaLore(ItemMeta itemMeta) {
        final List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        final int loreIndexModifier = this.supportsCustomMaxAmount ? 1 : 0;
        lore.set(lore.size() - 2, Theme.CLICK_INFO + "Holding: " +
                (this.getItemMeta() != null && this.getItemMeta().hasDisplayName() ? this.getItemMeta().getDisplayName() : this.getItemStack().getType().name())
        );
        lore.set(lore.size() - 1, Theme.CLICK_INFO + "Amount: " + this.getAmount());
        if (this.supportsCustomMaxAmount) {
            lore.set(lore.size() - loreIndexModifier, Theme.CLICK_INFO + "Current capacity limit: " + Theme.ERROR + this.getLimit());
        }

        itemMeta.setLore(lore);
    }
}
