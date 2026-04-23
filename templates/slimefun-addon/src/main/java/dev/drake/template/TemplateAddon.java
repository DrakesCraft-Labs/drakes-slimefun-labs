package dev.drake.template;

import java.io.File;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import dev.drake.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import dev.drake.template.items.TemplateMachine;

public class TemplateAddon extends JavaPlugin implements SlimefunAddon {

    private ItemGroup itemGroup;

    @Override
    public void onEnable() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        new Metrics(this, 0);

        itemGroup = new ItemGroup(
            new NamespacedKey(this, "template_addon"),
            new CustomItemStack(Material.BEACON, "&bTemplate Addon")
        );

        TemplateMachine machine = new TemplateMachine(
            itemGroup,
            new Research(new NamespacedKey(this, "template_research"), 9100, "Template Research", 5)
        );

        machine.register(this);

        Slimefun.logger().info(() -> getName() + " enabled on Drake Framework.");
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/drakescraft_labs/drakes-slimefun-labs/issues";
    }
}
