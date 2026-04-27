package org.metamechanists.kinematiccore.api.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"WeakerAccess", "unused"})
public class ItemStackBuilder {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static final int LINE_LENGTH = 50;

    public static final String DIAMOND = "\u25C6";
    public static final String RIGHT_ARROW = "\u2192";

    public static final String COMPONENT_COLOR = "<color:#48df56>";
    public static final String VEHICLE_COLOR = "<color:#b259d6>";

    public static final String COMPONENT = COMPONENT_COLOR + "\u1F6E0 Component";
    public static final String TOOL = "<color:#00ff00>\u26CF Tool";
    public static final String WEAPON = "<color:#00ff00>\u1F5E1 Weapon";
    public static final String ARMOR = "<color:#00ff00>\u1F455 Armor";
    public static final String VEHICLE = VEHICLE_COLOR + "<color:#aa6eaa>\u2708 Vehicle";
    public static final String MACHINE = "<color:#00ff00>\u1F3ED Machine";
    public static final String STORAGE = "<color:#00ff00>\u1F9F0 Storage";
    public static final String GENERATOR = "<color:#00ff00>\u26A1 Generator";
    public static final String ENERGY_STORAGE = "<color:#00ff00>\u1F50B Energy Storage";
    public static final String RESOURCE = "<color:#00ff00>\u1FAA8 Resource";
    public static final String FOOD = "<color:#00ff00>\u1F352 Food/drink";
    public static final String BUILDING_BLOCK = "<color:#00ff00>\u1F3DB Building Block";

    private final ItemStack stack;

    public ItemStackBuilder(Material material) {
        this.stack = new ItemStack(material);
    }

    private void addLine(Component line) {
        stack.editMeta(meta -> {
            List<Component> lore = meta.lore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            lore.add(line.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            meta.lore(lore);
        });
    }

    public ItemStackBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder name(String name) {
        return name(mm.deserialize(name));
    }

    public ItemStackBuilder name(Component name) {
        stack.editMeta(meta -> meta.displayName(name.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        return this;
    }

    public ItemStackBuilder loreLine(String line) {
        addLine(mm.deserialize(line));
        return this;
    }

    public ItemStackBuilder loreLine(Component line) {
        addLine(line);
        return this;
    }

    public ItemStackBuilder enchantment(Enchantment enchantment, int level) {
        stack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemStackBuilder flag(ItemFlag flag) {
        stack.addItemFlags(flag);
        return this;
    }

    public ItemStackBuilder attribute(Attribute attribute, AttributeModifier modifier) {
        stack.editMeta(meta -> meta.addAttributeModifier(attribute, modifier));
        return this;
    }

    public ItemStackBuilder unbreakable() {
        stack.editMeta(meta -> meta.setUnbreakable(true));
        return this;
    }

    public ItemStack build() {
        return stack;
    }
}
