package com.github.drakescraft_labs.slimefun4.implementation.items.magical.runes;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;

import dev.drake.dough.items.ItemUtils;
import dev.drake.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.EntityInteractHandler;
import com.github.drakescraft_labs.slimefun4.core.services.sounds.SoundEffect;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.utils.compatibility.VersionedParticle;

/**
 * This {@link SlimefunItem} allows you to reset a {@link Villager} profession.
 * Useful to reset a villager who does not have desirable trades.
 *
 * @author dNiym
 *
 */
public class VillagerRune extends SimpleSlimefunItem<EntityInteractHandler> {

    @ParametersAreNonnullByDefault
    public VillagerRune(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public @Nonnull EntityInteractHandler getItemHandler() {
        return (e, item, offhand) -> {
            if (e.isCancelled() || !Slimefun.getProtectionManager().hasPermission(e.getPlayer(), e.getRightClicked().getLocation(), Interaction.INTERACT_ENTITY)) {
                // They don't have permission to use it in this area
                return;
            }

            if (e.getRightClicked() instanceof Villager villager) {
                if (villager.getProfession() == Profession.NONE || villager.getProfession() == Profession.NITWIT) {
                    return;
                }

                if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    ItemUtils.consumeItem(item, false);
                }

                // Reset Villager
                villager.setVillagerExperience(0);
                villager.setVillagerLevel(1);
                villager.setProfession(Profession.NONE);
                e.setCancelled(true);

                double offset = ThreadLocalRandom.current().nextDouble(0.5);

                SoundEffect.VILLAGER_RUNE_TRANSFORM_SOUND.playAt(villager.getLocation(), SoundCategory.NEUTRAL);
                villager.getWorld().spawnParticle(Particle.CRIMSON_SPORE, villager.getLocation(), 10, 0, offset / 2, 0, 0);
                villager.getWorld().spawnParticle(VersionedParticle.ENCHANT, villager.getLocation(), 5, 0.04, 1, 0.04);
            }
        };
    }
}
