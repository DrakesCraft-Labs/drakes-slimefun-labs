package ne.fnfal113.relicsofcthonia.slimefun.relics;

import com.google.gson.JsonObject;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import dev.drake.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.RelicsRegistry;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.core.Keys;
import ne.fnfal113.relicsofcthonia.core.RecipeTypes;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static ne.fnfal113.relicsofcthonia.RelicsOfCthonia.CONFIG_MANAGER;

public abstract class AbstractRelic extends UnplaceableBlock {
    private static final List<Material> DEFAULT_BLOCK_SOURCES = List.of(
            Material.NETHERRACK, Material.BLACKSTONE, Material.BASALT, Material.MAGMA_BLOCK,
            Material.SOUL_SOIL, Material.SOUL_SAND, Material.NETHER_WART_BLOCK, Material.WARPED_WART_BLOCK,
            Material.CRIMSON_NYLIUM, Material.MYCELIUM, Material.PODZOL, Material.WARPED_NYLIUM,
            Material.GRAVEL, Material.NETHER_GOLD_ORE, Material.NETHER_QUARTZ_ORE, Material.ANCIENT_DEBRIS
    );

    @Getter private final Rarity rarity;

    @Getter private final double dropChance;
    @Getter private final int piglinRewardAmount;
    @Getter private final int defaultDropSize;

    private final SlimefunItemStack templateItem;

    @ParametersAreNonnullByDefault
    public AbstractRelic(ItemGroup itemGroup, SlimefunItemStack item, Rarity rarity, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, RecipeTypes.DROP_TYPE, new ItemStack[9]);
        this.rarity = rarity;
        initSingleSectionSettings(dropChance, piglinRewardAmount);
        initDefaultWhereToDrop(defaultDropSize);
        initDefaultPiglinRewards(defaultDropSize);
        this.dropChance = CONFIG_MANAGER.getCustomConfig("relic-settings").getDouble(this.getId() + "." + "drop-chance") / 100.0;
        this.piglinRewardAmount = CONFIG_MANAGER.getCustomConfig("relic-settings").getInt(this.getId() + "." + "piglin-reward-amount");
        this.defaultDropSize = defaultDropSize;
        this.templateItem = (SlimefunItemStack) item.clone();
    }

    @Override
    public @NotNull ItemStack getItem() {
        return templateItem;
    }

    public ItemStack randomRelic() {
        ItemStack itemStack = templateItem.clone();
        int condition = ThreadLocalRandom.current().nextInt(1,100);
        itemStack.editMeta(meta -> PersistentDataAPI.setInt(meta, Keys.RELIC_CONDITION, condition));
        Utils.replaceLoreValue(itemStack, "%", "&d", "","%", condition);
        return itemStack;
    }

    public static int getRelicCondition(@Nonnull ItemStack itemStack){
        return PersistentDataAPI.getInt(itemStack.getItemMeta(), Keys.RELIC_CONDITION, 0);
    }

    public void postInit() {
        Utils.replaceLoreValue(templateItem, "%", "&e", "", "%", dropChance * 100);
        registerRelic();
        templateItem.lock();
    }

    private void registerRelic() {
        List<String> sourcesLore = new ArrayList<>();
        List<String> rewardsLore = new ArrayList<>();

        try {
            FileConfiguration config = CONFIG_MANAGER.getCustomConfig("relic-settings");
            ConfigurationSection section = config.getConfigurationSection(this.getId());
            if (section == null) {
                RelicsOfCthonia.getInstance().getLogger().warning("No configuration section found for relic: " + this.getId());
                return;
            }

            // retrieve this relic's config material list then add to relic registry
            if (section.contains("drops-on-material")) {
                for (String material : section.getStringList("drops-on-material")) {
                    Material mat = Material.matchMaterial(material);
                    if (mat == null) {
                        RelicsOfCthonia.getInstance().getLogger().warning("Invalid material '" + material + "' for relic: " + this.getId());
                        continue;
                    }
                    RelicsRegistry.BLOCK_SOURCES.compute(mat, (key, relics) -> {
                        if (relics == null) {
                            relics = new ArrayList<>();
                        }
                        relics.add(this);
                        return relics;
                    });
                    sourcesLore.add(Utils.translate(mat.getItemTranslationKey()));
                }
            } else {
                RelicsOfCthonia.getInstance().getLogger().warning("No 'drops-on-material' section found for relic: " + this.getId());
            }

            // retrieve this relic's config mob list then add to relic registry
            if(section.contains("drops-on-mob")) {
                for (String type : section.getStringList("drops-on-mob")) {
                    try {
                        EntityType entityType = EntityType.valueOf(type.toUpperCase());
                        RelicsRegistry.ENTITY_SOURCES.compute(entityType, (mobType, relics) -> {
                            if (relics == null) {
                                relics = new ArrayList<>();
                            }
                            relics.add(this);
                            return relics;
                        });
                        sourcesLore.add(Utils.translate(entityType.translationKey()));
                    } catch (IllegalArgumentException e) {
                        RelicsOfCthonia.getInstance().getLogger().warning("Invalid entity type '" + type + "' for relic: " + this.getId());
                    }
                }
            } else {
                RelicsOfCthonia.getInstance().getLogger().warning("No 'drops-on-mob' section found for relic: " + this.getId());
            }

            // retrieve this relic's config rewards list then add to relic registry
            if (section.contains("piglin-barter-rewards")) {
                for (String id : section.getStringList("piglin-barter-rewards")) {
                    Material material = Material.matchMaterial(id);
                    SlimefunItem item = SlimefunItem.getById(id);
                    if (item == null && material == null) {
                        RelicsOfCthonia.getInstance().getLogger().warning("Invalid item ID '" + id + "' for relic: " + this.getId());
                        continue;
                    }
                    RelicsRegistry.RELIC_OUTPUTS.compute(this, (relic, rewards) -> {
                        if (rewards == null) {
                            rewards = new ArrayList<>();
                        }
                        rewards.add(item != null ? item.getItem() : new ItemStack(material));
                        return rewards;
                    });
                    rewardsLore.add(item != null ? ChatColor.stripColor(item.getItemName()) : Utils.translate(material.getItemTranslationKey()));
                }
            } else {
                RelicsOfCthonia.getInstance().getLogger().warning("No 'piglin-barter-rewards' section found for relic: " + this.getId());
            }
        } catch (Exception e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred on adding data to relics registry! Please report on github issue tracker!");
            e.printStackTrace();
        }

        Utils.replaceLoreList(templateItem, "Drops on:", "&e", "‣ ", sourcesLore);
        Utils.replaceLoreList(templateItem, "Possible Piglin reward:", "&a", "‣ " + piglinRewardAmount + "x ", rewardsLore);
    }

    public void initSingleSectionSettings(double dropChance, int piglinRewardAmount) {
        try {
            CONFIG_MANAGER.initializeConfig(this.getId(), "drop-chance", dropChance, "relic-settings");
            CONFIG_MANAGER.initializeConfig(this.getId(), "piglin-reward-amount", piglinRewardAmount, "relic-settings");
        } catch (IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred upon initializing default single section settings! Please report on github issue tracker!");
            e.printStackTrace();
        }
    }

    public void initDefaultWhereToDrop(int defaultDropSize) {
        try {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            JsonObject jsonObject = CONFIG_MANAGER.loadJson("nether_mobs");
            List<String> randomMobList = new ArrayList<>();
            List<String> randomMaterialList = new ArrayList<>();

            // retrieve json resource for nether mobs and
            // create a randomized mob list, size determined dy this object default drop size
            for (int i = 0; i < defaultDropSize; i++) {
                String mob = jsonObject.getAsJsonPrimitive("nether_mob_" + random.nextInt(1, 12)).getAsString();
                if(!randomMobList.contains(mob)) {
                    randomMobList.add(mob);
                }
            }

            // create a randomized material list, size determined dy this object default drop size
            for (int i = 0; i < defaultDropSize; i++) {
                String material = DEFAULT_BLOCK_SOURCES.get(random.nextInt(0, DEFAULT_BLOCK_SOURCES.size())).toString();
                if(!randomMaterialList.contains(material)) {
                    randomMaterialList.add(material);
                }
            }

            CONFIG_MANAGER.initializeConfig(getId(), "drops-on-mob", randomMobList, "relic-settings");
            CONFIG_MANAGER.initializeConfig(getId(), "drops-on-material", randomMaterialList, "relic-settings");
        } catch (IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred upon initializing default drop settings! Please report on github issue tracker!");
            e.printStackTrace();
        }
    }

    public void initDefaultPiglinRewards(int defaultDropSize) {
        try {
            JsonObject jsonObject = CONFIG_MANAGER.loadJson("piglin_barter_list");
            List<String> randomRewardList = new ArrayList<>();

            // retrieve json resource for barter rewards and
            // create a randomized reward list, size determined dy this object default drop size
            for (int i = 1; i <= defaultDropSize; i++) {
                String reward = jsonObject.getAsJsonObject(rarity.name()).get("drop-" + ThreadLocalRandom.current().nextInt(1,8)).getAsString();
                if(!randomRewardList.contains(reward)) {
                    randomRewardList.add(reward);
                }
            }

            CONFIG_MANAGER.initializeConfig(getId(), "piglin-barter-rewards", randomRewardList, "relic-settings");
        } catch (IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred upon initializing default piglin rewards! Please report on github issue tracker!");
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDisenchantable() {
        return false;
    }

    @Override
    public boolean isEnchantable(){
        return false;
    }
}