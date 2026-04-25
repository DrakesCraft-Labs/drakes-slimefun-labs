package io.github.thebusybiscuit.slimefun4.api.items;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import com.github.drakescraft_labs.slimefun4.utils.HeadTexture;

@Deprecated(forRemoval = false)
public class SlimefunItemStack extends com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack {

    public SlimefunItemStack(String id, ItemStack item) {
        super(id, item);
    }

    public SlimefunItemStack(String id, ItemStack item, Consumer<ItemMeta> consumer) {
        super(id, item, consumer);
    }

    public SlimefunItemStack(String id, Material type, Consumer<ItemMeta> consumer) {
        super(id, type, consumer);
    }

    public SlimefunItemStack(String id, Material type, @Nullable String name, Consumer<ItemMeta> consumer) {
        super(id, type, name, consumer);
    }

    public SlimefunItemStack(String id, ItemStack item, @Nullable String name, String... lore) {
        super(id, item, name, lore);
    }

    public SlimefunItemStack(String id, Material type, @Nullable String name, String... lore) {
        super(id, type, name, lore);
    }

    public SlimefunItemStack(String id, Material type, Color color, @Nullable String name, String... lore) {
        super(id, type, color, name, lore);
    }

    public SlimefunItemStack(String id, Color color, PotionEffect effect, @Nullable String name, String... lore) {
        super(id, color, effect, name, lore);
    }

    public SlimefunItemStack(SlimefunItemStack item, int amount) {
        super(item, amount);
    }

    public SlimefunItemStack(String id, String texture, @Nullable String name, String... lore) {
        super(id, texture, name, lore);
    }

    public SlimefunItemStack(String id, HeadTexture head, @Nullable String name, String... lore) {
        super(id, head, name, lore);
    }

    public SlimefunItemStack(String id, String texture, @Nullable String name, Consumer<ItemMeta> consumer) {
        super(id, texture, name, consumer);
    }

    public SlimefunItemStack(String id, String texture, Consumer<ItemMeta> consumer) {
        super(id, texture, consumer);
    }
}
