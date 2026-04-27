package org.metamechanists.kinematiccore.test.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.api.Exceptions;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.test.BaseTest;

import static org.assertj.core.api.Assertions.*;


public class TestMissingConstructor implements BaseTest {
    private static final class TestEntity extends KinematicEntity<Pig, KinematicEntitySchema> {
        private static final KinematicEntitySchema SCHEMA = new KinematicEntitySchema(
                "test_missing_constructor",
                EntityType.PIG,
                TestEntity.class
        );

        private TestEntity(@NotNull Location location) {
            super(SCHEMA, () -> location.getWorld().spawn(location, Pig.class));
        }
    }

    @SuppressWarnings({"ResultOfObjectAllocationIgnored", "CodeBlock2Expr"})
    @Override
    public void test(World world) {
        TestUtil.runSync(() -> {
            assertThatThrownBy(() -> new KinematicEntitySchema(
                    "test_missing_constructor",
                    EntityType.PIG,
                    TestEntity.class
            )).isInstanceOf(Exceptions.EntityMissingConstructorException.class);
        });
    }

    @Override
    public void cleanup() {}
}
