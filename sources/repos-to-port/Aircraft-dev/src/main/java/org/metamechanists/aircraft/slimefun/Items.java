package org.metamechanists.aircraft.slimefun;

import io.github.bakedlibs.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.aircraft.Aircraft;
import org.metamechanists.aircraft.vehicle.VehicleEntitySchema;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.api.item.ItemStackBuilder;


public final class Items {
    private static final String DIAMOND_COLOR = "<color:#eec250>";
    private static final String KEY_COLOR = "<color:#b4b4b4>";
    private static final String VALUE_COLOR = "<color:#2182ff>";
    private static final String UNIT_COLOR = "<color:#416a7f>";

    private static final SlimefunItemStack CRUDE_AIRPLANE_WING_STACK = new SlimefunItemStack(
            "AIRCRAFT_AIRPLANE_WING",
            new CustomItemStack(Material.WHITE_WOOL, org.bukkit.ChatColor.WHITE + "Crude Airplane Wing")
    );
    public static final SlimefunItem CRUDE_AIRPLANE_WING = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_AIRPLANE_WING_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.CYAN_WOOL), new ItemStack(Material.WHITE_WOOL),
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), null,
                    null, null, null,
            }
    );

    private static final SlimefunItemStack CRUDE_AIRPLANE_FRAME_STACK = new SlimefunItemStack(
            "AIRCRAFT_AIRPLANE_FRAME",
            new CustomItemStack(Material.OAK_LOG, org.bukkit.ChatColor.WHITE + "Crude Airplane Frame")
    );
    public static final SlimefunItem CRUDE_AIRPLANE_FRAME = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_AIRPLANE_FRAME_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null, new ItemStack(Material.OAK_LOG), null,
                    new ItemStack(Material.OAK_LOG), new ItemStack(Material.OAK_LOG), new ItemStack(Material.OAK_LOG),
                    null, new ItemStack(Material.OAK_LOG), null,
            }
    );

    private static final SlimefunItemStack CRUDE_AIRPLANE_TAIL_STACK = new SlimefunItemStack(
            "AIRCRAFT_AIRPLANE_TAIL",
            new CustomItemStack(Material.OAK_WOOD, org.bukkit.ChatColor.WHITE + "Crude Airplane Tail")
    );
    public static final SlimefunItem CRUDE_AIRPLANE_TAIL = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_AIRPLANE_TAIL_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.OAK_LOG), null,
                    new ItemStack(Material.CYAN_WOOL), new ItemStack(Material.OAK_LOG), new ItemStack(Material.OAK_LOG),
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.OAK_LOG), null,
            }
    );

    private static final SlimefunItemStack CRUDE_AIRPLANE_AVIONICS_STACK = new SlimefunItemStack(
            "AIRCRAFT_AIRPLANE_AVIONICS",
            new CustomItemStack(Material.REPEATER, org.bukkit.ChatColor.WHITE + "Crude Airplane Avionics")
    );
    public static final SlimefunItem CRUDE_AIRPLANE_AVIONICS = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_AIRPLANE_AVIONICS_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.POWER_CRYSTAL, SlimefunItems.GPS_TRANSMITTER,
                    null, null, null,
                    null, null, null,
            }
    );

    private static final SlimefunItemStack CRUDE_AIRPLANE_ENGINE_STACK = new SlimefunItemStack(
            "AIRCRAFT_AIRPLANE_ENGINE",
            new CustomItemStack(Material.PISTON, org.bukkit.ChatColor.WHITE + "Crude Airplane Engine")
    );
    public static final SlimefunItem CRUDE_AIRPLANE_ENGINE = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_AIRPLANE_ENGINE_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.PISTON), new ItemStack(Material.IRON_BLOCK),
                    new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT),
                    new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.PISTON), new ItemStack(Material.IRON_BLOCK),
            }
    );

    private static final SlimefunItemStack CRUDE_AIRSHIP_BALLOON_STACK = new SlimefunItemStack(
            "AIRCRAFT_AIRSHIP_BALLOON",
            new CustomItemStack(Material.WHITE_WOOL, org.bukkit.ChatColor.WHITE + "Crude Airship Balloon")
    );
    public static final SlimefunItem CRUDE_AIRSHIP_BALLOON = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_AIRSHIP_BALLOON_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL),
                    new ItemStack(Material.RED_WOOL), new ItemStack(Material.RED_WOOL), new ItemStack(Material.RED_WOOL),
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL),
            }
    );

    private static final SlimefunItemStack CRUDE_AIRSHIP_FRAME_STACK = new SlimefunItemStack(
            "AIRCRAFT_AIRSHIP_FRAME",
            new CustomItemStack(Material.OAK_LOG, org.bukkit.ChatColor.WHITE + "Crude Airship Frame")
    );
    public static final SlimefunItem CRUDE_AIRSHIP_FRAME = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_AIRSHIP_FRAME_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.OAK_LOG), new ItemStack(Material.OAK_WOOD), new ItemStack(Material.OAK_LOG),
                    new ItemStack(Material.OAK_WOOD), null, new ItemStack(Material.OAK_WOOD),
                    new ItemStack(Material.OAK_LOG), new ItemStack(Material.OAK_WOOD), new ItemStack(Material.OAK_LOG),
            }
    );

    private static final SlimefunItemStack CRUDE_AIRSHIP_AVIONICS_STACK = new SlimefunItemStack(
            "AIRCRAFT_AIRSHIP_AVIONICS",
            new CustomItemStack(Material.REPEATER, org.bukkit.ChatColor.WHITE + "Crude Airship Avionics")
    );
    public static final SlimefunItem CRUDE_AIRSHIP_AVIONICS = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_AIRSHIP_AVIONICS_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.GPS_TRANSMITTER_2, SlimefunItems.POWER_CRYSTAL,
                    null, null, null,
                    null, null, null,
            }
    );

    private static final SlimefunItemStack CRUDE_AIRSHIP_ENGINE_STACK = new SlimefunItemStack(
            "AIRCRAFT_AIRSHIP_ENGINE",
            new CustomItemStack(Material.PISTON, org.bukkit.ChatColor.WHITE + "Crude Airship Engine")
    );
    public static final SlimefunItem CRUDE_AIRSHIP_ENGINE = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_AIRSHIP_ENGINE_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.ANVIL), null, new ItemStack(Material.ANVIL),
                    null, SlimefunItems.ENHANCED_FURNACE_4, null,
                    new ItemStack(Material.ANVIL), null, new ItemStack(Material.ANVIL),
            }
    );

    private static final SlimefunItemStack CRUDE_DRONE_FRAME_STACK = new SlimefunItemStack(
            "AIRCRAFT_DRONE_FRAME",
            new CustomItemStack(Material.OAK_LOG, org.bukkit.ChatColor.WHITE + "Crude Airship Frame")
    );
    public static final SlimefunItem CRUDE_DRONE_FRAME = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_DRONE_FRAME_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.OAK_WOOD), new ItemStack(Material.OAK_WOOD), new ItemStack(Material.OAK_WOOD),
                    new ItemStack(Material.OAK_WOOD), null, new ItemStack(Material.OAK_WOOD),
                    new ItemStack(Material.OAK_WOOD), new ItemStack(Material.OAK_WOOD), new ItemStack(Material.OAK_WOOD),
            }
    );

    private static final SlimefunItemStack CRUDE_DRONE_ROTOR_STACK = new SlimefunItemStack(
            "AIRCRAFT_DRONE_ROTOR",
            new CustomItemStack(Material.GRAY_CONCRETE, org.bukkit.ChatColor.WHITE + "Crude Drone Rotor")
    );
    public static final SlimefunItem CRUDE_DRONE_ROTOR = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_DRONE_ROTOR_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.IRON_BLOCK), null, null,
                    null, new ItemStack(Material.IRON_BLOCK), null,
                    null, null, new ItemStack(Material.IRON_BLOCK),
            }
    );

    private static final SlimefunItemStack CRUDE_DRONE_ENGINE_STACK = new SlimefunItemStack(
            "AIRCRAFT_DRONE_ENGINE",
            new CustomItemStack(Material.PISTON, org.bukkit.ChatColor.WHITE + "Crude Drone Engine")
    );
    public static final SlimefunItem CRUDE_DRONE_ENGINE = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_DRONE_ENGINE_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA),
                    new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.FURNACE), new ItemStack(Material.IRON_BLOCK),
                    new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA),
            }
    );

    private static final SlimefunItemStack CRUDE_DRONE_AVIONICS_STACK = new SlimefunItemStack(
            "AIRCRAFT_DRONE_AVIONICS",
            new CustomItemStack(Material.REPEATER, org.bukkit.ChatColor.WHITE + "Crude Drone Avionics")
    );
    public static final SlimefunItem CRUDE_DRONE_AVIONICS = new SlimefunItem(
            Groups.COMPONENTS,
            CRUDE_DRONE_AVIONICS_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null, SlimefunItems.BASIC_CIRCUIT_BOARD, null,
                    null, SlimefunItems.POWER_CRYSTAL, null,
                    null, SlimefunItems.GPS_TRANSMITTER, null,
            }
    );

    private static final SlimefunItemStack HOVERDUCK_AVIONICS_STACK = new SlimefunItemStack(
            "AIRCRAFT_HOVERDUCK_AVIONICS",
            new CustomItemStack(Material.TARGET, org.bukkit.ChatColor.WHITE + "Hoverduck Avionics")
    );
    public static final SlimefunItem HOVERDUCK_AVIONICS = new SlimefunItem(
            Groups.COMPONENTS,
            HOVERDUCK_AVIONICS_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null, null, null,
                    null, SlimefunItems.ANDROID_MEMORY_CORE, null,
                    null, SlimefunItems.GPS_TRANSMITTER_4, null,
            }
    );

    private static final SlimefunItemStack HOVERDUCK_ANTIGRAV_SYSTEM_STACK = new SlimefunItemStack(
            "AIRCRAFT_HOVERDUCK_ANTIGRAV_SYSTEM",
            new CustomItemStack(Material.LIGHT_BLUE_CONCRETE, org.bukkit.ChatColor.WHITE + "Hoverduck Antigrav System")
    );
    public static final SlimefunItem HOVERDUCK_ANTIGRAV_SYSTEM = new SlimefunItem(
            Groups.COMPONENTS,
            HOVERDUCK_ANTIGRAV_SYSTEM_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    SlimefunItems.POWER_CRYSTAL, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.POWER_CRYSTAL,
                    SlimefunItems.REINFORCED_PLATE, SlimefunItems.STEEL_THRUSTER, SlimefunItems.REINFORCED_PLATE,
                    SlimefunItems.REINFORCED_PLATE, SlimefunItems.STEEL_THRUSTER, SlimefunItems.REINFORCED_PLATE,
            }
    );

    private static final SlimefunItemStack CESSNA_FRAME_STACK = new SlimefunItemStack(
            "AIRCRAFT_CESSNA_FRAME",
            new CustomItemStack(Material.IRON_BLOCK, org.bukkit.ChatColor.WHITE + "Cessna Frame")
    );
    public static final SlimefunItem CESSNA_FRAME = new SlimefunItem(
            Groups.COMPONENTS,
            CESSNA_FRAME_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.ANVIL), new ItemStack(Material.ANVIL), new ItemStack(Material.ANVIL),
                    SlimefunItems.REINFORCED_PLATE, null, SlimefunItems.REINFORCED_PLATE,
                    new ItemStack(Material.ANVIL), new ItemStack(Material.ANVIL), new ItemStack(Material.ANVIL),
            }
    );

    private static final SlimefunItemStack CESSNA_AVIONICS_STACK = new SlimefunItemStack(
            "AIRCRAFT_CESSNA_AVIONICS",
            new CustomItemStack(Material.REPEATER, org.bukkit.ChatColor.WHITE + "Cessna Avionics")
    );
    public static final SlimefunItem CESSNA_AVIONICS = new SlimefunItem(
            Groups.COMPONENTS,
            CESSNA_AVIONICS_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null, SlimefunItems.POWER_CRYSTAL, null,
                    null, SlimefunItems.ADVANCED_CIRCUIT_BOARD, null,
                    null, SlimefunItems.GPS_TRANSMITTER_4, null,
            }
    );

    private static final SlimefunItemStack CESSNA_WING_STACK = new SlimefunItemStack(
            "AIRCRAFT_CESSNA_WING",
            new CustomItemStack(Material.WHITE_WOOL, org.bukkit.ChatColor.WHITE + "Cessna Wing")
    );
    public static final SlimefunItem CESSNA_WING = new SlimefunItem(
            Groups.COMPONENTS,
            CESSNA_WING_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.IRON_BLOCK),
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.IRON_BLOCK),
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.IRON_BLOCK),
            }
    );

    private static final SlimefunItemStack CESSNA_TAIL_STACK = new SlimefunItemStack(
            "AIRCRAFT_CESSNA_TAIL",
            new CustomItemStack(Material.IRON_BLOCK, org.bukkit.ChatColor.WHITE + "Cessna Tail")
    );
    public static final SlimefunItem CESSNA_TAIL = new SlimefunItem(
            Groups.COMPONENTS,
            CESSNA_TAIL_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.IRON_BLOCK), null,
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK),
                    new ItemStack(Material.WHITE_WOOL), new ItemStack(Material.IRON_BLOCK), null,
            }
    );

    private static final SlimefunItemStack CESSNA_ENGINE_STACK = new SlimefunItemStack(
            "AIRCRAFT_CESSNA_ENGINE",
            new CustomItemStack(Material.PISTON, org.bukkit.ChatColor.WHITE + "Cessna Engine")
    );
    public static final SlimefunItem CESSNA_ENGINE = new SlimefunItem(
            Groups.COMPONENTS,
            CESSNA_ENGINE_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.ANVIL), new ItemStack(Material.PISTON), new ItemStack(Material.ANVIL),
                    new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK),
                    new ItemStack(Material.ANVIL), new ItemStack(Material.PISTON), new ItemStack(Material.ANVIL),
            }
    );

    private static final SlimefunItemStack THROTTLE_UP_STACK = new SlimefunItemStack(
            "AIRCRAFT_THROTTLE_UP",
            new CustomItemStack(Material.LIME_DYE, ChatColor.WHITE + "Throttle up"));
    private static final AircraftControl THROTTLE_UP = new AircraftControl(
            Groups.CONTROLS,
            THROTTLE_UP_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    SlimefunItems.BASIC_CIRCUIT_BOARD, null, null,
                    null, null, null,
                    null, null, null,
            },
            "THROTTLE_UP");

    private static final SlimefunItemStack THROTTLE_DOWN_STACK = new SlimefunItemStack(
            "AIRCRAFT_THROTTLE_DOWN",
            new CustomItemStack(Material.RED_DYE, ChatColor.WHITE + "Throttle down"));
    private static final AircraftControl THROTTLE_DOWN = new AircraftControl(
            Groups.CONTROLS,
            THROTTLE_DOWN_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null, SlimefunItems.BASIC_CIRCUIT_BOARD, null,
                    null, null, null,
                    null, null, null,
            },
            "THROTTLE_DOWN");

    private static final SlimefunItemStack STEER_LEFT_STACK = new SlimefunItemStack(
            "AIRCRAFT_STEER_LEFT",
            new CustomItemStack(Material.MUSIC_DISC_STAL, ChatColor.WHITE + "Steer left"));
    private static final AircraftControl STEER_LEFT = new AircraftControl(
            Groups.CONTROLS,
            STEER_LEFT_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null, null, SlimefunItems.BASIC_CIRCUIT_BOARD,
                    null, null, null,
                    null, null, null,
            },
            "STEER_LEFT");

    private static final SlimefunItemStack STEER_RIGHT_STACK = new SlimefunItemStack(
            "AIRCRAFT_STEER_RIGHT",
            new CustomItemStack(Material.MUSIC_DISC_STAL, ChatColor.WHITE + "Steer right"));
    private static final AircraftControl STEER_RIGHT = new AircraftControl(
            Groups.CONTROLS,
            STEER_RIGHT_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null, null, null,
                    SlimefunItems.BASIC_CIRCUIT_BOARD, null, null,
                    null, null, null,
            },
            "STEER_RIGHT");

    private static final SlimefunItemStack STRAFE_LEFT_STACK  = new SlimefunItemStack(
            "AIRCRAFT_STRAFE_LEFT",
            new CustomItemStack(Material.ECHO_SHARD, ChatColor.WHITE + "Strafe left"));
    private static final AircraftControl STRAFE_LEFT = new AircraftControl(
            Groups.CONTROLS,
            STRAFE_LEFT_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null, null, null,
                    null, SlimefunItems.BASIC_CIRCUIT_BOARD, null,
                    null, null, null,
            },
            "STRAFE_LEFT");

    private static final SlimefunItemStack STRAFE_RIGHT_STACK = new SlimefunItemStack(
            "AIRCRAFT_STRAFE_RIGHT",
            new CustomItemStack(Material.AMETHYST_SHARD, ChatColor.WHITE + "Strafe right"));
    private static final AircraftControl STRAFE_RIGHT = new AircraftControl(
            Groups.CONTROLS,
            STRAFE_RIGHT_STACK,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null, null, null,
                    null, null, SlimefunItems.BASIC_CIRCUIT_BOARD,
                    null, null, null,
            },
            "STRAFE_RIGHT");

    private static final SlimefunItemStack CRUDE_AIRPLANE_STACK = new SlimefunItemStack(
            "AIRCRAFT_CRUDE_AIRPLANE",
            new ItemStackBuilder(Material.FEATHER)
                    .name("Crude Airplane")
                    .loreLine(ItemStackBuilder.VEHICLE)
                    .loreLine("")
                    .loreLine("<color:gray>Slow and inefficient, but at least it flies")
                    .loreLine("<color:gray>(That is, until you crash it. You buffoon)")
                    .loreLine("<color:#c9c9c9>Consumes coal/charcoal")
                    .loreLine("")
                    .loreLine(KEY_COLOR + "Throttle: " + VALUE_COLOR + "Throttle Up | Throttle Down")
                    .loreLine(KEY_COLOR + "Pitch: " + VALUE_COLOR + "w | s")
                    .loreLine(KEY_COLOR + "Roll: " + VALUE_COLOR + "a | d")
                    .loreLine(KEY_COLOR + "Steer (on the ground only): " + VALUE_COLOR + "Steer Left | Steer Right")
                    .loreLine("")
                    .loreLine("<color:yellow>Model by Idra")
                    .build()
    );

    private static final SlimefunItemStack CRUDE_AIRSHIP_STACK = new SlimefunItemStack(
            "AIRCRAFT_CRUDE_AIRSHIP",
            new ItemStackBuilder(Material.PAPER)
                    .name("Crude Airship")
                    .loreLine(ItemStackBuilder.VEHICLE)
                    .loreLine("")
                    .loreLine("<color:#c9c9c9>Easier to fly than the Crude Airplane, but rather slow...")
                    .loreLine("<color:#c9c9c9>Consumes coal/charcoal and water")
                    .loreLine("")
                    .loreLine(KEY_COLOR + "Throttle: " + VALUE_COLOR + "Throttle Up | Throttle Down")
                    .loreLine(KEY_COLOR + "Ascend/Descend: " + VALUE_COLOR + "w | s")
                    .loreLine(KEY_COLOR + "Yaw: " + VALUE_COLOR + "a | d")
                    .loreLine(KEY_COLOR + "Strafe: " + VALUE_COLOR + "Strafe Left | Strafe Right")
                    .loreLine("")
                    .loreLine("<color:yellow>Model by Idra")
                    .build()
    );

    private static final SlimefunItemStack CRUDE_DRONE_STACK = new SlimefunItemStack(
            "AIRCRAFT_CRUDE_DRONE",
            new ItemStackBuilder(Material.PHANTOM_MEMBRANE)
                    .name("Crude Drone")
                    .loreLine(ItemStackBuilder.VEHICLE)
                    .loreLine("")
                    .loreLine("<color:#c9c9c9>Versatile and easy to fly, but slow to turn and move")
                    .loreLine("<color:#c9c9c9>Consumes coal/charcoal and water")
                    .loreLine("")
                    .loreLine(KEY_COLOR + "Throttle: " + VALUE_COLOR + "Throttle Up | Throttle Down")
                    .loreLine(KEY_COLOR + "Pitch: " + VALUE_COLOR + "w | s")
                    .loreLine(KEY_COLOR + "Roll: " + VALUE_COLOR + "a | d")
                    .loreLine(KEY_COLOR + "Yaw: " + VALUE_COLOR + "Steer Left | Steer Right")
                    .loreLine("")
                    .loreLine("<color:yellow>Model by Idra")
                    .build()
    );

    private static final SlimefunItemStack CESSNA_STACK = new SlimefunItemStack(
            "AIRCRAFT_CESSNA",
            new ItemStackBuilder(Material.IRON_NUGGET)
                    .name("Cessna")
                    .loreLine(ItemStackBuilder.VEHICLE)
                    .loreLine("")
                    .loreLine("<color:gray>An upgrade over the crude aircraft")
                    .loreLine("")
                    .loreLine(KEY_COLOR + "Throttle: " + VALUE_COLOR + "Throttle Up | Throttle Down")
                    .loreLine(KEY_COLOR + "Pitch: " + VALUE_COLOR + "w | s")
                    .loreLine(KEY_COLOR + "Roll: " + VALUE_COLOR + "a | d")
                    .loreLine(KEY_COLOR + "Steer (on the ground only): " + VALUE_COLOR + "Steer Left | Steer Right")
                    .loreLine("")
                    .loreLine("<color:yellow>Model by TK4YT")
                    .build()
    );

    private static final SlimefunItemStack HOVERDUCK_STACK = new SlimefunItemStack(
            "AIRCRAFT_HOVERDUCK",
            new ItemStackBuilder(Material.GOLD_INGOT)
                    .name("Hoverduck™")
                    .loreLine(ItemStackBuilder.VEHICLE)
                    .loreLine("")
                    .loreLine("<color:#c9c9c9>Quack quack")
                    .loreLine("")
                    .loreLine(KEY_COLOR + "Throttle: " + VALUE_COLOR + "Throttle Up | Throttle Down")
                    .loreLine(KEY_COLOR + "Ascend/Descend: " + VALUE_COLOR + "w | s")
                    .loreLine(KEY_COLOR + "Yaw: " + VALUE_COLOR + "a | d")
                    .loreLine("")
                    .loreLine("<color:yellow>Model by Idra")
                    .build()
    );

    private static final SlimefunItemStack METACOIN_UFO_STACK = new SlimefunItemStack(
            "AIRCRAFT_METACOIN_UFO",
            new ItemStackBuilder(Material.GOLD_INGOT)
                    .name("Metacoin™ UFO")
                    .loreLine(ItemStackBuilder.VEHICLE)
                    .loreLine("")
                    .loreLine("<color:#c9c9c9>The transportation option of the future!")
                    .loreLine("<color:#c9c9c9>Embrace the future of blockchain technology")
                    .loreLine("<color:#c9c9c9>TO THE MOON!!!")
                    .loreLine("")
                    .loreLine(KEY_COLOR + "Throttle: " + VALUE_COLOR + "Throttle Up | Throttle Down")
                    .loreLine(KEY_COLOR + "Pitch: " + VALUE_COLOR + "w | s")
                    .loreLine(KEY_COLOR + "Roll: " + VALUE_COLOR + "a | d")
                    .loreLine(KEY_COLOR + "Yaw: " + VALUE_COLOR + "Steer Left | Steer Right")
                    .loreLine("")
                    .loreLine("<color:yellow>Model by Idra")
                    .build()
    );

    private static @NotNull VehicleItem crudeAirplane(VehicleEntitySchema schema) {
        return new VehicleItem(
                "AIRCRAFT_CRUDE_AIRPLANE",
                CRUDE_AIRPLANE_STACK,
                schema,
                Groups.VEHICLES,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        null, CRUDE_AIRPLANE_WING_STACK, null,
                        CRUDE_AIRPLANE_TAIL_STACK, CRUDE_AIRPLANE_FRAME_STACK, CRUDE_AIRPLANE_ENGINE_STACK,
                        null, CRUDE_AIRPLANE_WING_STACK, CRUDE_AIRPLANE_AVIONICS_STACK,
                });
    }

    private static @NotNull VehicleItem crudeAirship(VehicleEntitySchema schema) {
        return new VehicleItem(
                "AIRCRAFT_CRUDE_AIRSHIP",
                CRUDE_AIRSHIP_STACK,
                schema,
                Groups.VEHICLES,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        CRUDE_AIRSHIP_BALLOON_STACK, CRUDE_AIRSHIP_BALLOON_STACK, CRUDE_AIRSHIP_BALLOON_STACK,
                        new ItemStack(Material.GRAY_CONCRETE), null, new ItemStack(Material.GRAY_CONCRETE),
                        CRUDE_AIRSHIP_ENGINE_STACK, CRUDE_AIRSHIP_FRAME_STACK, CRUDE_AIRSHIP_AVIONICS_STACK,
                });
    }

    private static @NotNull VehicleItem crudeDrone(VehicleEntitySchema schema) {
        return new VehicleItem(
                "AIRCRAFT_CRUDE_DRONE",
                CRUDE_DRONE_STACK,
                schema,
                Groups.VEHICLES,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        CRUDE_DRONE_ROTOR_STACK, CRUDE_DRONE_FRAME_STACK, CRUDE_DRONE_ROTOR_STACK,
                        null, CRUDE_DRONE_ENGINE_STACK, CRUDE_DRONE_AVIONICS_STACK,
                        CRUDE_DRONE_ROTOR_STACK, CRUDE_DRONE_FRAME_STACK, CRUDE_DRONE_ROTOR_STACK,

                });
    }

    private static @NotNull VehicleItem cessna(VehicleEntitySchema schema) {
        return new VehicleItem(
                "AIRCRAFT_CESSNA",
                CESSNA_STACK,
                schema,
                Groups.VEHICLES,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        null, CESSNA_WING_STACK, null,
                        CESSNA_TAIL_STACK, CESSNA_FRAME_STACK, CESSNA_ENGINE_STACK,
                        null, CESSNA_WING_STACK, CESSNA_AVIONICS_STACK,
                });
    }

    private static @NotNull VehicleItem hoverduck(VehicleEntitySchema schema) {
        return new VehicleItem(
                "AIRCRAFT_HOVERDUCK",
                HOVERDUCK_STACK,
                schema,
                Groups.VEHICLES,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        null, slimefunItemOrFallback("DUCK_PLUSH_MODERATE", Material.YELLOW_WOOL), null,
                        null, HOVERDUCK_AVIONICS_STACK, null,
                        null, HOVERDUCK_ANTIGRAV_SYSTEM_STACK, null,
                });
    }

    private static @NotNull VehicleItem metacoinUfo(VehicleEntitySchema schema) {
        return new VehicleItem(
                "AIRCRAFT_METACOIN_UFO",
                METACOIN_UFO_STACK,
                schema,
                Groups.VEHICLES,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        new ItemStack(Material.DEAD_BUSH), new ItemStack(Material.DEAD_BUSH), new ItemStack(Material.DEAD_BUSH),
                        new ItemStack(Material.DEAD_BUSH), slimefunItemOrFallback("INFINITY_SINGULARITY", Material.NETHER_STAR), new ItemStack(Material.DEAD_BUSH),
                        new ItemStack(Material.DEAD_BUSH), new ItemStack(Material.DEAD_BUSH), new ItemStack(Material.DEAD_BUSH),
                });
    }

    /**
     * Optional cross-addon ingredients (p. ej. plush de otro addon, singularidad de Infinity).
     * Si el id no está registrado, se usa un material vanilla para no tumbar el enable del plugin.
     */
    private static @NotNull ItemStack slimefunItemOrFallback(@NotNull String id, @NotNull Material fallback) {
        SlimefunItem item = SlimefunItem.getById(id);
        if (item == null) {
            Aircraft.getInstance().getLogger().warning(
                    "Slimefun item '" + id + "' no está registrado (addon opcional ausente). Receta con respaldo vanilla: " + fallback);
            return new ItemStack(fallback);
        }
        return item.getItem();
    }

    /**
     * Tras recargar YAML, reaplica el schema al {@link VehicleItem} ya registrado (no volver a {@code register}).
     */
    private static void applyReloadedSchema(@NotNull SlimefunItem rebuiltItem, @NotNull VehicleEntitySchema schema) {
        SlimefunItem registered = Slimefun.getRegistry().getSlimefunItemIds().get(rebuiltItem.getId());
        if (registered instanceof VehicleItem vehicleItem) {
            vehicleItem.schema = schema;
        } else {
            Aircraft.getInstance().getLogger().warning(
                    "reload: no hay VehicleItem registrado para id " + rebuiltItem.getId() + " (omitido)");
        }
    }

    private Items() {}

    private static @Nullable VehicleEntitySchema loadVehicle(@NotNull String id, @NotNull ItemStack itemStack) {
        try {
            return new VehicleEntitySchema(id, itemStack);
        } catch (RuntimeException e) {
            Aircraft.getInstance().getLogger().severe("Failed to load aircraft " + id);
            e.printStackTrace();
            return null;
        }
    }

    public static void init() {
        JavaPlugin plugin = Aircraft.getInstance();
        SlimefunAddon addon = (SlimefunAddon) plugin;

        CRUDE_AIRPLANE_WING.register(addon);
        CRUDE_AIRPLANE_FRAME.register(addon);
        CRUDE_AIRPLANE_TAIL.register(addon);
        CRUDE_AIRPLANE_AVIONICS.register(addon);
        CRUDE_AIRPLANE_ENGINE.register(addon);

        CRUDE_AIRSHIP_BALLOON.register(addon);
        CRUDE_AIRSHIP_FRAME.register(addon);
        CRUDE_AIRSHIP_AVIONICS.register(addon);
        CRUDE_AIRSHIP_ENGINE.register(addon);

        CRUDE_DRONE_FRAME.register(addon);
        CRUDE_DRONE_ROTOR.register(addon);
        CRUDE_DRONE_ENGINE.register(addon);
        CRUDE_DRONE_AVIONICS.register(addon);

        HOVERDUCK_AVIONICS.register(addon);
        HOVERDUCK_ANTIGRAV_SYSTEM.register(addon);

        CESSNA_FRAME.register(addon);
        CESSNA_AVIONICS.register(addon);
        CESSNA_WING.register(addon);
        CESSNA_TAIL.register(addon);
        CESSNA_ENGINE.register(addon);

        THROTTLE_UP.register(addon);
        THROTTLE_DOWN.register(addon);
        STEER_LEFT.register(addon);
        STEER_RIGHT.register(addon);
        STRAFE_LEFT.register(addon);
        STRAFE_RIGHT.register(addon);

        initAircraftItems(addon);
    }

    private static void initAircraftItems(SlimefunAddon addon) {
        VehicleEntitySchema crudeAirplaneSchema = loadVehicle("crude_airplane", CRUDE_AIRPLANE_STACK);
        if (crudeAirplaneSchema != null) {
            crudeAirplane(crudeAirplaneSchema).register(addon);
        }

        VehicleEntitySchema crudeAirshipSchema = loadVehicle("crude_airship", CRUDE_AIRSHIP_STACK);
        if (crudeAirshipSchema != null) {
            crudeAirship(crudeAirshipSchema).register(addon);
        }

        VehicleEntitySchema crudeDroneSchema = loadVehicle("crude_drone", CRUDE_DRONE_STACK);
        if (crudeDroneSchema != null) {
            crudeDrone(crudeDroneSchema).register(addon);
        }

        VehicleEntitySchema cessnaSchema = loadVehicle("cessna", CESSNA_STACK);
        if (cessnaSchema != null) {
            cessna(cessnaSchema).register(addon);
        }

        VehicleEntitySchema hoverduckSchema = loadVehicle("hoverduck", HOVERDUCK_STACK);
        if (hoverduckSchema != null) {
            hoverduck(hoverduckSchema).register(addon);
        }

        VehicleEntitySchema metacoinUfoSchema = loadVehicle("metacoin_ufo", METACOIN_UFO_STACK);
        if (metacoinUfoSchema != null) {
            metacoinUfo(metacoinUfoSchema).register(addon);
        }
    }

    public static void reload() {
        for (String id : KinematicEntitySchema.registeredSchemasByAddon(Aircraft.getInstance())) {
            KinematicEntitySchema schema = KinematicEntitySchema.get(id);
            if (schema != null) {
                Aircraft.getInstance().getLogger().info("yeeted " + id);
                schema.unregister();
            }
        }

        SlimefunAddon addon = Aircraft.getInstance();

        VehicleEntitySchema crudeAirplaneSchema = loadVehicle("crude_airplane", CRUDE_AIRPLANE_STACK);
        if (crudeAirplaneSchema != null) {
            SlimefunItem newItem = crudeAirplane(crudeAirplaneSchema);
            applyReloadedSchema(newItem, crudeAirplaneSchema);
        }

        VehicleEntitySchema crudeAirshipSchema = loadVehicle("crude_airship", CRUDE_AIRSHIP_STACK);
        if (crudeAirshipSchema != null) {
            SlimefunItem newItem = crudeAirship(crudeAirshipSchema);
            applyReloadedSchema(newItem, crudeAirshipSchema);
        }

        VehicleEntitySchema crudeDroneSchema = loadVehicle("crude_drone", CRUDE_DRONE_STACK);
        if (crudeDroneSchema != null) {
            SlimefunItem newItem = crudeDrone(crudeDroneSchema);
            applyReloadedSchema(newItem, crudeDroneSchema);
        }

        VehicleEntitySchema cessnaSchema = loadVehicle("cessna", CESSNA_STACK);
        if (cessnaSchema != null) {
            SlimefunItem newItem = cessna(cessnaSchema);
            applyReloadedSchema(newItem, cessnaSchema);
        }

        VehicleEntitySchema hoverduckSchema = loadVehicle("hoverduck", HOVERDUCK_STACK);
        if (hoverduckSchema != null) {
            SlimefunItem newItem = hoverduck(hoverduckSchema);
            applyReloadedSchema(newItem, hoverduckSchema);
        }

        VehicleEntitySchema metacoinUfo = loadVehicle("metacoin_ufo", METACOIN_UFO_STACK);
        if (metacoinUfo != null) {
            SlimefunItem newItem = metacoinUfo(metacoinUfo);
            applyReloadedSchema(newItem, metacoinUfo);
        }
    }

    private static @NotNull String keyValueUnit(String key, String value, String unit) {
        return DIAMOND_COLOR + ItemStackBuilder.DIAMOND
                + " " + KEY_COLOR + key
                + " " + VALUE_COLOR + value
                + " " + UNIT_COLOR + unit;
    }
}
