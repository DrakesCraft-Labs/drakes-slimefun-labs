package com.balugaq.netex.utils;

import com.google.common.base.Preconditions;
import dev.drake.dough.common.ChatColors;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author balugaq
 */
@SuppressWarnings({"deprecation", "unused"})
@ApiStatus.Experimental
public record CustomItemStack(@NotNull ItemStack delegate) implements Cloneable {
    /**
     * Creates a CustomItemStack from a Bukkit ItemStack.
     *
     * @param delegate the Bukkit ItemStack to create from
     */
    public CustomItemStack(@NotNull ItemStack delegate) {
        this.delegate = delegate.clone();
    }

    /**
     * Creates a CustomItemStack from a Material.
     *
     * @param material the Material to create from
     */
    public CustomItemStack(@NotNull Material material) {
        this(new ItemStack(material));
    }

    /**
     * Creates a CustomItemStack from a Bukkit ItemStack with custom metadata.
     *
     * @param itemStack        the Bukkit ItemStack to create from
     * @param itemMetaConsumer the consumer to modify the item metadata
     */
    public CustomItemStack(@NotNull ItemStack itemStack, @NotNull Consumer<ItemMeta> itemMetaConsumer) {
        this(itemStack.clone());
        Preconditions.checkNotNull(itemMetaConsumer, "ItemMeta consumer cannot be null");
        editItemMeta(itemMetaConsumer);
    }

    /**
     * Creates a CustomItemStack from a Material with custom metadata.
     *
     * @param material the Material to create from
     * @param meta     the consumer to modify the item metadata
     */
    public CustomItemStack(@NotNull Material material, @NotNull Consumer<ItemMeta> meta) {
        this(new ItemStack(material), meta);
    }

    /**
     * Creates a CustomItemStack from a Bukkit ItemStack with a name and lore.
     *
     * @param itemStack the Bukkit ItemStack to create from
     * @param name      the name of the item
     * @param lore      the lore of the item
     */
    public CustomItemStack(@NotNull ItemStack itemStack, @Nullable String name, @NotNull List<String> lore) {
        this(itemStack, name, lore.toArray(new String[0]));
    }

    /**
     * Creates a CustomItemStack from a Bukkit ItemStack with a name and lore.
     *
     * @param itemStack the Bukkit ItemStack to create from
     * @param name      the name of the item
     * @param lore      the lore of the item
     */
    public CustomItemStack(@NotNull ItemStack itemStack, @Nullable String name, @NotNull String @NotNull ... lore) {
        this(itemStack, itemMeta -> {
            if (name != null) {
                itemMeta.setDisplayName(color(name));
            }
            if (lore.length > 0) {
                List<String> lines = new ArrayList<>();
                for (String line : lore) {
                    lines.add(color(line));
                }
                itemMeta.setLore(lines);
            }
        });
    }

    /**
     * Creates a CustomItemStack from a Bukkit ItemStack with a color, name, and lore.
     *
     * @param itemStack the Bukkit ItemStack to create from
     * @param color     the color of the item
     * @param name      the name of the item
     * @param lore      the lore of the item
     */
    public CustomItemStack(@NotNull ItemStack itemStack, Color color, @Nullable String name, String @NotNull ... lore) {
        this(itemStack, itemMeta -> {
            if (name != null) {
                itemMeta.setDisplayName(color(name));
            }
            if (lore.length > 0) {
                List<String> lines = new ArrayList<>();
                for (String line : lore) {
                    lines.add(color(line));
                }
                itemMeta.setLore(lines);
            }
            if (itemMeta instanceof LeatherArmorMeta leatherArmorMeta) {
                leatherArmorMeta.setColor(color);
            }
            if (itemMeta instanceof PotionMeta potionMeta) {
                potionMeta.setColor(color);
            }
        });
    }

    /**
     * Creates a CustomItemStack from a Material with a name and lore.
     *
     * @param material the Material to create from
     * @param name     the name of the item
     * @param lore     the lore of the item
     */
    public CustomItemStack(@NotNull Material material, String name, String... lore) {
        this(new ItemStack(material), name, lore);
    }

    /**
     * Creates a CustomItemStack from a Material with a name and lore.
     *
     * @param material the Material to create from
     * @param name     the name of the item
     * @param lore     the lore of the item
     */
    public CustomItemStack(@NotNull Material material, String name, @NotNull List<String> lore) {
        this(new ItemStack(material), name, lore.toArray(new String[0]));
    }

    /**
     * Creates a CustomItemStack from a Bukkit ItemStack with a list of lore.
     *
     * @param itemStack the Bukkit ItemStack to create from
     * @param list      the list of lore
     */
    public CustomItemStack(@NotNull ItemStack itemStack, @NotNull List<String> list) {
        this(itemStack, list.get(0), list.subList(1, list.size()).toArray(new String[0]));
    }

    /**
     * Creates a CustomItemStack from a Material with a list of lore.
     *
     * @param material the Material to create from
     * @param list     the list of lore
     */
    public CustomItemStack(@NotNull Material material, @NotNull List<String> list) {
        this(new ItemStack(material), list);
    }

    /**
     * Creates a CustomItemStack from a Bukkit ItemStack with a specified amount.
     *
     * @param itemStack the Bukkit ItemStack to create from
     * @param amount    the amount of the item
     */
    public CustomItemStack(@NotNull ItemStack itemStack, @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
        this(itemStack.clone());
        this.delegate.setAmount(amount);
    }

    /**
     * Creates a CustomItemStack from a Bukkit ItemStack with a specified Material.
     *
     * @param itemStack the Bukkit ItemStack to create from
     * @param material  the Material of the item
     */
    public CustomItemStack(@NotNull ItemStack itemStack, @NotNull Material material) {
        this(itemStack.clone());
        this.delegate.setType(material);
    }

    /**
     * Translates color codes in a string.
     *
     * @param raw the string to translate
     * @return the translated string
     */
    public static @NotNull String color(@NotNull String raw) {
        return ChatColors.color(Preconditions.checkNotNull(raw, "raw cannot be null"));
    }

    /**
     * Returns the delegate Bukkit ItemStack.
     *
     * @return the delegate Bukkit ItemStack
     */
    @Override
    public @NotNull ItemStack delegate() {
        return delegate.clone();
    }

    /**
     * Returns the Material of the item.
     *
     * @return the Material of the item
     */
    public @NotNull Material getType() {
        return delegate.getType();
    }

    /**
     * Sets the Material of the item.
     *
     * @param material the Material to set
     */
    public void setType(@NotNull Material material) {
        delegate.setType(material);
    }

    /**
     * Returns the amount of the item.
     *
     * @return the amount of the item
     */
    public int getAmount() {
        return delegate.getAmount();
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
        delegate.setAmount(amount);
    }

    /**
     * Checks if the item has metadata.
     *
     * @return true if the item has metadata, false otherwise
     */
    public boolean hasItemMeta() {
        return delegate.hasItemMeta();
    }

    /**
     * Returns the item metadata.
     *
     * @return the item metadata
     */
    public ItemMeta getItemMeta() {
        return delegate.getItemMeta();
    }

    /**
     * Sets the item metadata.
     *
     * @param meta the metadata to set
     * @return true if the metadata was set successfully, false otherwise
     */
    public boolean setItemMeta(ItemMeta meta) {
        return delegate.setItemMeta(meta);
    }

    /**
     * Adds item flags to the item.
     *
     * @param flags the item flags to add
     * @return the CustomItemStack with the added flags
     */
    public @NotNull CustomItemStack addFlags(@NotNull ItemFlag @NotNull ... flags) {
        Preconditions.checkNotNull(flags, "flags cannot be null");
        Preconditions.checkArgument(flags.length > 0, "flags cannot be empty");
        return editItemMeta(meta -> meta.addItemFlags(flags));
    }

    /**
     * Returns the Bukkit ItemStack.
     *
     * @return the Bukkit ItemStack
     */
    public @NotNull ItemStack asBukkit() {
        return delegate.clone();
    }

    /**
     * Edits the item metadata.
     *
     * @param itemMetaConsumer the consumer to modify the item metadata
     * @return the CustomItemStack with the edited metadata
     */
    public @NotNull CustomItemStack editItemMeta(@NotNull Consumer<ItemMeta> itemMetaConsumer) {
        Preconditions.checkNotNull(itemMetaConsumer, "ItemMeta consumer cannot be null");

        ItemMeta meta = delegate.getItemMeta();
        if (meta != null) {
            itemMetaConsumer.accept(meta);
            delegate.setItemMeta(meta);
        }
        return this;
    }

    /**
     * Edits the item stack.
     *
     * @param itemStackConsumer the consumer to modify the item stack
     * @return the CustomItemStack with the edited stack
     */
    public @NotNull CustomItemStack editItemStack(@NotNull Consumer<ItemStack> itemStackConsumer) {
        Preconditions.checkNotNull(itemStackConsumer, "ItemStack consumer cannot be null");

        itemStackConsumer.accept(delegate);
        return this;
    }

    /**
     * Sets the custom model data of the item.
     *
     * @param data the custom model data to set
     * @return the CustomItemStack with the set custom model data
     */
    public @NotNull CustomItemStack setCustomModelData(@Range(from = 0, to = Integer.MAX_VALUE) int data) {
        return editItemMeta(meta -> meta.setCustomModelData(data == 0 ? null : data));
    }

    /**
     * Clones the CustomItemStack.
     *
     * @return the cloned CustomItemStack
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull CustomItemStack clone() {
        return new CustomItemStack(delegate());
    }
}
