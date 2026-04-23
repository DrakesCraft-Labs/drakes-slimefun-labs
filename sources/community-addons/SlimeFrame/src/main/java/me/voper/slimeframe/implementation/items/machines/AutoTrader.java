package me.voper.slimeframe.implementation.items.machines;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft-labs.slimefun4.implementation.operations.CraftingOperation;
import com.github.drakescraft-labs.slimefun4.libraries.dough.blocks.BlockPosition;
import com.github.drakescraft-labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import com.github.drakescraft-labs.slimefun4.libraries.dough.inventory.InvUtils;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft-labs.slimefun4.utils.ChestMenuUtils;
import com.github.drakescraft-labs.slimefun4.utils.HeadTexture;
import com.github.drakescraft-labs.slimefun4.utils.SlimefunUtils;
import com.github.drakescraft-labs.slimefun4.utils.itemstack.ItemStackWrapper;

import me.voper.slimeframe.core.datatypes.MerchantRecipeListDataType;
import me.voper.slimeframe.implementation.SFrameStacks;
import me.voper.slimeframe.implementation.items.abstracts.AbstractProcessorMachine;
import me.voper.slimeframe.utils.Keys;
import me.voper.slimeframe.utils.MachineUtils;
import me.voper.slimeframe.utils.Utils;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

@ParametersAreNonnullByDefault
public class AutoTrader extends AbstractProcessorMachine implements RecipeDisplayItem {

    private static final String BLOCK_KEY = "auto_trader";
    private static final ItemStack SELECT_TRADE = new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + "Valid Contract", ChatColor.WHITE + "Select one of the trades");

    private static final Map<BlockPosition, List<MerchantRecipe>> recipesMap = new HashMap<>();
    private static final Map<BlockPosition, Integer> selectedTradeMap = new HashMap<>();

    public AutoTrader(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected MachineRecipe findNextRecipe(@Nonnull BlockMenu menu) {
        final BlockPosition bp = new BlockPosition(menu.getLocation());
        Integer trade = selectedTradeMap.get(bp);

        if (trade == -1) return null;

        List<MerchantRecipe> recipeList = recipesMap.get(bp);
        MerchantRecipe selectedRecipe = recipeList.get(trade);

        // Creates a slot-ItemStack map according to the input slots
        Map<Integer, ItemStack> inv = new HashMap<>();

        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);

            if (item != null) {
                inv.put(slot, ItemStackWrapper.wrap(item));
            }
        }

        // If the output slots are full, return null
        int maxedSlots = 0;
        for (int slot : getOutputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            if (item != null && item.getAmount() == item.getMaxStackSize()) {
                maxedSlots += 1;
            }
        }
        if (maxedSlots == getOutputSlots().length) {
            MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.NO_SPACE);
            return null;
        }

        Map<Integer, Integer> found = new HashMap<>();
        for (ItemStack input : selectedRecipe.getIngredients()) {
            for (int slot : getInputSlots()) {
                if (SlimefunUtils.isItemSimilar(inv.get(slot), input, true)) {
                    found.put(slot, input.getAmount());
                    break;
                }
            }
        }

        if (found.size() == selectedRecipe.getIngredients().size()) {
            MachineRecipe machineRecipe = new MachineRecipe(((trade + 1) * 10) / getProcessingSpeed(), selectedRecipe.getIngredients().toArray(new ItemStack[0]), new ItemStack[]{selectedRecipe.getResult()});
            if (!InvUtils.fitAll(menu.toInventory(), machineRecipe.getOutput(), getOutputSlots())) {
                MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.NO_SPACE);
                return null;
            }

            for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                menu.consumeItem(entry.getKey(), entry.getValue());
            }

            return machineRecipe;
        } else {
            found.clear();
        }

        return null;
    }

    @Override
    protected boolean checkCraftConditions(@Nonnull BlockMenu menu) {
        final BlockPosition bp = new BlockPosition(menu.getLocation());

        // There is no contract in the contract slot
        ItemStack contract = menu.getItemInSlot(getContractSlot());
        ItemMeta contractItemMeta = contract != null && contract.hasItemMeta() ? contract.getItemMeta() : null;
        if (contract == null || contract.getType() == Material.AIR || contractItemMeta == null) {
            recipesMap.put(bp, null);
            return false;
        }

        // Invalid contract --> no pdc
        List<MerchantRecipe> recipeList = PersistentDataAPI.get(contractItemMeta, Keys.MERCHANT_RECIPE, new MerchantRecipeListDataType());
        if (recipeList == null || recipeList.isEmpty()) return false;

        List<MerchantRecipe> recipes = recipesMap.get(bp);
        Integer trade = selectedTradeMap.get(bp);

        if (recipes == null || !compareMerchantRecipes(recipeList, recipes)) {
            recipesMap.put(bp, recipeList);
            selectedTradeMap.put(bp, -1);
            updateTradesSlots(menu, contractItemMeta);
        }

        // If no trade was selected
        if (trade == -1) {
            menu.replaceExistingItem(getStatusSlot(), SELECT_TRADE);
        } else {
            // One of the trades is selected but the machine is not running an operation
            CraftingOperation operation = getMachineProcessor().getOperation(menu.getBlock());
            if (operation == null) {
                menu.replaceExistingItem(getStatusSlot(), MachineUtils.WAITING);
            }
        }

        return true;
    }

    // A way to compare two lists of MerchantRecipe as it does not implement the method equals()
    private boolean compareMerchantRecipes(List<MerchantRecipe> one, List<MerchantRecipe> two) {
        if (one.size() != two.size()) return false;
        for (int i = 0; i < one.size(); i++) {
            MerchantRecipe recipeOne = one.get(i);
            MerchantRecipe recipeTwo = two.get(i);
            if (!recipeOne.getIngredients().equals(recipeTwo.getIngredients())) return false;
            if (!recipeOne.getResult().equals(recipeTwo.getResult())) return false;
        }
        return true;
    }

    @Override
    protected void onCraftConditionsNotMet(BlockMenu menu) {
        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), new CustomItemStack(Material.BARRIER, ChatColor.DARK_RED + "Merchant Soul Contract required",
                ChatColor.RED + "There must be a valid merchant contract",
                ChatColor.RED + "in the contract slot!")
        );
        MachineUtils.replaceExistingItemViewer(menu, getTradesSlots(), new CustomItemStack(Material.RED_STAINED_GLASS_PANE, " "));
    }

    private void updateTradesSlots(@Nonnull BlockMenu menu, @Nonnull ItemMeta contractMeta) {
        List<String> lore = contractMeta.getLore();
        if (lore == null) return;

        List<String> trades = lore.subList(2, lore.size()).stream()
                .map(s -> s.substring(s.indexOf("-") + 1).trim())
                .toList();

        for (int i = 0; i < trades.size(); i++) {
            menu.replaceExistingItem(getTradesSlots()[i], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + "Trade " + (i + 1), trades.get(i)));
        }
    }

    @Override
    protected void onNewInstance(BlockMenu menu, Block b) {
        super.onNewInstance(menu, b);

        final BlockPosition bp = new BlockPosition(b);

        // Set up the interactions
        for (int slot : getTradesSlots()) {
            menu.addMenuClickHandler(slot, tradeSelectionHandler(menu));
        }

        // Init merchantRecipes variable
        ItemStack contract = menu.getItemInSlot(getContractSlot());
        if (contract != null && contract.getItemMeta() != null) {
            List<MerchantRecipe> merchantRecipeList = PersistentDataAPI.get(contract.getItemMeta(), Keys.MERCHANT_RECIPE, new MerchantRecipeListDataType());
            if (merchantRecipeList != null && !merchantRecipeList.isEmpty()) {
                recipesMap.put(bp, merchantRecipeList);
                updateTradesSlots(menu, contract.getItemMeta());
            }
        }

        // Init the selected trade
        if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), BLOCK_KEY) == null) {
            BlockStorage.addBlockInfo(b, BLOCK_KEY, String.valueOf(-1));
            selectedTradeMap.put(bp, -1);
        } else {
            selectedTradeMap.put(bp, Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), BLOCK_KEY)));
            if (selectedTradeMap.get(bp) != -1) {
                Utils.enchant(menu.getItemInSlot(getTradesSlots()[selectedTradeMap.get(bp)]));
            }
        }
    }

    @Override
    protected void createMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "), new int[]{2, 11, 20, 29, 38, 47});
        preset.drawBackground(ChestMenuUtils.getInputSlotTexture(), new int[]{3, 4, 5, 6, 7, 8, 12, 17, 21, 22, 23, 24, 25, 26});
        preset.drawBackground(ChestMenuUtils.getOutputSlotTexture(), new int[]{30, 31, 32, 33, 34, 35, 39, 44, 48, 49, 50, 51, 52, 53});
        preset.drawBackground(new CustomItemStack(Material.RED_STAINED_GLASS_PANE, " "), getTradesSlots());
        // Contract status slot
        preset.addItem(1, new CustomItemStack(HeadTexture.CARGO_ARROW_LEFT.getAsItemStack(), ChatColor.GREEN + "<<< " + ChatColor.WHITE + "Contract Slot"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(getStatusSlot(), MachineUtils.STATUS, ChestMenuUtils.getEmptyClickHandler());
    }

    @SuppressWarnings("deprecation")
    private ChestMenu.MenuClickHandler tradeSelectionHandler(@Nonnull BlockMenu menu) {
        return (player, slot, itemstack, clickAction) -> {
            // The trade is locked
            if (itemstack.getType() == Material.RED_STAINED_GLASS_PANE) return false;

            ItemMeta itemMeta = itemstack.getItemMeta();
            if (itemMeta == null) return false;

            List<Integer> tradesSlots = Arrays.stream(getTradesSlots()).boxed().toList();
            int tradeIndex = tradesSlots.indexOf(slot);

            final BlockPosition bp = new BlockPosition(menu.getLocation());
            Integer trade = selectedTradeMap.get(bp);

            // The clicked trade is already selected
            if (tradeIndex == trade) {
                selectedTradeMap.put(bp, -1);
                itemMeta.removeEnchant(Enchantment.LUCK);
            } else {
                if (trade != -1) {
                    ItemStack itemInSlot = menu.getItemInSlot(getTradesSlots()[trade]);
                    Utils.disenchant(itemInSlot);
                }
                selectedTradeMap.put(bp, tradeIndex);
                itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            }

            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemstack.setItemMeta(itemMeta);
            BlockStorage.addBlockInfo(menu.getLocation(), BLOCK_KEY, String.valueOf(selectedTradeMap.get(bp)));
            return false;
        };
    }

    @Override
    protected void onBreak(BlockBreakEvent e, BlockMenu menu, Location l) {
        super.onBreak(e, menu, l);
        menu.dropItems(l, getContractSlot());
    }

    @Nonnull
    @Override
    public int[] getInputSlots() {
        return new int[]{13, 14, 15, 16};
    }

    @Nonnull
    @Override
    public int[] getOutputSlots() {
        return new int[]{40, 41, 42, 43};
    }

    @Override
    public int getStatusSlot() {
        return 2;
    }

    protected int[] getTradesSlots() {
        return new int[]{9, 10, 18, 19, 27, 28, 36, 37, 45, 46};
    }

    protected int getContractSlot() {
        return 0;
    }

    @Override
    protected ItemStack getProgressBar() {
        return new ItemStack(Material.EMERALD);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return List.of(SFrameStacks.MERCHANT_SOUL_CONTRACT.clone());
    }

}