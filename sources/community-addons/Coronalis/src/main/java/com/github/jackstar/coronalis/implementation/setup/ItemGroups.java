package com.github.jackstar.coronalis.implementation.setup;

import org.bukkit.NamespacedKey;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.jackstar.coronalis.Coronalis;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.libraries.dough.skins.PlayerHead;
import com.github.drakescraft_labs.slimefun4.libraries.dough.skins.PlayerSkin;

public final class ItemGroups {

    public static final ItemGroup CORONALIS_GROUP = new ItemGroup(
        new NamespacedKey(Coronalis.instance(), "coronalis"),
        new CustomItemStack(
            PlayerHead.getItemStack(PlayerSkin.fromBase64(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2MyM2RkODRjYzI2M2Q5OGE1NzU4YjMwMWNkMTc1NWVhYzE5M2I5In19fQ=="
            )),
            "&5&lCoronalis",
            "&7Deep-sky · fase · interferometría"
        )
    );

    private ItemGroups() {
    }
}
