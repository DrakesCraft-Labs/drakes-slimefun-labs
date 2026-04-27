package org.metamechanists.kinematiccore.test;


import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.test.entity.TestDiskFieldStorageSuccess;
import org.metamechanists.kinematiccore.test.entity.TestDiskStorageSuccess;
import org.metamechanists.kinematiccore.test.entity.TestDoubleRegister;
import org.metamechanists.kinematiccore.test.entity.TestMemoryStorageSuccess;
import org.metamechanists.kinematiccore.test.entity.TestMissingConstructor;

import java.util.ArrayList;
import java.util.List;


public final class MainTester {
    private final List<BaseTest> tests = List.of(
            new TestDoubleRegister(),
            new TestMissingConstructor(),
            new TestMemoryStorageSuccess(),
            new TestDiskStorageSuccess(),
            new TestDiskFieldStorageSuccess()
    );
    private final World world;

    public MainTester(World world) {
        this.world = world;
    }

    public record TestResult(int total, int passed, int failed, List<String> failures) {}

    public @NotNull TestResult all() {
        int total = 0;
        int passed = 0;
        int failed = 0;
        List<String> failures = new ArrayList<>();

        for (BaseTest test : tests) {
            boolean success = test(test);
            total += 1;
            if (success) {
                passed += 1;
            } else {
                failed += 1;
                failures.add(test.getClass().getSimpleName());
            }
        }

        return new TestResult(total, passed, failed, failures);
    }

    private boolean test(@NotNull BaseTest test) {
        boolean success = true;

        try {
            test.test(world);
        } catch (Exception | LinkageError | AssertionError e) {
            e.printStackTrace();
            success = false;
        }

        try {
            test.cleanup();
        } catch (Exception | LinkageError | AssertionError e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }
}
