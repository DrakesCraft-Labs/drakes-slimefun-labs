package com.github.drakescraft_labs.slimefunluckyblocks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import dev.drake.dough.common.ChatColors;
import dev.drake.dough.common.CommonPatterns;
import dev.drake.dough.config.Config;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;
import dev.drake.dough.updater.GitHubBuildsUpdater;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.CustomItemSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.Surprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.CakeSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.CookedFoodSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.DiamondBlockPillarSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.DiamondBlockSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.EmeraldBlockSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.GoldenAppleSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.IronBlockSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.LuckyAxeSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.LuckyBootsSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.LuckyChestplateSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.LuckyHelmetSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.LuckyLeggingsSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.LuckyPickaxeSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.LuckyPotionsSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.LuckySwordSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.TamedCatsSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.TamedDogsSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.UnluckyPotionsSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.ValuablesSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky.XPRainSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.ChickenRainSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.CookieSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.DyeSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.FishSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.GrootSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.HaySurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.JebSheepSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.JerrySlimeSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.PotatOSSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.RainbowSheepSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.RawFoodSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.VillagersSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral.WanderingTraderSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.pandora.IronGolemsSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.pandora.ReapersSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.AnvilRainSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.BryanZombieSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.ChargedCreeperSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.CobwebSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.EnclosedWaterSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.ExplosionSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.FakeDiamondBlock;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.FlyingCreeperSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.FlyingTNTSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.GiantSlimeSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.HighJumpSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.TNTRainSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.VoidHoleSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.WalshrusSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.WitchSurprise;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.unlucky.ZombiePigmenSurprise;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;

public class SlimefunLuckyBlocks extends JavaPlugin implements SlimefunAddon {

    private static final String TEXTURE = "b3b710b08b523bba7efba07c629ba0895ad61126d26c86beb3845603a97426c";

    private Config cfg;
    private final List<Surprise> surprises = new LinkedList<>();
    private final BlockFace[] blockfaces = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };

    @Override
    public void onEnable() {
        cfg = new Config(this);

        // Setting up bStats


        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/luckyblocks-sf/master").start();
        }

        ItemGroup itemGroup = new ItemGroup(new NamespacedKey(this, "lucky_blocks"), new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode(TEXTURE)), "&rLucky Blocks"));

        SlimefunItemStack luckyBlock = new SlimefunItemStack("LUCKY_BLOCK", TEXTURE, "&fLucky Block", "&7Luck: &f0");
        SlimefunItemStack veryLuckyBlock = new SlimefunItemStack("LUCKY_BLOCK_LUCKY", TEXTURE, "&fVery lucky Block", "&7Luck: &a+80");
        SlimefunItemStack veryUnluckyBlock = new SlimefunItemStack("LUCKY_BLOCK_UNLUCKY", TEXTURE, "&fVery unlucky Block", "&7Luck: &c-80");
        SlimefunItemStack pandorasBox = new SlimefunItemStack("PANDORAS_BOX", "86c7dde512871bd607b77e6635ad39f44f2d5b4729e60273f1b14fba9a86a", "&5Pandora\"s Box", "&7Luck: &c&oERROR");

        // @formatter:off
        new LuckyBlock(itemGroup, luckyBlock, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] { SlimefunItems.GOLD_12K, SlimefunItems.GOLD_12K, SlimefunItems.GOLD_12K, SlimefunItems.GOLD_12K, new ItemStack(Material.DISPENSER), SlimefunItems.GOLD_12K, SlimefunItems.GOLD_12K, SlimefunItems.GOLD_12K, SlimefunItems.GOLD_12K }).register(this, surprises, s -> s.getLuckLevel() != LuckLevel.PANDORA);

        new LuckyBlock(itemGroup, veryLuckyBlock, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] { null, SlimefunItems.GOLD_12K, null, SlimefunItems.GOLD_12K, luckyBlock, SlimefunItems.GOLD_12K, null, SlimefunItems.GOLD_12K, null }).register(this, surprises, s -> s.getLuckLevel() == LuckLevel.LUCKY);

        new LuckyBlock(itemGroup, veryUnluckyBlock, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] { null, new ItemStack(Material.SPIDER_EYE), null, new ItemStack(Material.SPIDER_EYE), luckyBlock, new ItemStack(Material.SPIDER_EYE), null, new ItemStack(Material.SPIDER_EYE), null }).register(this, surprises, s -> s.getLuckLevel() == LuckLevel.UNLUCKY);

        new LuckyBlock(itemGroup, pandorasBox, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] { new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.LAPIS_BLOCK), luckyBlock, new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.OAK_PLANKS) }).register(this, surprises, s -> s.getLuckLevel() == LuckLevel.PANDORA);
        // @formatter:on

        new WorldGenerator(this);

        registerDefaultSurprises();
        registerCustomSurprises();

        getLogger().log(Level.INFO, "Loaded {0} different Surprises!", surprises.size());
    }

    private void registerDefaultSurprises() {
        // Lucky Surprises
        registerSurprise(new CookedFoodSurprise());
        registerSurprise(new GoldenAppleSurprise());
        registerSurprise(new DiamondBlockSurprise());
        registerSurprise(new DiamondBlockPillarSurprise());
        registerSurprise(new EmeraldBlockSurprise());
        registerSurprise(new IronBlockSurprise());
        registerSurprise(new TamedDogsSurprise());
        registerSurprise(new TamedCatsSurprise());
        registerSurprise(new ValuablesSurprise());
        registerSurprise(new LuckySwordSurprise());
        registerSurprise(new LuckyPickaxeSurprise());
        registerSurprise(new LuckyAxeSurprise());
        registerSurprise(new XPRainSurprise());
        registerSurprise(new LuckyHelmetSurprise());
        registerSurprise(new LuckyChestplateSurprise());
        registerSurprise(new LuckyLeggingsSurprise());
        registerSurprise(new LuckyBootsSurprise());
        registerSurprise(new LuckyPotionsSurprise());
        registerSurprise(new UnluckyPotionsSurprise());
        registerSurprise(new CakeSurprise());

        // Neutral Surprises
        registerSurprise(new GrootSurprise());
        registerSurprise(new RawFoodSurprise());
        registerSurprise(new FishSurprise());
        registerSurprise(new WanderingTraderSurprise());
        registerSurprise(new RainbowSheepSurprise());
        registerSurprise(new ChickenRainSurprise());
        registerSurprise(new DyeSurprise());
        registerSurprise(new HaySurprise());
        registerSurprise(new CookieSurprise());
        registerSurprise(new JebSheepSurprise());
        registerSurprise(new VillagersSurprise());
        registerSurprise(new PotatOSSurprise());
        registerSurprise(new JerrySlimeSurprise());

        // Unlucky Surprises
        registerSurprise(new ChargedCreeperSurprise());
        registerSurprise(new WitchSurprise());
        registerSurprise(new ExplosionSurprise());
        registerSurprise(new VoidHoleSurprise());
        registerSurprise(new AnvilRainSurprise());
        registerSurprise(new EnclosedWaterSurprise());
        registerSurprise(new TNTRainSurprise());
        registerSurprise(new FlyingCreeperSurprise());
        registerSurprise(new FlyingTNTSurprise());
        registerSurprise(new FakeDiamondBlock());
        registerSurprise(new BryanZombieSurprise());
        registerSurprise(new WalshrusSurprise());
        registerSurprise(new HighJumpSurprise());
        registerSurprise(new CobwebSurprise());
        registerSurprise(new GiantSlimeSurprise());
        registerSurprise(new ZombiePigmenSurprise());

        // Pandora Box Surprises
        registerSurprise(new ReapersSurprise());
        registerSurprise(new IronGolemsSurprise());
    }

    private void registerCustomSurprises() {
        // CustomItem Surprises
        if (cfg.getValue("custom") != null && !cfg.getKeys("custom").isEmpty()) {
            for (String name : cfg.getKeys("custom")) {
                LuckLevel luckLevel = LuckLevel.NEUTRAL;
                List<ItemStack> items = new ArrayList<>();
                List<String> commands = new ArrayList<>();

                if (cfg.getString("custom." + name + ".lucklevel") != null) {
                    try {
                        luckLevel = LuckLevel.valueOf(cfg.getString("custom." + name + ".lucklevel").toUpperCase());
                    } catch (IllegalArgumentException ex) {
                        getLogger().log(Level.WARNING, "Couldn\"t load lucklevel of CustomItem Surprise \"{0}\", now using NEUTRAL (default)", name);
                        getLogger().log(Level.WARNING, "Valid lucklevel types: LUCKY, NEUTRAL, UNLUCKY, PANDORA");
                    }
                }

                if (cfg.getValue("custom." + name + ".commands") != null && !cfg.getStringList("custom." + name + ".commands").isEmpty()) {
                    commands.addAll(cfg.getStringList("custom." + name + ".commands"));
                }

                if (cfg.getValue("custom." + name + ".items") != null && !cfg.getKeys("custom." + name + ".items").isEmpty()) {
                    for (String itemID : cfg.getKeys("custom." + name + ".items")) {
                        ItemStack item = null;
                        String itemPath = "custom." + name + ".items." + itemID;

                        if (cfg.getString(itemPath + ".slimefun_item") != null) {
                            String id = cfg.getString(itemPath + ".slimefun_item").toUpperCase(Locale.ROOT);
                            SlimefunItem sfItem = SlimefunItem.getById(id);

                            if (sfItem != null) {
                                item = sfItem.getItem();

                                if (cfg.getInt(itemPath + ".amount") > 1) {
                                    item.setAmount(cfg.getInt(itemPath + ".amount"));
                                }
                            } else {
                                getLogger().log(Level.WARNING, "Could not load SlimefunItem \"{0}\" to custom surprise \"{1}\"", new Object[] { id, name });
                            }
                        } else if (cfg.getString(itemPath + ".type") != null && Material.getMaterial(cfg.getString(itemPath + ".type")) != null) {
                            item = new ItemStack(Material.getMaterial(cfg.getString(itemPath + ".type")));
                            ItemMeta itemMeta = item.getItemMeta();

                            if (cfg.getInt(itemPath + ".amount") > 1) {
                                item.setAmount(cfg.getInt(itemPath + ".amount"));
                            }

                            if (cfg.getString(itemPath + ".displayname") != null) {
                                itemMeta.setDisplayName(ChatColors.color(cfg.getString(itemPath + ".displayname")));
                            }

                            if (!cfg.getStringList(itemPath + ".lore").isEmpty()) {
                                List<String> lore = new ArrayList<>();
                                for (String l : cfg.getStringList(itemPath + ".lore")) {
                                    lore.add(ChatColors.color(l));
                                }
                                itemMeta.setLore(lore);
                            }

                            if (!cfg.getStringList(itemPath + ".enchants").isEmpty()) {
                                for (String ench : cfg.getStringList(itemPath + ".enchants")) {
                                    String[] split = ench.split(":");
                                    String enchName = split[0];
                                    Enchantment enchantment = Enchantment.getByName(enchName.toUpperCase(Locale.ROOT));
                                    int level = 1;

                                    if (enchantment != null) {
                                        if (split.length == 2) {
                                            if (!CommonPatterns.NUMERIC.matcher(split[1]).matches()) {
                                                getLogger().log(Level.WARNING, "Could not set \"{0}\" enchant with level \"{1}\" for custom surprise \"{2}\"", new Object[] { enchName, split[1], name });
                                                continue;
                                            }

                                            level = Integer.parseInt(split[1]);
                                        }

                                        itemMeta.addEnchant(enchantment, level, true);
                                    } else {
                                        getLogger().log(Level.WARNING, "Could not set \"{0}\" enchant for custom surprise \"{1}\"", new Object[] { enchName, name });
                                    }
                                }
                            }

                            item.setItemMeta(itemMeta);
                        }

                        if (item != null) {
                            items.add(item);
                        }
                    }
                }
                if (!items.isEmpty() || !commands.isEmpty()) {
                    registerSurprise(new CustomItemSurprise(name, items, commands, luckLevel));
                }
            }
        }
    }

    public static ItemStack createPotion(Color color, PotionEffect effect, boolean lucky) {
        ItemStack potion = new ItemStack(lucky ? Material.POTION : Material.SPLASH_POTION);
        PotionMeta pm = (PotionMeta) potion.getItemMeta();
        pm.setDisplayName(ChatColors.color((lucky ? "&6Lucky" : "&cUnlucky") + " potion"));
        pm.setColor(color);
        pm.addCustomEffect(effect, false);
        potion.setItemMeta(pm);
        return potion;
    }

    public void registerSurprise(Surprise surprise) {
        if (surprise instanceof CustomItemSurprise) {
            if (cfg.getBoolean("custom." + surprise.getName() + ".enabled")) {
                surprises.add(surprise);
            }

            return;
        }

        if (cfg.contains("events." + surprise.getName())) {
            if (cfg.getBoolean("events." + surprise.getName())) {
                surprises.add(surprise);
            }
        } else {
            cfg.setValue("events." + surprise.getName(), true);
            cfg.save();
            surprises.add(surprise);
        }
    }

    public void spawnLuckyBlock(Block b) {
        BlockData data = Material.PLAYER_HEAD.createBlockData(bd -> {
            if (bd instanceof Rotatable) {
                Rotatable skull = (Rotatable) bd;

                BlockFace rotation = blockfaces[ThreadLocalRandom.current().nextInt(blockfaces.length)];
                skull.setRotation(rotation);
            }
        });

        b.setBlockData(data);
        PlayerHead.setSkin(b, PlayerSkin.fromHashCode(TEXTURE), true);
        BlockStorage.store(b, "LUCKY_BLOCK");

        if (getCfg().getBoolean("debug")) {
            getLogger().log(Level.INFO, "spawned lucky block at {0} {1} {2} - {3}", new Object[] { b.getX(), b.getY(), b.getZ(), b.getWorld().getName() });
        }
    }

    public Config getCfg() {
        return cfg;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/luckyblocks-sf/issues";
    }

}
