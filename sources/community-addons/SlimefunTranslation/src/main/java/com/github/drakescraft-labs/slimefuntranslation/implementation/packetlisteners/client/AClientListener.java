package com.github.drakescraft-labs.slimefuntranslation.implementation.packetlisteners.client;

import javax.annotation.Nonnull;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import com.github.drakescraft-labs.slimefuntranslation.SlimefunTranslation;
import com.github.drakescraft-labs.slimefuntranslation.implementation.packetlisteners.AListener;

public abstract class AClientListener extends AListener {
    protected AClientListener(@Nonnull PacketType packetType) {
        super(packetType);

        adapter = new PacketAdapter(SlimefunTranslation.getInstance(), ListenerPriority.HIGHEST, packetType) {
            @Override
            public void onPacketReceiving(@Nonnull PacketEvent event) {
                process(event);
            }
        };
    }
}
