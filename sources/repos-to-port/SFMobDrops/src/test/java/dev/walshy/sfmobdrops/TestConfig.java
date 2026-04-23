package dev.walshy.sfmobdrops;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import dev.walshy.sfmobdrops.drops.MobDrop;
import dev.walshy.sfmobdrops.drops.Drop;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

public class TestConfig {

    private static SfMobDrops instance;

    @BeforeAll
    public static void setup() {
        MockBukkit.mock();

        MockBukkit.load(Slimefun.class);
        instance = MockBukkit.load(SfMobDrops.class);
    }

    @Test
    public void testLoadConfig_simpleOneItem() throws Exception {
        writeConfig("""
        drops:
          - entity: ZOMBIE
            drops:
              - slimefunItem: MAGIC_LUMP_1
                chance: 100
        """);

        Assertions.assertEquals(1, instance.getMobDrops().size());
        
        MobDrop mobDrop = instance.getMobDrops().iterator().next();

        Assertions.assertEquals(EntityType.ZOMBIE, mobDrop.getDropsFrom());
        Assertions.assertEquals(1, mobDrop.getDrops().size());
        
        Drop drop = mobDrop.getDrops().iterator().next();
        Assertions.assertEquals("MAGIC_LUMP_1", drop.getSlimefunItem());
        Assertions.assertEquals(100, drop.getChance());
        Assertions.assertEquals(1, drop.getAmount());
    }

    @Test
    public void testLoadConfig_simpleMultiItems() throws Exception {
        writeConfig("""
        drops:
          - entity: ZOMBIE
            drops:
              - slimefunItem: MAGIC_LUMP_1
                chance: 10
              - slimefunItem: MAGIC_LUMP_2
                chance: 5
              - slimefunItem: MAGIC_LUMP_3
                chance: 1
        """);

        Assertions.assertEquals(1, instance.getMobDrops().size());
        
        MobDrop mobDrop = instance.getMobDrops().iterator().next();

        Assertions.assertEquals(EntityType.ZOMBIE, mobDrop.getDropsFrom());
        Assertions.assertEquals(3, mobDrop.getDrops().size());

        for (Drop drop : mobDrop.getDrops()) {
            switch (drop.getSlimefunItem()) {
                case "MAGIC_LUMP_1":
                    Assertions.assertEquals(10, drop.getChance());
                    break;
                case "MAGIC_LUMP_2":
                    Assertions.assertEquals(5, drop.getChance());
                    break;
                case "MAGIC_LUMP_3":
                    Assertions.assertEquals(1, drop.getChance());
                    break;
                default:
                    Assertions.fail("Unknown item: " + drop.getSlimefunItem());
            }
        }
    }

    @Test
    public void testLoadConfig_complexOneItem() throws Exception {
        writeConfig("""
        drops:
          - entity: ENDER_DRAGON
            slimefunItem: ANCIENT_RUNE_SOULBOUND
            chance: 0.5
            name: '&cAmazing Dragon'
            nbtTag: 'plugin_name:awesome_mob'
            amount: 6
        """);

        Assertions.assertEquals(1, instance.getMobDrops().size());

        MobDrop mobDrop = instance.getMobDrops().iterator().next();

        Assertions.assertEquals(EntityType.ENDER_DRAGON, mobDrop.getDropsFrom());
        Assertions.assertEquals(ChatColor.RED + "Amazing Dragon", mobDrop.getEntityName());
        Assertions.assertEquals("plugin_name:awesome_mob", mobDrop.getEntityNbtTag().toString());
        Assertions.assertEquals(1, mobDrop.getDrops().size());

        Drop drop = mobDrop.getDrops().iterator().next();
        Assertions.assertEquals("ANCIENT_RUNE_SOULBOUND", drop.getSlimefunItem());
        Assertions.assertEquals(0.5, drop.getChance());
        Assertions.assertEquals(6, drop.getAmount());
    }

    @Test
    public void testLoadConfig_complexMultiItems() throws Exception {
        writeConfig("""
        drops:
          - entity: ENDER_DRAGON
            name: '&cAmazing Dragon'
            nbtTag: 'plugin_name:awesome_mob'
            drops:
              - slimefunItem: MAGIC_LUMP_1
                chance: 10
                amount: 3
              - slimefunItem: MAGIC_LUMP_2
                chance: 5
                amount: 2
              - slimefunItem: MAGIC_LUMP_3
                chance: 1
                amount: 1
        """);

        Assertions.assertEquals(1, instance.getMobDrops().size());

        MobDrop mobDrop = instance.getMobDrops().iterator().next();

        Assertions.assertEquals(EntityType.ENDER_DRAGON, mobDrop.getDropsFrom());
        Assertions.assertEquals(ChatColor.RED + "Amazing Dragon", mobDrop.getEntityName());
        Assertions.assertEquals("plugin_name:awesome_mob", mobDrop.getEntityNbtTag().toString());
        Assertions.assertEquals(3, mobDrop.getDrops().size());

        for (Drop drop : mobDrop.getDrops()) {
            switch (drop.getSlimefunItem()) {
                case "MAGIC_LUMP_1":
                    Assertions.assertEquals(10, drop.getChance());
                    break;
                case "MAGIC_LUMP_2":
                    Assertions.assertEquals(5, drop.getChance());
                    break;
                case "MAGIC_LUMP_3":
                    Assertions.assertEquals(1, drop.getChance());
                    break;
                default:
                    Assertions.fail("Unknown item: " + drop.getSlimefunItem());
            }
        }
    }

    @Test
    public void testLoadConfig_allEntities() throws Exception {
        writeConfig("""
        drops:
          - entity: ALL
            slimefunItem: MAGIC_LUMP_2
            chance: 50
        """);

        Assertions.assertEquals(1, instance.getMobDrops().size());

        MobDrop mobDrop = instance.getMobDrops().iterator().next();

        Assertions.assertNull(mobDrop.getDropsFrom());
        Assertions.assertTrue(mobDrop.isAllMobs());
        Assertions.assertEquals(1, mobDrop.getDrops().size());

        Drop drop = mobDrop.getDrops().iterator().next();
        Assertions.assertEquals("MAGIC_LUMP_2", drop.getSlimefunItem());
        Assertions.assertEquals(50, drop.getChance());
        Assertions.assertEquals(1, drop.getAmount());
    }

    // -- Legacy -- //
    @Test
    public void testLoadConfig_legacySimpleItem() throws Exception {
        writeConfig("""
        drops:
          - entity: ZOMBIE
            slimefunItem: MAGIC_LUMP_1
            chance: 100
        """);

        Assertions.assertEquals(1, instance.getMobDrops().size());
        
        MobDrop mobDrop = instance.getMobDrops().iterator().next();

        Assertions.assertEquals(EntityType.ZOMBIE, mobDrop.getDropsFrom());
        Assertions.assertEquals(1, mobDrop.getDrops().size());
        
        Drop drop = mobDrop.getDrops().iterator().next();
        Assertions.assertEquals("MAGIC_LUMP_1", drop.getSlimefunItem());
        Assertions.assertEquals(100, drop.getChance());
        Assertions.assertEquals(1, drop.getAmount());
    }

    @Test
    public void testLoadConfig_legacyComplexItem() throws Exception {
        writeConfig("""
        drops:
          - entity: ENDER_DRAGON
            slimefunItem: ANCIENT_RUNE_SOULBOUND
            chance: 0.5
            name: '&cAmazing Dragon'
            nbtTag: 'plugin_name:awesome_mob'
            amount: 6
        """);

        Assertions.assertEquals(1, instance.getMobDrops().size());

        MobDrop mobDrop = instance.getMobDrops().iterator().next();

        Assertions.assertEquals(EntityType.ENDER_DRAGON, mobDrop.getDropsFrom());
        Assertions.assertEquals(ChatColor.RED + "Amazing Dragon", mobDrop.getEntityName());
        Assertions.assertEquals("plugin_name:awesome_mob", mobDrop.getEntityNbtTag().toString());
        Assertions.assertEquals(1, mobDrop.getDrops().size());

        Drop drop = mobDrop.getDrops().iterator().next();
        Assertions.assertEquals("ANCIENT_RUNE_SOULBOUND", drop.getSlimefunItem());
        Assertions.assertEquals(0.5, drop.getChance());
        Assertions.assertEquals(6, drop.getAmount());
    }

    @Test
    public void testLoadConfig_legacyAllEntities() throws Exception {
        writeConfig("""
        drops:
          - entity: ALL
            slimefunItem: MAGIC_LUMP_2
            chance: 50
        """);

        Assertions.assertEquals(1, instance.getMobDrops().size());

        MobDrop mobDrop = instance.getMobDrops().iterator().next();

        Assertions.assertNull(mobDrop.getDropsFrom());
        Assertions.assertTrue(mobDrop.isAllMobs());
        Assertions.assertEquals(1, mobDrop.getDrops().size());

        Drop drop = mobDrop.getDrops().iterator().next();
        Assertions.assertEquals("MAGIC_LUMP_2", drop.getSlimefunItem());
        Assertions.assertEquals(50, drop.getChance());
        Assertions.assertEquals(1, drop.getAmount());
    }

    // TODO: Validation tests

    private void writeConfig(String str) throws Exception {
        instance.getConfig().loadFromString(str);
        instance.loadDrops();
    }
}
