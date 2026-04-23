package com.github.drakescraft-labs.slimefun4.test.mocks;

import java.util.HashMap;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.OfflinePlayer;

import com.github.drakescraft-labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft-labs.slimefun4.storage.data.PlayerData;

public class MockProfile extends PlayerProfile {

    public MockProfile(@Nonnull OfflinePlayer p) {
        this(p, new PlayerData(Set.of(), new HashMap<>(), Set.of()));
    }

    public MockProfile(@Nonnull OfflinePlayer p, @Nonnull PlayerData data) {
        super(p, data);
    }
}
