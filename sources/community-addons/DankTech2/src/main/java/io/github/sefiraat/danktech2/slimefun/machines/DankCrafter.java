package io.github.sefiraat.danktech2.slimefun.machines;

import io.github.sefiraat.danktech2.managers.ConfigManager;
import io.github.sefiraat.danktech2.core.DankPackInstance;
import io.github.sefiraat.danktech2.core.TrashPackInstance;
import io.github.sefiraat.danktech2.slimefun.Machines;
import io.github.sefiraat.danktech2.slimefun.packs.DankPack;
import io.github.sefiraat.danktech2.slimefun.packs.TrashPack;
import io.github.sefiraat.danktech2.theme.ThemeType;
import io.github.sefiraat.danktech2.utils.Keys;
import io.github.sefiraat.danktech2.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.danktech2.utils.datatypes.PersistentDankInstanceType;
import io.github.sefiraat.danktech2.utils.datatypes.PersistentTrashInstanceType;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.protection.Interaction;
import com.github.drakescraft-labs.slimefun4.utils.SlimefunUtils;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft-labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DankCrafter extends SlimefunItem {

    private static final int[] BACKGROUND_SLOTS = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 22, 24, 26, 27, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private static final int[] RECIPE_SLOTS = {
        10, 11, 12, 19, 20, 21, 28, 29, 30
    };
    private static final int CRAFT_SLOT = 23;
    private static final int OUTPUT_SLOT = 25;

    private static final CustomItemStack CRAFT_BUTTON_STACK = new CustomItemStack(
        Material.JUKEBOX,
        ThemeType.CLICK_INFO.getColor() + "Click to upgrade"
    );

    public static final RecipeType TYPE = new RecipeType(
        Keys.newKey("dank_crafter"),
        new CustomItemStack(
            Material.JUKEBOX,
            "Dank Upgrader",
            "Crafted within the Dank Crafter"
        ),
        DankCrafter::addRecipe
    );

    private static final Map<ItemStack[], ItemStack> RECIPE_MAP = new HashMap<>();

    @ParametersAreNonnullByDefault
    public DankCrafter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemHandler(getBlockBreakHandler());
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {
            @Override
            public void init() {
                drawBackground(BACKGROUND_SLOTS);
                addItem(CRAFT_SLOT, CRAFT_BUTTON_STACK, (p, slot, item, action) -> false);
            }

            @Override
            public boolean canOpen(@NotNull Block block, @NotNull Player player) {
                return Machines.getDankCrafter().canUse(player, false)
                    && Slimefun.getProtectionManager().hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.WITHDRAW) {
                    return new int[]{DankCrafter.OUTPUT_SLOT};
                }
                return new int[0];
            }

            @Override
            public void newInstance(@NotNull BlockMenu menu, @NotNull Block b) {
                menu.addMenuClickHandler(CRAFT_SLOT, (player, slot, item, action) -> {

                    ItemStack itemInOutput = menu.getItemInSlot(OUTPUT_SLOT);

                    // Quick escape, we only allow crafting if the output is empty
                    if (itemInOutput != null) {
                        return false;
                    }

                    final ItemStack[] inputs = new ItemStack[RECIPE_SLOTS.length];
                    int i = 0;

                    // Fill the inputs
                    for (int recipeSlot : RECIPE_SLOTS) {
                        ItemStack stack = menu.getItemInSlot(recipeSlot);
                        inputs[i] = stack;
                        i++;
                    }

                    ItemStack crafted = null;

                    // Go through each recipe, test and set the ItemStack if found
                    for (Map.Entry<ItemStack[], ItemStack> entry : RECIPE_MAP.entrySet()) {
                        if (testRecipe(inputs, entry.getKey())) {
                            crafted = entry.getValue().clone();
                            break;
                        }
                    }

                    if (crafted == null) {
                        return false;
                    }

                    final SlimefunItem slimefunItem = SlimefunItem.getByItem(crafted);
                    final ItemStack coreItemStack = inputs[4];
                    final ItemMeta coreItemMeta = coreItemStack.getItemMeta();
                    final ItemMeta craftedItemMeta = crafted.getItemMeta();

                    if (slimefunItem instanceof DankPack) {
                        final DankPack dankPack = (DankPack) slimefunItem;
                        DankPackInstance instance = DataTypeMethods.getCustom(coreItemMeta, Keys.DANK_INSTANCE, PersistentDankInstanceType.TYPE, new DankPackInstance(System.currentTimeMillis(), 0));
                        instance.setTier(dankPack.getTier());
                        DataTypeMethods.setCustom(craftedItemMeta, Keys.DANK_INSTANCE, PersistentDankInstanceType.TYPE, instance);
                    } else if (slimefunItem instanceof TrashPack) {
                        final TrashPack trashPack = (TrashPack) slimefunItem;
                        TrashPackInstance instance = DataTypeMethods.getCustom(coreItemMeta, Keys.DANK_INSTANCE, PersistentTrashInstanceType.TYPE, new TrashPackInstance(System.currentTimeMillis(), 0));
                        instance.setTier(trashPack.getTier());
                        DataTypeMethods.setCustom(craftedItemMeta, Keys.TRASH_INSTANCE, PersistentTrashInstanceType.TYPE, instance);
                    }

                    crafted.setItemMeta(craftedItemMeta);
                    ConfigManager.getInstance().saveDankPack(crafted);
                    menu.pushItem(crafted, OUTPUT_SLOT);

                    // Consume items
                    for (int recipeSlot : RECIPE_SLOTS) {
                        menu.consumeItem(recipeSlot, 1, true);
                    }
                    return false;
                });
            }
        };
    }

    private boolean testRecipe(ItemStack[] input, ItemStack[] recipe) {
        for (int test = 0; test < recipe.length; test++) {
            if (!SlimefunUtils.isItemSimilar(input[test], recipe[test], true, false, false)) {
                return false;
            }
        }
        return true;
    }

    private BlockBreakHandler getBlockBreakHandler() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent event, ItemStack itemStack, List<ItemStack> drops) {
                BlockMenu menu = BlockStorage.getInventory(event.getBlock());
                menu.dropItems(menu.getLocation(), RECIPE_SLOTS);
                menu.dropItems(menu.getLocation(), OUTPUT_SLOT);
            }
        };
    }

    private static void addRecipe(ItemStack[] input, ItemStack output) {
        RECIPE_MAP.put(input, output);
    }

}
