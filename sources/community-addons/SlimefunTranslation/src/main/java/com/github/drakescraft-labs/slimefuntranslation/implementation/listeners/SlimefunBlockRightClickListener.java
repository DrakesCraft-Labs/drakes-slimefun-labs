package com.github.drakescraft-labs.slimefuntranslation.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.drakescraft-labs.slimefun4.api.events.PlayerRightClickEvent;

import com.github.drakescraft-labs.slimefuntranslation.SlimefunTranslation;
import com.github.drakescraft-labs.slimefuntranslation.core.users.User;

public class SlimefunBlockRightClickListener implements Listener {
    public SlimefunBlockRightClickListener(@Nonnull SlimefunTranslation plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRightClick(@Nonnull PlayerRightClickEvent e) {
        User user = SlimefunTranslation.getUserService().getUser(e.getPlayer());
        var sfBlock = e.getSlimefunBlock();
        user.setRecentClickedBlock(sfBlock.orElse(null));
    }
}
