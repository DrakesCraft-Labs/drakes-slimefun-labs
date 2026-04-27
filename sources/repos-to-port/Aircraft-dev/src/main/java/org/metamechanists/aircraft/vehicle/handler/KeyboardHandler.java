package org.metamechanists.aircraft.vehicle.handler;

import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.Aircraft;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;


public final class KeyboardHandler {
    private KeyboardHandler() {}

    private static void handleKey(@NotNull Player player, float rightLeft, float forwardbackwards) {
        if (player.getVehicle() == null) {
            return;
        }

        if (!(KinematicEntity.get(player.getVehicle().getUniqueId()) instanceof VehicleEntity vehicleEntity)) {
            return;
        }

        if (rightLeft < 0) {
            vehicleEntity.onSignal("d");
        } else if (rightLeft > 0) {
            vehicleEntity.onSignal("a");
        }

        if (forwardbackwards < 0) {
            vehicleEntity.onSignal("s");
        } else if (forwardbackwards > 0) {
            vehicleEntity.onSignal("w");
        }
    }

    public static void init() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        manager.addPacketListener(new PacketAdapter(
                Aircraft.getInstance(),
                ListenerPriority.NORMAL,
                Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                float rightleft = packet.getFloat().readSafely(0);
                float forwardbackwards = packet.getFloat().readSafely(1);
                handleKey(event.getPlayer(), rightleft, forwardbackwards);
            }
        });
    }
}
