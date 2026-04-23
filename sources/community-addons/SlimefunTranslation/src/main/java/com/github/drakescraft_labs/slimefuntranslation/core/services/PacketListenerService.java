package com.github.drakescraft_labs.slimefuntranslation.core.services;

import java.util.ArrayList;
import java.util.List;

import com.github.drakescraft_labs.slimefuntranslation.implementation.packetlisteners.AListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.packetlisteners.client.items.SetCreativeSlotListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.packetlisteners.client.items.WindowClickListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.packetlisteners.server.EntityMetadataListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.packetlisteners.server.OpenWindowListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.packetlisteners.server.items.SetSlotListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.packetlisteners.server.items.WindowItemListener;

public final class PacketListenerService {

    public PacketListenerService() {
        List<AListener> packetListeners = new ArrayList<>();

        packetListeners.add(new SetCreativeSlotListener());
        packetListeners.add(new WindowClickListener());
        packetListeners.add(new SetSlotListener());
        packetListeners.add(new WindowItemListener());
        packetListeners.add(new EntityMetadataListener());
        packetListeners.add(new OpenWindowListener());

        for (var listener : packetListeners) {
            listener.register();
        }
    }
}
