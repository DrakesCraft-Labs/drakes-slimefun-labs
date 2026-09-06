/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile
 *  com.github.drakescraft_labs.slimefun4.api.researches.Research
 *  javax.annotation.ParametersAreNonnullByDefault
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package me.poma123.globalwarming.tasks;

import com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft_labs.slimefun4.api.researches.Research;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.ParametersAreNonnullByDefault;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.api.Temperature;
import me.poma123.globalwarming.tasks.MechanicTask;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SlownessTask
extends MechanicTask {
    private final ThreadLocalRandom rnd = ThreadLocalRandom.current();
    private final double chance;
    private final Research neededResearch;

    @ParametersAreNonnullByDefault
    public SlownessTask(double chance) {
        this.chance = chance;
        this.neededResearch = GlobalWarmingPlugin.getRegistry().getResearchNeededForPlayerMechanics();
    }

    private void applyEffect(Player p, int duration, int amplifier) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, amplifier));
    }

    @Override
    public void run() {
        Set<String> enabledWorlds = GlobalWarmingPlugin.getRegistry().getEnabledWorlds();
        for (String worldName : enabledWorlds) {
            World w = Bukkit.getWorld((String)worldName);
            if (w == null || !GlobalWarmingPlugin.getRegistry().isWorldEnabled(w.getName()) || w.getEnvironment() != World.Environment.NORMAL || w.getPlayers().isEmpty()) continue;
            for (Player p : w.getPlayers()) {
                int duration;
                int amplifier;
                double random;
                Optional profile;
                if (p.hasPotionEffect(PotionEffectType.SLOWNESS) || this.neededResearch != null && (profile = PlayerProfile.find((OfflinePlayer)p)).isPresent() && !((PlayerProfile)profile.get()).hasUnlocked(this.neededResearch) || !((random = this.rnd.nextDouble()) < this.chance)) continue;
                Temperature temp = GlobalWarmingPlugin.getTemperatureManager().getTemperatureAtLocation(p.getLocation());
                double celsiusValue = temp.getCelsiusValue();
                if (celsiusValue <= -30.0 || celsiusValue >= 50.0) {
                    amplifier = 2;
                    duration = 100;
                } else if (celsiusValue <= -20.0 || celsiusValue >= 40.0) {
                    amplifier = 1;
                    duration = 60;
                } else {
                    if (!(celsiusValue <= -10.0) && !(celsiusValue >= 36.0)) continue;
                    amplifier = 0;
                    duration = 40;
                }
                this.applyEffect(p, duration, amplifier);
            }
        }
    }
}

