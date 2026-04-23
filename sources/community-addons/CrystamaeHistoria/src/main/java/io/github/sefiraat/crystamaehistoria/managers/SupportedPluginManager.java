package io.github.sefiraat.crystamaehistoria.managers;

import io.github.sefiraat.crystamaehistoria.CrystamaeHistoria;
import com.github.drakescraft-labs.exoticgarden.items.BonemealableItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Getter
public class SupportedPluginManager {

    private static final NamespacedKey IGNORE_DAMAGE_KEY = new NamespacedKey(Slimefun.instance(), "ignore_damage");
    private static SupportedPluginManager instance;

    private boolean mcMMO;
    private boolean exoticGarden;
    private boolean slimeTinker;
    private boolean headLimiter;
    private boolean networks;
    private boolean wildStacker;
    private boolean roseStacker;
    private boolean netheopoiesis;

    private Object roseStackerAPI;

    public SupportedPluginManager() {
        instance = this;
        Bukkit.getScheduler().runTaskLater(CrystamaeHistoria.instance(), this::postSetup, 1);
        this.netheopoiesis = Bukkit.getPluginManager().isPluginEnabled("Netheopoiesis");
    }

    private void postSetup() {
        this.mcMMO = Bukkit.getPluginManager().isPluginEnabled("mcMMO");
        this.exoticGarden = Bukkit.getPluginManager().isPluginEnabled("ExoticGarden");
        this.slimeTinker = Bukkit.getPluginManager().isPluginEnabled("SlimeTinker");
        this.headLimiter = Bukkit.getPluginManager().isPluginEnabled("HeadLimiter");
        this.networks = Bukkit.getPluginManager().isPluginEnabled("Networks");
        this.wildStacker = Bukkit.getPluginManager().isPluginEnabled("WildStacker");
        this.roseStacker = Bukkit.getPluginManager().isPluginEnabled("RoseStacker");
        if (this.roseStacker) {
            try {
                Class<?> apiClass = Class.forName("dev.rosewood.rosestacker.api.RoseStackerAPI");
                this.roseStackerAPI = apiClass.getMethod("getInstance").invoke(null);
            } catch (Exception e) {
                this.roseStacker = false;
            }
        }
    }

    /**
     * Damaging an entity and attributing to a player will make mcMMO give exp based
     * on the held item. If mcMMO is installed, we need to flag the entity to be ignored
     * briefly.
     *
     * @param livingEntity The {@link LivingEntity} to be damaged
     * @param player       The {@link Player} to attribute the damage/drops to
     * @param damage       The damage to apply
     */
    @ParametersAreNonnullByDefault
    public void playerDamageWithoutAttribution(LivingEntity livingEntity, Player player, double damage) {
        markIgnoreDamage(livingEntity);
        livingEntity.damage(damage, player);
        clearIgnoreDamageMarker(livingEntity);
    }

    @ParametersAreNonnullByDefault
    public void markIgnoreDamage(LivingEntity livingEntity) {
        if (mcMMO) {
            try {
                Class<?> combatUtils = Class.forName("com.gmail.nossr50.util.skills.CombatUtils");
                Method method = combatUtils.getMethod("applyIgnoreDamageMetadata", LivingEntity.class);
                method.invoke(null, livingEntity);
            } catch (Exception ignored) {
            }
        }
        if (slimeTinker) {
            PersistentDataAPI.setBoolean(livingEntity, IGNORE_DAMAGE_KEY, true);
        }
    }

    @ParametersAreNonnullByDefault
    public void clearIgnoreDamageMarker(LivingEntity livingEntity) {
        if (mcMMO) {
            try {
                Class<?> combatUtils = Class.forName("com.gmail.nossr50.util.skills.CombatUtils");
                Method method = combatUtils.getMethod("removeIgnoreDamageMetadata", LivingEntity.class);
                method.invoke(null, livingEntity);
            } catch (Exception ignored) {
            }
        }
        if (slimeTinker) {
            PersistentDataAPI.remove(livingEntity, IGNORE_DAMAGE_KEY);
        }
    }

    @ParametersAreNonnullByDefault
    public boolean isExoticGardenPlant(Block block) {
        return exoticGarden
            && BlockStorage.hasBlockInfo(block)
            && BlockStorage.check(block) instanceof BonemealableItem;
    }

    /**
     * Gets the SlimefunItem for the ExoticPlant if it exists
     *
     * @param block The {@link Block} to check
     * @return Returns null if there is not a plant (or Exotic is not installed) or the
     * the SlimefunItem if applicable.
     */
    @Nullable
    @ParametersAreNonnullByDefault
    public SlimefunItem getExoticGardenPlant(Block block) {
        if (exoticGarden && BlockStorage.hasBlockInfo(block)) {
            SlimefunItem slimefunItem = BlockStorage.check(block);
            if (slimefunItem instanceof BonemealableItem) {
                return slimefunItem;
            }
        }
        return null;
    }

    public int getStackAmount(Item item) {
        if (wildStacker) {
            try {
                Class<?> apiClass = Class.forName("com.bgsoftware.wildstacker.api.WildStackerAPI");
                Method method = apiClass.getMethod("getItemAmount", Item.class);
                return (int) method.invoke(null, item);
            } catch (Exception e) {
                return item.getItemStack().getAmount();
            }
        } else if (roseStacker && roseStackerAPI != null) {
            try {
                Method getStackedItem = roseStackerAPI.getClass().getMethod("getStackedItem", Item.class);
                Object stackedItem = getStackedItem.invoke(roseStackerAPI, item);
                if (stackedItem != null) {
                    Method getStackSize = stackedItem.getClass().getMethod("getStackSize");
                    return (int) getStackSize.invoke(stackedItem);
                }
            } catch (Exception ignored) {
            }
            return item.getItemStack().getAmount();
        } else {
            return item.getItemStack().getAmount();
        }
    }

    public void setStackAmount(Item item, int amount) {
        if (wildStacker) {
            try {
                Class<?> apiClass = Class.forName("com.bgsoftware.wildstacker.api.WildStackerAPI");
                Method method = apiClass.getMethod("getStackedItem", Item.class);
                Object stackedItem = method.invoke(null, item);
                if (stackedItem != null) {
                    Method setStackAmount = stackedItem.getClass().getMethod("setStackAmount", int.class, boolean.class);
                    setStackAmount.invoke(stackedItem, amount, true);
                }
            } catch (Exception ignored) {
            }
        } else if (roseStacker && roseStackerAPI != null) {
            try {
                Method getStackedItem = roseStackerAPI.getClass().getMethod("getStackedItem", Item.class);
                Object stackedItem = getStackedItem.invoke(roseStackerAPI, item);
                if (stackedItem != null) {
                    Method setStackSize = stackedItem.getClass().getMethod("setStackSize", int.class);
                    setStackSize.invoke(stackedItem, amount);
                }
            } catch (Exception ignored) {
            }
        } else {
            item.getItemStack().setAmount(amount);
        }
    }

    public static boolean isMcMMO() {
        return instance.mcMMO;
    }

    public static boolean isExoticGarden() {
        return instance.exoticGarden;
    }

    public static boolean isSlimeTinker() {
        return instance.slimeTinker;
    }

    public static boolean isHeadLimiter() {
        return instance.headLimiter;
    }

    public static boolean isNetworks() {
        return instance.networks;
    }

    public static boolean isWildStacker() {
        return instance.wildStacker;
    }

    public static boolean isRoseStacker() {
        return instance.roseStacker;
    }

    public boolean isNetheopoiesis() {
        return netheopoiesis;
    }
}
