package com.github.drakescraft-labs.infinitylib.core;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;
import com.github.drakescraft-labs.otheraddon.MockOtherAddon;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestAddonLive {

    private static PluginManagerMock manager;

    @BeforeAll
    public static void load() {
        manager = MockBukkit.mock().getPluginManager();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @BeforeEach
    void clear() {
        manager.clearPlugins();
    }

    @Test
    void testNullInstance() {
        assertThrows(NullPointerException.class, AbstractAddon::instance);
    }

    @Test
    void testNoCommand() {
        MockAddon.setTestCase(null);
        assertDoesNotThrow(() -> MockBukkit.load(MockAddon.class));
        // assertThrows(NullPointerException.class, () -> MockAddon.instance().getAddonCommand());
    }

    @Test
    void testSharedInfinityLib() {
        PluginDescriptionFile desc = new PluginDescriptionFile("MockAddon", "", MockOtherAddon.class.getName());
        assertThrows(RuntimeException.class, () -> MockBukkit.load(MockOtherAddon.class, desc));
    }

    @Test
    void testBadGithubStrings() {
        MockAddon.setTestCase(MockAddonTest.BAD_GITHUB_PATH);
        assertThrows(RuntimeException.class,
                () -> MockBukkit.load(MockAddon.class));
    }

    @Test
    void testMissingAutoUpdateKey() {
        MockAddon.setTestCase(MockAddonTest.MISSING_KEY);
        assertDoesNotThrow(
                () -> MockBukkit.load(MockAddon.class));
    }

    @Test
    void testSuperEnable() {
        MockAddon.setTestCase(MockAddonTest.CALL_SUPER);
        assertDoesNotThrow(
                () -> MockBukkit.load(MockAddon.class));
        assertDoesNotThrow(
                () -> manager.disablePlugin(MockAddon.instance()));
    }

    @Test
    void testErrorThrown() {
        MockAddon.setTestCase(MockAddonTest.THROW_EXCEPTION);
        assertDoesNotThrow(
                () -> MockBukkit.load(MockAddon.class));
        assertDoesNotThrow(
                () -> manager.disablePlugin(MockAddon.instance()));
    }

}
