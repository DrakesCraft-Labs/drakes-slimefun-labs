package com.xmooncorp.magic8ball.implementation.setup;

import com.xmooncorp.magic8ball.Magic8Ball;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;
import org.bukkit.NamespacedKey;

class ItemGroups {

    protected final ItemGroup main = new ItemGroup(
            createKey("main_group"),
            new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGYyNDEyZDYzZGQyZTBlMjMyMjMwYWJkMDRhOGY3MjZlNTFjOTNhNzI2ODZjN2RhZTE3MjJjNDY3N2Y5ZjU0OCJ9fX0=")),
                    Magic8Ball.instance().localization().getString("item-groups.main.name")
            )
    );
    @SuppressWarnings("SameParameterValue")
    private NamespacedKey createKey(String key) {
        return new NamespacedKey(Magic8Ball.instance(), key);
    }
}
