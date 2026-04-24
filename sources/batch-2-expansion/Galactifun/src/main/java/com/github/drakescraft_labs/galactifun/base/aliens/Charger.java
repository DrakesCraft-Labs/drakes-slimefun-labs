package com.github.drakescraft_labs.galactifun.base.aliens;

import javax.annotation.Nonnull;

import org.bukkit.entity.Goat;

import com.github.drakescraft_labs.galactifun.api.aliens.Alien;

public final class Charger extends Alien<Goat> {

    public Charger() {
        super(Goat.class, "CHARGER", "Charger", 30, 5);
    }

    @Override
    public void onSpawn(@Nonnull Goat spawned) {
        spawned.setScreaming(true);
    }

}
