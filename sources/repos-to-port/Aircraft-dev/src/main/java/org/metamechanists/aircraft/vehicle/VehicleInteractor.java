package org.metamechanists.aircraft.vehicle;

import org.bukkit.entity.Interaction;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.vehicle.gui.MainGui;
import org.metamechanists.displaymodellib.builders.InteractionBuilder;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.kinematiccore.api.state.StateWriter;

import java.util.UUID;


public class VehicleInteractor extends KinematicEntity<Interaction, KinematicEntitySchema> {
    private final UUID vehicleEntity;

    public VehicleInteractor(@NotNull VehicleEntity vehicleEntity) {
        super(vehicleEntity.schema().getInteractorSchema(), () -> {
            Pig pig = vehicleEntity.entity();
            Interaction interaction = new InteractionBuilder()
                .width(1.2F)
                .height(1.2F)
                .build(pig.getLocation());
            pig.addPassenger(interaction);
            return interaction;
        });

        this.vehicleEntity = vehicleEntity.uuid();
    }

    public VehicleInteractor(StateReader reader, Interaction interaction) {
        super(reader, interaction);
        vehicleEntity = reader.get("vehicleEntity", UUID.class);
    }

    @Override
    public void write(@NotNull StateWriter writer) {
        writer.set("vehicleEntity", vehicleEntity);
    }

    @Override
    public void onRightClick(@NotNull Player player) {
        VehicleEntity vehicleEntity = (VehicleEntity) KinematicEntity.get(this.vehicleEntity);
        assert vehicleEntity != null;
        MainGui.show(vehicleEntity, player);
    }
}
