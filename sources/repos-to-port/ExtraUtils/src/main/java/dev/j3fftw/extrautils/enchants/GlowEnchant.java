package dev.j3fftw.extrautils.enchants;

import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import io.papermc.paper.enchantments.EnchantmentRarity;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;

public class GlowEnchant extends Enchantment {

    private final Set<String> ids = new HashSet<>();
    private final NamespacedKey key;

    public GlowEnchant(@Nonnull NamespacedKey key, @Nonnull String[] applicableItems) {
        this.key = key;
        ids.addAll(Arrays.asList(applicableItems));
    }

    @Nonnull
    @Override
    @Deprecated
    public String getName() {
        return "EXTRA_UTILS_GLOW";
    }

    @Nonnull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Nonnull
    @Override
    public String getTranslationKey() {
        return "enchantment.extra_utils_glow";
    }

    @Nonnull
    @Override
    public String translationKey() {
        return getTranslationKey();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Nonnull
    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    @Deprecated
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@Nonnull Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        if (item.hasItemMeta()) {
            final ItemMeta itemMeta = item.getItemMeta();
            final Optional<String> id = Slimefun.getItemDataService().getItemData(itemMeta);

            if (id.isPresent()) {
                return ids.contains(id.get());
            }
        }
        return false;
    }

    @Nonnull
    @Override
    public Component displayName(int level) {
        return Component.text(getName());
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public int getMinModifiedCost(int level) {
        return 1;
    }

    @Override
    public int getMaxModifiedCost(int level) {
        return 1;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Nonnull
    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.COMMON;
    }

    @Override
    public float getDamageIncrease(int level, @Nonnull EntityCategory entityCategory) {
        return 0.0F;
    }

    @Override
    public float getDamageIncrease(int level, @Nonnull EntityType entityType) {
        return 0.0F;
    }

    @Nonnull
    @Override
    public Set<EquipmentSlot> getActiveSlots() {
        return new HashSet<>(Arrays.asList(EquipmentSlot.values()));
    }

    @Nonnull
    @Override
    public Set<EquipmentSlotGroup> getActiveSlotGroups() {
        return new HashSet<>(Arrays.asList(EquipmentSlotGroup.ANY));
    }

    @Nonnull
    @Override
    public RegistryKeySet<Enchantment> getExclusiveWith() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT);
    }

    @Nonnull
    @Override
    public RegistryKeySet<ItemType> getSupportedItems() {
        return RegistrySet.keySet(RegistryKey.ITEM);
    }

    @Nullable
    @Override
    public RegistryKeySet<ItemType> getPrimaryItems() {
        return null;
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Nonnull
    @Override
    public Component description() {
        return Component.text("Adds a cosmetic glow to items.");
    }

}
