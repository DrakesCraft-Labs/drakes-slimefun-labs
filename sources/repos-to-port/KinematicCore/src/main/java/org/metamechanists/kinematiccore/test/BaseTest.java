package org.metamechanists.kinematiccore.test;

import org.bukkit.World;


public interface BaseTest {
    void test(World world);
    void cleanup();
}
