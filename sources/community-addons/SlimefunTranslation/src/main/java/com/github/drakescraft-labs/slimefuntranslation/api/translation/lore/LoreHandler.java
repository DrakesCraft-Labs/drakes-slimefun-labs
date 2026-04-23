package com.github.drakescraft-labs.slimefuntranslation.api.translation.lore;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.github.drakescraft-labs.slimefuntranslation.core.users.User;

public interface LoreHandler {
    @Nullable
    @ParametersAreNonnullByDefault
    String getLore(User user, String id, String[] args);
}
