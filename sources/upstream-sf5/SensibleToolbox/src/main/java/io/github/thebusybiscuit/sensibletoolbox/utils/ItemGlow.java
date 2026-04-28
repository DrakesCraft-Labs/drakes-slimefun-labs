package io.github.thebusybiscuit.sensibletoolbox.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

/**
 * Utility class to add an enchantment glint to items using the modern
 * {@link ItemMeta#setEnchantmentGlintOverride(Boolean)} API.
 * 
 * @author desht
 * @author TheBusyBiscuit
 */
public final class ItemGlow {

    private ItemGlow() {}

    /**
     * Initialise the ItemGlow system.
     * This is now a no-op since we use the native glint override API.
     *
     * @param plugin
     *            the plugin instance
     */
    public static void init(@Nonnull Plugin plugin) {
        // No-op: enchantment glint override is a native API, no ProtocolLib needed
    }

    /**
     * Set the glowing status of an item stack.
     *
     * @param stack
     *            the item stack to modify
     * @param glowing
     *            true to make the item glow, false to stop it glowing
     */
    public static void setGlowing(@Nonnull ItemStack stack, boolean glowing) {
        ItemMeta meta = stack.getItemMeta();

        if (meta != null) {
            meta.setEnchantmentGlintOverride(glowing ? true : null);
            stack.setItemMeta(meta);
        }
    }

    /**
     * Check if this item stack has been set to glow.
     *
     * @param stack
     *            the item stack to check
     * @return true if the stack will glow; false otherwise
     */
    public static boolean hasGlow(@Nullable ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) {
            return false;
        }

        Boolean override = stack.getItemMeta().getEnchantmentGlintOverride();
        return override != null && override;
    }
}
