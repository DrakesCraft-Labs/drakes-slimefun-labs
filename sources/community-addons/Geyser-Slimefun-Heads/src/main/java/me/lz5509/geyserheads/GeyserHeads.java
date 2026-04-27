package me.lz5509.geyserheads;


import com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;
import org.bukkit.plugin.java.JavaPlugin;

import me.lz5509.geyserheads.commands.Commands;


public class GeyserHeads extends JavaPlugin {
    private static GeyserHeads instance;

    @Override
    public void onEnable() {
        DrakesLabsReleaseUpdate.schedule(this, "GeyserHeads-drake");

        instance = this;
        if (!getDataFolder().exists()) saveResource("config.yml", false);
        this.getLogger().info("********************");
        this.getLogger().info("      GeyserHeads - v" + this.getDescription().getVersion());
        this.getLogger().info("********************");
        this.getCommand("geyserheads").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {}

    public static GeyserHeads getInstance(){
        return instance;
    }
}
