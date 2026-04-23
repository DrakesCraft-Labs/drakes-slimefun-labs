package me.voper.slimeframe.implementation.items.relics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.jeff_media.morepersistentdatatypes.DataType;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.libraries.commons.lang.Validate;
import com.github.drakescraft_labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.ItemUtils;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.api.events.PlayerOpenRelicEvent;
import me.voper.slimeframe.api.events.PlayerRefineRelicEvent;
import me.voper.slimeframe.core.managers.SettingsManager;
import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.utils.ChatUtils;
import me.voper.slimeframe.utils.Colors;
import me.voper.slimeframe.utils.Keys;
import me.voper.slimeframe.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

@Getter
public class Relic extends SlimefunItem implements NotPlaceable {

    private static final SlimeFrame plugin = SlimeFrame.getInstance();

    private static final RecipeType LITH_RECIPE_TYPE = new RecipeType(Keys.createKey("lith_recipe"), new CustomItemStack(Material.FISHING_ROD, ChatColor.AQUA + "Go fishing!", ChatColor.WHITE + "One random lith relic", ChatColor.WHITE + "for every " + SlimeFrame.getSettingsManager().getInt(SettingsManager.ConfigField.LITH_RELIC) + " fishes caught!"));
    private static final RecipeType MESO_RECIPE_TYPE = new RecipeType(Keys.createKey("meso_recipe"), new CustomItemStack(Material.DIAMOND_SWORD, ChatColor.AQUA + "Go killing!", ChatColor.WHITE + "One random meso relic", ChatColor.WHITE + "for every " + SlimeFrame.getSettingsManager().getInt(SettingsManager.ConfigField.MESO_RELIC) + " entities killed!"));
    private static final RecipeType NEO_RECIPE_TYPE = new RecipeType(Keys.createKey("neo_recipe"), new CustomItemStack(Material.DIAMOND_PICKAXE, ChatColor.AQUA + "Go mining!", ChatColor.WHITE + "One random neo relic", ChatColor.WHITE + "for every " + SlimeFrame.getSettingsManager().getInt(SettingsManager.ConfigField.NEO_RELIC) + " blocks broken!"));
    private static final RecipeType AXI_RECIPE_TYPE = new RecipeType(Keys.createKey("axi_recipe"), new CustomItemStack(Material.BRICKS, ChatColor.AQUA + "Go building!", ChatColor.WHITE + "One random axi relic", ChatColor.WHITE + "for every " + SlimeFrame.getSettingsManager().getInt(SettingsManager.ConfigField.AXI_RELIC) + " blocks placed!"));

    private final Era era;
    private final SlimefunItemStack[] commonDrops;
    private final SlimefunItemStack[] uncommonDrops;
    private final SlimefunItemStack rareDrop;

    private Relic(ItemGroup itemGroup, RelicItemStack item, RecipeType recipeType) {
        super(itemGroup, item, recipeType, Utils.NULL_ITEMS_ARRAY);
        this.era = item.getRelicEra();
        this.commonDrops = item.getCommonDrops();
        this.uncommonDrops = item.getUncommonDrops();
        this.rareDrop = item.getRareDrop();
    }

    public static Relic create(@Nonnull RelicItemStack relicItemStack) {
        Relic relic = switch (relicItemStack.getRelicEra()) {
            case LITH -> new Relic(Groups.LITH, relicItemStack, LITH_RECIPE_TYPE);
            case MESO -> new Relic(Groups.MESO, relicItemStack, MESO_RECIPE_TYPE);
            case NEO -> new Relic(Groups.NEO, relicItemStack, NEO_RECIPE_TYPE);
            case AXI -> new Relic(Groups.AXI, relicItemStack, AXI_RECIPE_TYPE);
        };
        Validate.notNull(relic, "An error has occurred while creating a new relic!");
        return relic;
    }

    @Override
    public void preRegister() {
        addItemHandler(onBlockPlace());
        addItemHandler(onRelicUse());
    }

    protected BlockPlaceHandler onBlockPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                e.setCancelled(true);
            }
        };
    }

    protected ItemUseHandler onRelicUse() {
        return e -> {
            e.cancel();
            Player p = e.getPlayer();
            ItemStack relic = e.getItem();

            if (getReactants(relic) != 10) {
                ChatUtils.sendMessage(p, SlimeFrame.getSettingsManager().getStringList(SettingsManager.ConfigField.INSUFFICIENT_REACTANTS));
                return;
            }

            EnumeratedDistribution<SlimefunItemStack> dropDistribution = getDropDistribution(relic);

            int voidTraces = PersistentDataAPI.getInt(p, Keys.VOID_TRACES_OWNED, 0);
            int voidTracesReward = ThreadLocalRandom.current().nextInt(20) + 1;
            SlimefunItemStack reward = dropDistribution.sample();

            PlayerOpenRelicEvent relicEvent = new PlayerOpenRelicEvent(p, reward, relic, voidTracesReward);
            Bukkit.getPluginManager().callEvent(relicEvent);
            if (relicEvent.isCancelled()) return;

            PersistentDataAPI.set(p, Keys.VOID_TRACES_OWNED, PersistentDataType.INTEGER, voidTraces + relicEvent.getVoidTracesReward());

            ItemUtils.consumeItem(relicEvent.getRelic(), false);

            HashMap<Integer, ItemStack> result = p.getInventory().addItem(relicEvent.getReward());
            if (!result.isEmpty()) {
                p.getWorld().dropItemNaturally(p.getLocation(), relicEvent.getReward());
            }

            ChatUtils.sendMessage(p, ChatColor.GREEN + "Relic opened!");
            ChatUtils.sendMessage(p, ChatColor.GREEN + "You have just received " + ChatColor.WHITE + voidTracesReward + ChatColor.GREEN + " void traces for opening this relic!");

        };
    }

    @ParametersAreNonnullByDefault
    public static void refineRelic(Player p, ItemStack relic, Refinement refinement) {
        PlayerRefineRelicEvent refineEvent = new PlayerRefineRelicEvent(p, relic, refinement);
        Bukkit.getPluginManager().callEvent(refineEvent);
        if (refineEvent.isCancelled()) return;

        refinement = refineEvent.getNewRefinement();

        int tracesRequired = refinement.getTracesRequired() - getRefinement(relic).getTracesRequired();
        if (tracesRequired <= 0) {
            ChatUtils.sendMessage(p, ChatColor.RED + "You can not refine your relic to a lower refinement level");
            return;
        }

        if (p.getGameMode() == GameMode.CREATIVE) {
            setRefinement(relic, refinement);
            return;
        }

        int tracesOwned = PersistentDataAPI.getInt(p, Keys.VOID_TRACES_OWNED, 0);

        if (tracesOwned < tracesRequired) {
            ChatUtils.sendMessage(p,
                    ChatColor.RED + "You do not have enough void traces to refine this relic",
                    ChatColor.RED + "Amount of void traces needed: " + Colors.ORANGE + tracesRequired);
            return;
        }

        ChatUtils.sendMessage(p, ChatColor.GREEN + "Relic successfully refined to " + ChatColor.WHITE + refinement.name());
        PersistentDataAPI.set(p, Keys.VOID_TRACES_OWNED, PersistentDataType.INTEGER, tracesOwned - tracesRequired);

        setRefinement(relic, refinement);
    }

    public EnumeratedDistribution<SlimefunItemStack> getDropDistribution(@Nonnull ItemStack relic) {
        // Adjust probabilities
        Refinement relicRefinement = getRefinement(relic);
        double sumOfProbabilities = commonDrops.length * (relicRefinement.getCommonProbability() / 100.0) +
                uncommonDrops.length * (relicRefinement.getUncommonProbability() / 100.0) +
                (relicRefinement.getRareProbability() / 100.0);

        List<Pair<SlimefunItemStack, Double>> dropProbabilities = new ArrayList<>();

        for (SlimefunItemStack common : commonDrops) {
            dropProbabilities.add(new Pair<>(common, (relicRefinement.commonProbability / 100.0) / sumOfProbabilities));
        }

        for (SlimefunItemStack uncommon : uncommonDrops) {
            dropProbabilities.add(new Pair<>(uncommon, (relicRefinement.uncommonProbability / 100.0) / sumOfProbabilities));
        }

        dropProbabilities.add(new Pair<>(rareDrop, (relicRefinement.getRareProbability() / 100.0) / sumOfProbabilities));

        return new EnumeratedDistribution<>(dropProbabilities);
    }

    public static void incrementReactants(Player player, @Nonnull ItemStack relic) {
        ItemMeta itemMeta = relic.getItemMeta();
        if (itemMeta == null) return;

        SlimefunItem byItem = SlimefunItem.getByItem(relic);
        if (byItem == null || !(byItem instanceof Relic)) return;

        int reactants = getReactants(relic);
        if (reactants >= 10) return;

        List<String> lore = itemMeta.getLore();
        lore.set(1, lore.get(1).replaceFirst(String.valueOf(reactants), String.valueOf(++reactants)));
        itemMeta.setLore(lore);
        PersistentDataAPI.set(itemMeta, Keys.REACTANTS_COUNTER, PersistentDataType.INTEGER, reactants);
        relic.setItemMeta(itemMeta);

        if (reactants == 10) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            ChatUtils.sendMessage(player, ChatColor.GREEN + "Your relic is ready to be opened!");
        }

    }

    public static int getReactants(@Nonnull ItemStack relic) {
        ItemMeta itemMeta = relic.getItemMeta();
        return PersistentDataAPI.getInt(itemMeta, Keys.REACTANTS_COUNTER, 0);
    }

    @Nonnull
    public static Relic.Refinement getRefinement(@Nonnull ItemStack relic) {
        return PersistentDataAPI.get(relic.getItemMeta(), Keys.RELIC_REFINEMENT, DataType.asEnum(Relic.Refinement.class), Relic.Refinement.INTACT);
    }

    public static void setRefinement(@Nonnull ItemStack relic, Relic.Refinement refinement) {
        ItemMeta itemMeta = relic.getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.set(2, lore.get(2).replace(getRefinement(relic).name(), refinement.name()));
        itemMeta.setLore(lore);
        PersistentDataAPI.set(itemMeta, Keys.RELIC_REFINEMENT, DataType.asEnum(Relic.Refinement.class), refinement);
        relic.setItemMeta(itemMeta);
    }

    public enum Era {
        LITH, MESO, NEO, AXI
    }

    @Getter
    @AllArgsConstructor
    public enum Refinement {
        INTACT(76, 22, 2, 0),
        EXCEPTIONAL(70, 26, 4, 25),
        FLAWLESS(60, 34, 6, 50),
        RADIANT(50, 40, 10, 100);

        private final int commonProbability;
        private final int uncommonProbability;
        private final int rareProbability;
        private final int tracesRequired;

    }

}
