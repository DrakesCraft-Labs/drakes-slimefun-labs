package me.voper.slimeframe.implementation.groups;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.libraries.commons.lang.Validate;

public class ChildGroup extends ItemGroup {

    private final MasterGroup master;

    @ParametersAreNonnullByDefault
    public ChildGroup(NamespacedKey key, MasterGroup master, ItemStack item) {
        this(key, master, item, 3);
    }

    @ParametersAreNonnullByDefault
    public ChildGroup(NamespacedKey key, MasterGroup master, ItemStack item, int tier) {
        super(key, item, tier);

        Validate.notNull(master, "The master group cannot be null");

        this.master = master;
        master.addSubGroup(this);
    }

    @Override
    public final boolean isVisible(@Nonnull Player p) {
        return false;
    }

    @Override
    public final boolean isAccessible(@Nonnull Player p) {
        return true;
    }

    public final @Nonnull MasterGroup getParent() {
        return master;
    }

    @Override
    public final void register(@Nonnull SlimefunAddon addon) {
        super.register(addon);

        if (!master.isRegistered()) {
            master.register(addon);
        }
    }

}
