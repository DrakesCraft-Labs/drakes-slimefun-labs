package com.github.drakescraft_labs.gcereborn.core.services;

import java.util.logging.Level;

import javax.annotation.Nonnull;

import com.bgsoftware.wildstacker.api.WildStackerAPI;

import org.bukkit.entity.Chicken;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.integrations.wildstacker.EntityStackListener;

import lombok.Getter;
import uk.antiperson.stackmob.StackMob;

@Getter
public final class IntegrationService {

    private final GeneticChickengineering plugin;

    private final boolean stackMobEnabled;
    private final boolean wildStackerEnabled;

    private StackMob stackMobInst;

    public IntegrationService(GeneticChickengineering plugin) {
        this.plugin = plugin;

        stackMobEnabled = isEnabled("StackMob");
        wildStackerEnabled = isEnabled("WildStacker");

        if (stackMobEnabled) {
            stackMobInst = (StackMob) plugin.getServer().getPluginManager().getPlugin("StackMob");
        }

        if (wildStackerEnabled) {
            new EntityStackListener(plugin);
        }
    }

    private boolean isEnabled(@Nonnull String pluginName) {
        boolean result = plugin.getServer().getPluginManager().isPluginEnabled(pluginName);
        if (result) {
            GeneticChickengineering.log(Level.INFO,
                GeneticChickengineering.getLocalization().getString("console.load.integration", pluginName));
        }
        return result;
    }

    public void captureChicken(@Nonnull Chicken chicken) {
        try {
            if (stackMobEnabled) {
                var stackEntity = stackMobInst.getEntityManager().getStackEntity(chicken);
                if (stackEntity != null && stackEntity.getSize() > 1) {
                    stackEntity.incrementSize(-1);
                } else {
                    chicken.remove();
                }
            } else if (wildStackerEnabled) {
                var stackedEntity = WildStackerAPI.getStackedEntity(chicken);
                if (stackedEntity != null && stackedEntity.getStackAmount() > 1) {
                    stackedEntity.decreaseStackAmount(1, true);
                } else {
                    chicken.remove();
                }
            } else {
                chicken.remove();
            }
        } catch (Exception e) {
            GeneticChickengineering.log(Level.SEVERE, e, "An error has occurred while capturing chicken");
            chicken.remove();
        }
    }
}
