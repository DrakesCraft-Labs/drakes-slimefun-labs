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
import org.metamechanists.kinematiccore.api.state.StateWriter;
import org.metamechanists.kinematiccore.test.BaseTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.*;


public class TestDiskFieldStorageSuccess implements BaseTest {
    @SuppressWarnings("unused")
    private static final class TestEntity extends KinematicEntity<Pig, KinematicEntitySchema> {
        private int integer;
        private final List<String> list;
        private final Location location;

        private static final KinematicEntitySchema SCHEMA = new KinematicEntitySchema(
                "test_disk_field_storage_success",
                EntityType.PIG,
                TestEntity.class
        );

        private TestEntity(@NotNull Location location) {
            super(SCHEMA, () -> location.getWorld().spawn(location, Pig.class));
            this.integer = 5;
            this.list = new ArrayList<>();
            this.location = location;
        }

        @SuppressWarnings("DataFlowIssue")
        public TestEntity(@NotNull StateReader reader, @NotNull Pig pig) {
            super(reader, pig);
            integer = reader.get("integer", Integer.class);
            list = reader.get("list", new ArrayList<>());
            location = reader.get("location", Location.class);
        }

        @Override
        public void write(@NotNull StateWriter writer) {
            integer += 1;
            list.add("bruh");
            writer.set("integer", integer);
            writer.set("list", list);
            writer.set("location", location);
        }
    }

    @SuppressWarnings("CodeBlock2Expr")
    @Override
    public void test(World world) {
        TestEntity.SCHEMA.register(KinematicCore.instance());

        Location location = TestUtil.findUnloadedChunk(world);

        TestUtil.loadChunk(location);

        AtomicReference<UUID> uuid = new AtomicReference<>();
        TestUtil.runSync(() -> uuid.set(new TestEntity(location).uuid()));

        TestUtil.runSync(() -> {
            TestEntity testEntity = (TestEntity) KinematicEntity.get(uuid.get());
            assertThat(testEntity)
                    .isNotNull();
            assertThat(testEntity.integer)
                    .isEqualTo(5);
            assertThat(testEntity.list)
                    .isEmpty();
            //noinspection DataFlowIssue
            assertThat(testEntity.location.x())
                    .isEqualTo(location.x());
        });

        TestUtil.unloadChunk(location);

        TestUtil.runSync(() -> {
            assertThat(KinematicEntity.get(uuid.get()))
                    .isNull();
        });

        TestUtil.loadChunk(location);

        TestUtil.runSync(() -> {
            TestEntity testEntity = (TestEntity) KinematicEntity.get(uuid.get());
            assertThat(KinematicEntitySchema.get(TestEntity.SCHEMA.id()))
                    .isEqualTo(TestEntity.SCHEMA);
            assertThat(testEntity)
                    .isNotNull();
            assertThat(testEntity.integer)
                    .isEqualTo(6);
            assertThat(testEntity.list)
                    .hasSize(1);
            assertThat(testEntity.location.x())
                    .isEqualTo(location.x());
        });

        TestUtil.runSync(() -> {
            //noinspection DataFlowIssue
            KinematicEntity.get(uuid.get()).entity().remove();
        });

        TestUtil.runSync(() -> {
            assertThat(KinematicEntity.get(uuid.get()))
                    .isNull();
        });

        TestUtil.unloadChunk(location);
    }

    @Override
    public void cleanup() {
        TestEntity.SCHEMA.unregister();
    }
}
