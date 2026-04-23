package io.github.sefiraat.networks.integrations;

import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.slimefun.network.NetworkGreedyBlock;
import io.github.sefiraat.networks.slimefun.network.NetworkQuantumStorage;
import com.github.drakescraft-labs.slimefun4.utils.ChatUtils;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HudCallbacks {
    private static final String EMPTY = "&7| Empty";
    public static void setup() {
        // Disabled due to missing SlimeHUD dependency
    }

    private static String format(ItemStack itemStack, int amount, int limit) {
        return "";
    }
}
