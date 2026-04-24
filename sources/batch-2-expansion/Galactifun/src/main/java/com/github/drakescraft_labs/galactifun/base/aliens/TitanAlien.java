package com.github.drakescraft_labs.galactifun.base.aliens;

import javax.annotation.Nonnull;

import org.bukkit.entity.Illusioner;
import org.bukkit.event.entity.EntityDeathEvent;

import com.github.drakescraft_labs.galactifun.api.aliens.Alien;

/**
 * Class for the Titan, an alien of Titan
 *
 * @author Seggan
 */
public final class TitanAlien extends Alien<Illusioner> {

    public TitanAlien(String id, String name, double maxHealth, int spawnChance) {
        super(Illusioner.class, id, name, maxHealth, spawnChance);
    }

    @Override
    public void onDeath(@Nonnull EntityDeathEvent e) {
        e.getDrops().clear();
    }

}
