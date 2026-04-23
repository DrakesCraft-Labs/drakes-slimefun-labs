package io.github.sefiraat.simplestorage.items.other;

import io.github.sefiraat.simplestorage.SimpleStorage;
import io.github.sefiraat.simplestorage.utils.Theme;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public final class RareDropCoal {

    private RareDropCoal() {
        throw new IllegalStateException("Utility class");
    }

    public static final SlimefunItemStack STACK = new SlimefunItemStack(
            "MECHANIC_DROP_COAL",
            Material.COAL_ORE,
            Theme.ITEM_RARE_DROP + "Rare Drop: Coal",
            "",
            Theme.PASSIVE + "This item is a rare drop from",
            Theme.PASSIVE + "coal ore. The block needs",
            Theme.PASSIVE + "to be broken without Silk Touch.",
            "",
            Theme.LORE_TYPE_DROP
    );

    public static final RecipeType TYPE = new RecipeType(new NamespacedKey(SimpleStorage.inst(), "drop-coal"), STACK, "");

}
