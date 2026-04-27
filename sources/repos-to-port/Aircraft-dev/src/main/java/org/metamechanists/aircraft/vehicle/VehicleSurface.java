package org.metamechanists.aircraft.vehicle;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.metamechanists.aircraft.vehicle.forces.SpatialForce;
import org.metamechanists.aircraft.vehicle.forces.SpatialForceType;


public class VehicleSurface {
    private final double liftCoefficient;
    private final double dragCoefficient;
    private final double area;
    private final Vector3d normal;
    private final Vector3d location;

    public VehicleSurface(double dragCoefficient, double liftCoefficient, double area,
                          Vector3d normal, Vector3d location) {
        this.dragCoefficient = dragCoefficient;
        this.liftCoefficient = liftCoefficient;
        this.area = area;
        this.normal = normal;
        this.location = location;
    }

    private double getRelativeArea(@NotNull Vector3d normal, @NotNull Vector3d airflowVelocity) {
        return new Vector3d(normal).angleCos(airflowVelocity) * area;
    }

    public SpatialForce getLiftForce(double airDensity, Vector3d velocity, Vector3d angularVelocity) {
        Vector3d angularVelocityVector = new Vector3d(angularVelocity).cross(location);
        Vector3d airflowVelocity = new Vector3d(velocity).add(angularVelocityVector).mul(-1);

        // Check the airflow isn't coming *out* of the surface as opposed to going into it
        // Also check that 1) airflow is not zero 2) airflow and normal are not in opposite directions - these cause NaN values
        if (velocity.length() < 0.000001 || normal.angle(airflowVelocity) < Math.PI / 2 || normal.angle(airflowVelocity) > (Math.PI - 0.001)) {
            return new SpatialForce(SpatialForceType.LIFT, new Vector3d(), location);
        }

        // L = 0.5 * Cl * ρ * A * V^2,
        // L = lift force
        // Cl = coefficient of lift
        // ρ = air density
        // A = surface area facing airflow
        // V = aircraft velocity
        double aircraftSpeed = velocity.length();
        Vector3d perpendicularDirection = new Vector3d(normal).cross(airflowVelocity);
        Vector3d liftDirection = new Vector3d(perpendicularDirection).cross(airflowVelocity).normalize();
        Vector3d force = liftDirection.mul(
                Math.sin(2.0*normal.angle(airflowVelocity))
                        * 0.5
                        * liftCoefficient
                        * airDensity
                        * getRelativeArea(normal, airflowVelocity)
                        * (aircraftSpeed * aircraftSpeed));
        return new SpatialForce(SpatialForceType.LIFT, force, location);
    }

    public SpatialForce getDragForce(double airDensity, Vector3d velocity, Vector3d angularVelocity) {
        Vector3d angularVelocityVector = new Vector3d(angularVelocity).cross(location);
        Vector3d airflowVelocity = new Vector3d(velocity).add(angularVelocityVector).mul(-1);

        // Check the airflow isn't coming *out* of the surface as opposed to going into it
        // Also check that 1) airflow is not zero 2) airflow and normal are not in opposite directions - these cause NaN values
        if (velocity.length() < 0.000001 || normal.angle(airflowVelocity) < Math.PI / 2 || normal.angle(airflowVelocity) < 0.001) {
            return new SpatialForce(SpatialForceType.DRAG, new Vector3d(), location);
        }

        // D = 0.5 * Cd * ρ * A * V^2, where
        // D = drag force
        // Cd = coefficient of drag
        // ρ = air density
        // A = surface area facing airflow
        // V = aircraft velocity
        double aircraftSpeed = velocity.length();
        Vector3d dragDirection = new Vector3d(airflowVelocity).mul(-1).normalize();
        Vector3d force = dragDirection.mul(
                Math.sin(normal.angle(airflowVelocity))
                        * 0.5
                        * dragCoefficient
                        * airDensity
                        * getRelativeArea(normal, airflowVelocity)
                        * (aircraftSpeed * aircraftSpeed));
        return new SpatialForce(SpatialForceType.DRAG, force, location);
    }
}
