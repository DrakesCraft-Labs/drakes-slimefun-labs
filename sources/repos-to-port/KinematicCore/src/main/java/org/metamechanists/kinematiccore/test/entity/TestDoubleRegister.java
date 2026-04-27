package org.metamechanists.kinematiccore.test.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.KinematicCore;
import org.metamechanists.kinematiccore.api.Exceptions;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.kinematiccore.test.BaseTest;

import static org.assertj.core.api.Assertions.*;


public class TestDoubleRegister implements BaseTest {
    @SuppressWarnings("unused")
    private static final class TestEntity extends KinematicEntity<Pig, KinematicEntitySchema> {
        private static final KinematicEntitySchema SCHEMA = new KinematicEntitySchema(
                "test_double_register",
                EntityType.PIG,
                TestEntity.class
        );

        private TestEntity(@NotNull Location location) {
            super(SCHEMA, () -> location.getWorld().spawn(location, Pig.class));
        }

        public TestEntity(@NotNull StateReader reader, @NotNull Pig pig) {
            super(reader, pig);
        }
    }

    @Override
    public void test(World world) {
        TestEntity.SCHEMA.register(KinematicCore.instance());

        TestUtil.runSync(() -> {
            if (KinematicEntitySchema.get(TestEntity.SCHEMA.id()) == null) {
                TestEntity.SCHEMA.register(KinematicCore.instance());
            }

            assertThatThrownBy(() -> TestEntity.SCHEMA.register(KinematicCore.instance()))
                    .isInstanceOf(Exceptions.IdConflictException.class);
        });
    }

    @Override
    public void cleanup() {
        TestDoubleRegister.TestEntity.SCHEMA.unregister();
    }
}
