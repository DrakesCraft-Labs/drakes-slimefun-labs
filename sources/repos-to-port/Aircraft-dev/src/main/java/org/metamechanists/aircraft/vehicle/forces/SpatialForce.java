package org.metamechanists.aircraft.vehicle.forces;

import org.joml.Vector3d;


public record SpatialForce(SpatialForceType type, Vector3d force, Vector3d location) {
    public Vector3d getTorqueVector() {
        return new Vector3d(location).cross(force);
    }
}
