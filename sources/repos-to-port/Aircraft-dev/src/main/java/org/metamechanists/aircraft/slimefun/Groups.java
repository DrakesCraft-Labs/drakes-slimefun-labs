package org.metamechanists.aircraft.slimefun;

import dev.drake.sefilib.slimefun.itemgroup.DummyItemGroup;
import dev.drake.sefilib.slimefun.itemgroup.SimpleFlexGroup;
import io.github.bakedlibs.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import org.bukkit.Material;
import org.metamechanists.aircraft.Aircraft;
import org.metamechanists.aircraft.utils.Keys;


public class Groups {

    public static final SimpleFlexGroup AIRCRAFT = new SimpleFlexGroup(
            Aircraft.getInstance(),
            "Aircraft",
            Keys.newKey("AIRCRAFT"),
            new CustomItemStack(Material.FEATHER, "&fAircraft")
    );

    public static final ItemGroup VEHICLES = new DummyItemGroup(
            Keys.newKey("AIRCRAFT_VEHICLES"),
            new CustomItemStack(Material.PHANTOM_MEMBRANE, "&fVehicles")
    );

    public static final ItemGroup CONTROLS = new DummyItemGroup(
            Keys.newKey("AIRCRAFT_CONTROLS"),
            new CustomItemStack(Material.LIME_DYE, "&fControls")
    );

    public static final ItemGroup COMPONENTS = new DummyItemGroup(
            Keys.newKey("AIRCRAFT_COMPONENTS"),
            new CustomItemStack(Material.IRON_BLOCK, "&fComponents")
    );

    public static void register() {
        Aircraft addon = Aircraft.getInstance();

        AIRCRAFT.addItemGroup(VEHICLES);
        AIRCRAFT.addItemGroup(CONTROLS);
        AIRCRAFT.addItemGroup(COMPONENTS);
        AIRCRAFT.register(addon);
    }
}
