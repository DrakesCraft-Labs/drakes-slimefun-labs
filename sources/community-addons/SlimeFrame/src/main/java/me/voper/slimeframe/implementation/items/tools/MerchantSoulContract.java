package me.voper.slimeframe.implementation.items.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import com.github.drakescraft_labs.slimefun4.utils.compatibility.VersionedEnchantment;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.EntityInteractHandler;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import dev.drake.dough.data.persistent.PersistentDataAPI;
import dev.drake.dough.items.ItemUtils;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.core.datatypes.MerchantRecipeListDataType;
import me.voper.slimeframe.core.managers.SettingsManager;
import me.voper.slimeframe.utils.ChatUtils;
import me.voper.slimeframe.utils.Colors;
import me.voper.slimeframe.utils.Keys;
import me.voper.slimeframe.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class MerchantSoulContract extends SimpleSlimefunItem<EntityInteractHandler> {

    private static final String AE_ITEM_IDENTIFIER = "internal=H4sIAAAAAAAA/";

    private static final Predicate<MerchantRecipe> REMOVE_AE_TRADES = (merchantRecipe) -> {
        ItemStack result = merchantRecipe.getResult();
        if (SlimeFrame.getSettingsManager().getBoolean(SettingsManager.ConfigField.ENABLE_AE_ITEMS_IN_SOUL_CONTRACTS) || !result.hasItemMeta())
            return false;
        ItemMeta itemMeta = result.getItemMeta();
        return itemMeta.toString().contains(AE_ITEM_IDENTIFIER);
    };

    public MerchantSoulContract(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        this.disenchantable = false;
        this.enchantable = false;
        setUseableInWorkbench(false);
    }

    @Nonnull
    @Override
    public EntityInteractHandler getItemHandler() {
        return (e, item, offhand) -> {
            Player p = e.getPlayer();

            if (e.isCancelled() || !Slimefun.getProtectionManager().hasPermission(p, e.getRightClicked().getLocation(), Interaction.INTERACT_ENTITY) || !Slimefun.getProtectionManager().hasPermission(p, e.getRightClicked().getLocation(), Interaction.ATTACK_ENTITY)) {
                // They don't have permission to use it in this area
                return;
            }

            if (e.getRightClicked() instanceof AbstractVillager merchant) {
                ItemMeta meta = item.getItemMeta();
                if (meta == null) {
                    e.setCancelled(true);
                    return;
                }

                if (PersistentDataAPI.has(meta, Keys.MERCHANT_RECIPE, new MerchantRecipeListDataType())) {
                    e.setCancelled(true);
                    p.sendMessage("Contract already sealed");
                    return;
                }

                final List<MerchantRecipe> recipes = new ArrayList<>(merchant.getRecipes());
                if (recipes.isEmpty()) {
                    e.setCancelled(true);
                    p.sendMessage("This merchant does not have any trade available for you");
                    return;
                }

                // Removing every trade that has a AdvancedEnchantment item involved according to the config
                recipes.removeIf(REMOVE_AE_TRADES);

                // Filtering each recipe to remove any Material.AIR from their ingredients
                for (MerchantRecipe recipe : recipes) {
                    List<ItemStack> ingredients = recipe.getIngredients().stream()
                            .filter(itemStack -> itemStack.getType() != Material.AIR)
                            .toList();

                    recipe.setIngredients(ingredients);
                }

                PersistentDataAPI.set(meta, Keys.MERCHANT_RECIPE, new MerchantRecipeListDataType(), recipes);

                // Update the meta
                meta.addEnchant(VersionedEnchantment.LUCK_OF_THE_SEA, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GREEN + "Trades:");
                lore.add(" ");

                recipes.forEach(recipe -> {
                    List<String> ingredientsNames = recipe.getIngredients().stream()
                            .filter(itemStack -> itemStack.getItemMeta() != null)
                            .map(itemStack -> itemStack.getAmount() + " " + (
                                    itemStack.getItemMeta().hasDisplayName() ?
                                            itemStack.getItemMeta().getDisplayName() :
                                            Utils.formatMaterialString(itemStack.getType()))
                            )
                            .toList();

                    ItemMeta resultMeta = recipe.getResult().getItemMeta();
                    if (resultMeta == null) return;
                    String resultName = resultMeta.hasDisplayName() ? resultMeta.getDisplayName() : Utils.formatMaterialString(recipe.getResult().getType());
                    resultName = recipe.getResult().getAmount() + " " + resultName;

                    lore.add(ChatColor.WHITE + String.valueOf(recipes.indexOf(recipe) + 1) + " - " +
                            ChatColor.BLUE + String.join(" + ", ingredientsNames) +
                            ChatColor.DARK_PURPLE + " -> " +
                            Colors.ORANGE + resultName
                    );
                });

                ItemStack contract = new ItemStack(item);
                contract.setAmount(1);
                meta.setLore(lore);
                contract.setItemMeta(meta);

                ItemUtils.consumeItem(item, false);
                HashMap<Integer, ItemStack> result = p.getInventory().addItem(contract);
                if (!result.isEmpty()) {
                    p.getWorld().dropItemNaturally(p.getLocation(), contract);
                }

                double offset = ThreadLocalRandom.current().nextDouble(0.5);

                merchant.getWorld().playSound(merchant.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1.4F);
                merchant.getWorld().spawnParticle(Particle.CRIMSON_SPORE, merchant.getLocation(), 10, 0, offset / 2, 0, 0);
                merchant.getWorld().spawnParticle(Particle.ENCHANT, merchant.getLocation(), 5, 0.04, 1, 0.04);

                e.setCancelled(true);
                merchant.setHealth(0);
                ChatUtils.sendMessage(p, "Soul Contract successfully sealed");
            }
        };
    }

}