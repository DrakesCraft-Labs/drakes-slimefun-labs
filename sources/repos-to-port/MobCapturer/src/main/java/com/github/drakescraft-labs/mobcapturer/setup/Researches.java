package com.github.drakescraft-labs.mobcapturer.setup;

import org.bukkit.NamespacedKey;

import com.github.drakescraft-labs.mobcapturer.MobCapturer;
import com.github.drakescraft-labs.slimefun4.api.researches.Research;

import lombok.experimental.UtilityClass;

/**
 * All the {@link Research}es in MobCapturer.
 *
 * @author TheBusyBiscuit
 * @author ybw0014
 */
@UtilityClass
public final class Researches {

    public static final Research MOB_CAPTURING = new Research(new NamespacedKey(MobCapturer.getInstance(), "mob_capturing"), 32652, "Capturing Mobs", 28);
}
