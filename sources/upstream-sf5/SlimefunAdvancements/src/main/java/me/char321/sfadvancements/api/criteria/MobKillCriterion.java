package me.char321.sfadvancements.api.criteria;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

/**
 * This criterion is performed when a player kills a mob.
 *
 * @author char321
 */
public class MobKillCriterion extends Criterion {
    private final EntityType entity;

    @SuppressWarnings("deprecation")
    public static MobKillCriterion loadFromConfig(ConfigurationSection config) {
        String id = config.getName();

        int amount = config.getInt("amount");
        if (amount == 0) {
            amount = 1;
        }

        String name = config.getString("name");
        if (name == null) {
            name = id;
        }

        name = ChatColor.translateAlternateColorCodes('&', name);

        String entity = config.getString("entity");
        if (entity == null) {
            SFAdvancements.warn("entity not provided for " + id);
            return null;
        }
        EntityType entityType = Registry.ENTITY_TYPE.match(entity);
        if (entityType == null) {
            SFAdvancements.warn("invalid entity type " + entity + " for criterion " + id);
            return null;
        }

        return new MobKillCriterion(id, amount, name, entityType);
    }

    public MobKillCriterion(String id, int amount, String name, EntityType entity) {
        super(id, amount, name);
        this.entity = entity;
    }

    public EntityType getEntity() {
        return entity;
    }
}
