package com.github.drakescraft_labs.slimefuntranslation.implementation.packetlisteners.client.items;

import javax.annotation.Nonnull;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefuntranslation.SlimefunTranslation;
import com.github.drakescraft_labs.slimefuntranslation.implementation.packetlisteners.client.AClientListener;
import com.github.drakescraft_labs.slimefuntranslation.utils.PacketItemStackReaders;

public class SetCreativeSlotListener extends AClientListener {
    public SetCreativeSlotListener() {
        super(PacketType.Play.Client.SET_CREATIVE_SLOT);
    }

    protected void process(@Nonnull PacketEvent event) {
        var user = getUser(event);
        if (user == null) {
            return;
        }
        ItemStack item = PacketItemStackReaders.readSingletonItemModifierOrNull(event.getPacket());
        if (item == null) {
            return;
        }
        SlimefunTranslation.getTranslationService().translateItem(user, item);
    }
}
