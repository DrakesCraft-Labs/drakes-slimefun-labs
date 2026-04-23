package dev.j3fftw.headlimiter.blocklimiter;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.base.Objects;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;

public class Group {

    private final String groupName;
    private final int defaultAmount;
    private final HashSet<String> items;
    private final HashMap<String, Integer> permissionAmounts;

    public Group(ConfigurationSection configurationSection) {
        this.groupName = configurationSection.getName();
        this.defaultAmount = configurationSection.getInt("items-amount", 0);
        this.items = new HashSet<>(configurationSection.getStringList("items"));
        this.permissionAmounts = new HashMap<>();

        ConfigurationSection permissionSection = configurationSection.getConfigurationSection("permission-amount");

        if (permissionSection != null) {
            for (String key : permissionSection.getKeys(false)) {
                permissionAmounts.put(key, permissionSection.getInt(key, 0));
            }
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public Set<String> getItems() {
        return items;
    }

    public Map<String, Integer> getPermissionAmounts() {
        return permissionAmounts;
    }

    public boolean contains(@Nonnull SlimefunItem slimefunItem) {
        return contains(slimefunItem.getId());
    }

    public boolean contains(@Nonnull String itemId) {
        return this.items.contains(itemId);
    }

    public int getPermissibleAmount(@Nonnull Player player) {
        int allowable = defaultAmount;
        if (!this.permissionAmounts.isEmpty()) {
            for (Map.Entry<String, Integer> entry : this.permissionAmounts.entrySet()) {
                String permission = entry.getKey();
                if (player.hasPermission(permission)) {
                    allowable = Math.max(entry.getValue(), allowable);
                }
            }
        }
        return allowable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        return defaultAmount == group.defaultAmount && Objects.equal(
            groupName,
            group.groupName
        ) && Objects.equal(items, group.items) && Objects.equal(
            permissionAmounts,
            group.permissionAmounts
        );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(groupName, defaultAmount, items, permissionAmounts);
    }
}
