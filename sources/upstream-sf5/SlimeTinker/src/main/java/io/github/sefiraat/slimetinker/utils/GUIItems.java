package io.github.sefiraat.slimetinker.utils;

import io.github.sefiraat.slimetinker.items.tinkermaterials.TinkerMaterialManager;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GUIItems {

    private GUIItems() {
        throw new IllegalStateException("Utility class");
    }

    public static final CustomItemStack MENU_BACKGROUND_INPUT = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_BACKGROUND_OUTPUT = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_BACKGROUND_CAST = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_PREVIEW = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_BACKGROUND_PREVIEW = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_MARKER_ROD = CustomItemStack.create($$$);


    public static final CustomItemStack MENU_MARKER_BINDER = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_MARKER_HEAD = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_MARKER_LINKS = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_MARKER_GAMBESON = CustomItemStack.create($$$);


    public static final CustomItemStack MENU_MARKER_PLATES = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_PURGE = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_ALLOY = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_POUR = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_CRAFT_WORKBENCH = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_CRAFT_TOOL_TABLE = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_CRAFT_ARMOUR_TABLE = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_CRAFT_REPAIR = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_CRAFT_MOD = CustomItemStack.create($$$);

    public static final CustomItemStack MENU_CRAFT_SWAP = CustomItemStack.create($$$);

    @Nonnull
    public static CustomItemStack menuLavaInfo(int fillPercent, int fillAmt, int fillMax) {
        ItemStack skull;
        if (fillPercent >= 95) {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_LAVA_5));
        } else if (fillPercent >= 75) {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_LAVA_4));
        } else if (fillPercent >= 50) {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_LAVA_3));
        } else if (fillPercent >= 25) {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_LAVA_2));
        } else if (fillPercent > 0) {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_LAVA_1));
        } else {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_EMPTY));
        }
        List<String> meta = new ArrayList<>();
        meta.add(ThemeUtils.GUI_HEAD + "Lava Tank");
        meta.add("");
        meta.add(ThemeUtils.CLICK_INFO + "Lava: " + ChatColor.WHITE + fillAmt + " / " + fillMax);
        return CustomItemStack.create($$$);
    }

    @Nonnull
    public static CustomItemStack menuMetalInfo(int fillPercent, int fillAmt, int fillMax, @Nullable Map<String, Integer> map) {
        ItemStack skull;
        if (fillPercent >= 95) {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_METAL_5));
        } else if (fillPercent >= 75) {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_METAL_4));
        } else if (fillPercent >= 50) {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_METAL_3));
        } else if (fillPercent >= 25) {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_METAL_2));
        } else if (fillPercent > 0) {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_METAL_1));
        } else {
            skull = PlayerHead.getItemStack(PlayerSkin.fromBase64(SkullTextures.TANK_EMPTY));
        }
        List<String> meta = new ArrayList<>();
        meta.add(ThemeUtils.GUI_HEAD + "Metals Tank");
        meta.add("");
        meta.add(ThemeUtils.CLICK_INFO + "Total Metal: " + ChatColor.WHITE + fillAmt + " / " + fillMax);
        meta.add("");
        if (map != null) {
            for (Map.Entry<String, Integer> e : map.entrySet()) {
                String name =
                    TinkerMaterialManager.getById(e.getKey()).getColor() +
                        ThemeUtils.toTitleCase(e.getKey());
                String amount = e.getValue().toString();
                meta.add(ThemeUtils.CLICK_INFO + name + ": " + ChatColor.WHITE + amount + " units.");
            }
        }
        meta.add("");
        meta.add(ThemeUtils.PASSIVE + "Metals pour out from the " + ChatColor.BOLD + "top" + ThemeUtils.PASSIVE + " first");
        meta.add("");
        meta.add(ThemeUtils.CLICK_INFO + "Click to cycle metal order.");
        return CustomItemStack.create($$$);
    }
}
