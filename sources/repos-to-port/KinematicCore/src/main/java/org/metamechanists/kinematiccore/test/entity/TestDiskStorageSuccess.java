package org.metamechanists.kinematiccore.test.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.KinematicCore;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.kinematiccore.test.BaseTest;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.*;


public class TestDiskStorageSuccess implements BaseTest {
    @SuppressWarnings("unused")
    private static final class TestEntity extends KinematicEntity<Pig, KinematicEntitySchema> {
        private static final KinematicEntitySchema SCHEMA = new KinematicEntitySchema(
                "test_disk_storage_success",
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

        Location location = TestUtil.findUnloadedChunk(world);

        TestUtil.loadChunk(location);

        AtomicReference<UUID> uuid = new AtomicReference<>();
        TestUtil.runSync(() -> uuid.set(new TestEntity(location).uuid()));

        TestUtil.unloadChunk(location);

        TestUtil.runSync(() -> {
            assertThat(KinematicEntity.get(uuid.get()))
                    .isNull();
            assertThat(KinematicEntitySchema.get(TestEntity.SCHEMA.id()))
                    .isEqualTo(TestEntity.SCHEMA);
            assertThat(KinematicEntity.loadedById(TestEntity.SCHEMA))
                    .isEmpty();
        });

        TestUtil.loadChunk(location);

        TestUtil.runSync(() -> {
            assertThat(KinematicEntitySchema.get(TestEntity.SCHEMA.id()))
                    .isEqualTo(TestEntity.SCHEMA);
            assertThat(KinematicEntity.loadedById(TestEntity.SCHEMA))
                    .hasSize(1)
                    .contains(uuid.get());
        });

        TestUtil.runSync(() -> {
            //noinspection DataFlowIssue
            KinematicEntity.get(uuid.get()).entity().remove();
        });

        TestUtil.unloadChunk(location);

        TestUtil.loadChunk(location);

        TestUtil.runSync(() -> {
            assertThat(KinematicEntity.get(uuid.get()))
                    .isNull();
            assertThat(KinematicEntitySchema.get(TestEntity.SCHEMA.id()))
                    .isEqualTo(TestEntity.SCHEMA);
            assertThat(KinematicEntity.loadedById(TestEntity.SCHEMA))
                    .isEmpty();
        });

        TestUtil.unloadChunk(location);
    }

    @Override
    public void cleanup() {
        TestDiskStorageSuccess.TestEntity.SCHEMA.unregister();
    }
}
