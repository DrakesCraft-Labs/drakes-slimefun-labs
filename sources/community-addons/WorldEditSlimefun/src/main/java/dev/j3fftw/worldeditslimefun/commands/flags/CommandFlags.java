package dev.j3fftw.worldeditslimefun.commands.flags;

import co.aikar.commands.BukkitCommandCompletionContext;
import dev.j3fftw.worldeditslimefun.tasks.RefillInputsTask;
import dev.j3fftw.worldeditslimefun.tasks.VoidOutputsTask;
import dev.j3fftw.worldeditslimefun.utils.Utils;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommandFlags {
    private static final Map<String, CommandFlag<?>> FLAG_TYPES = new HashMap<>(Map.of(
            "--energy", new EnergyFlag(),
            "--inputs", new InputsFlag(),
            "--refill_inputs_task", new RefillInputsFlag(),
            "--void_outputs_task", new VoidOutputsFlag(),
            "--task_timeout", new TimeoutFlag()
    ));

    /**
     * Takes a type and value and converts it into a matching {@link CommandFlag}
     * @param type The type of flag to get, formatted as "--type"
     * @param value The value of the flag to get, format depends on the type provided
     * @return the matching {@link CommandFlag} or null if one could not be found
     */
    public static @Nullable CommandFlag<?> getFlag(@Nonnull String type, @Nonnull String value) {
        CommandFlag<?> flag = FLAG_TYPES.get(type);
        if (flag != null) {
            return flag.ofValue(value);
        }
        return null;
    }

    /**
     * Translates the provided args into {@link CommandFlag CommandFlags}
     * @param args The args to convert, formatted as: {"--energy", "true", "--task_timeout", "10s"}
     * @return The command flags from the given arguments, empty if arguments are invalid
     */
    public static @Nonnull List<CommandFlag<?>> getFlags(@Nonnull List<String> args) {
        List<CommandFlag<?>> flags = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            if (FLAG_TYPES.containsKey(arg)) {
                CommandFlag<?> flag = CommandFlags.getFlag(arg, args.get(i + 1));
                if (flag != null) {
                    flags.add(flag);
                }
            }
        }
        return flags;
    }

    /**
     * Attempts to register the flag type, fails if there is already a flag for the given flag type
     * @param type A {@link String} flag type in the format of "--type"
     * @param flag The {@link CommandFlag} for the given flag type
     * @return If the flag type was successfully registered
     */
    public static boolean addFlagType(@Nonnull String type, @Nonnull CommandFlag<?> flag) {
        if (FLAG_TYPES.containsKey(type)) {
            return false;
        }
        FLAG_TYPES.put(type, flag);
        return true;
    }

    /**
     * @return An unmodifiable copy of {@link CommandFlags#FLAG_TYPES}
     */
    public static @Nonnull Map<String, CommandFlag<?>> getFlagTypes() {
        return Map.copyOf(FLAG_TYPES);
    }

    public static class EnergyFlag extends CommandFlag<Boolean> {
        @Override
        public void apply(Player player, List<CommandFlag<?>> flags, SlimefunItem sfItem, Block block) {
            BlockStorage.addBlockInfo(block, "energy-charge", String.valueOf(Integer.MAX_VALUE), false);
        }

        @Override
        public boolean canApply(SlimefunItem sfItem) {
            return this.value != null && this.value && sfItem instanceof EnergyNetComponent component && component.isChargeable();
        }

        @Override
        public Collection<String> getTabSuggestions(BukkitCommandCompletionContext context) {
            return List.of("true", "false");
        }

        @Override
        public EnergyFlag ofValue(String value) {
            return (EnergyFlag) new EnergyFlag().setValue(Boolean.parseBoolean(value));
        }
    }

    public static class InputsFlag extends CommandFlag<List<ItemStack>> {

        @Override
        public void apply(Player player, List<CommandFlag<?>> flags, SlimefunItem sfItem, Block block) {
            BlockMenu menu = BlockStorage.getInventory(block);
            int[] slots = menu.getPreset().getSlotsAccessedByItemTransport(ItemTransportFlow.INSERT);
            for (ItemStack input : this.value) {
                if (menu.pushItem(new ItemStack(input), slots) != null) {
                    break;
                }
            }
        }

        @Override
        public boolean canApply(SlimefunItem sfItem) {
            return this.value != null && !this.value.isEmpty() && Slimefun.getRegistry().getMenuPresets().containsKey(sfItem.getId());
        }

        @Override
        public Collection<String> getTabSuggestions(BukkitCommandCompletionContext context) {
            String input = context.getInput();
            if (input.isEmpty()) {
                return List.of("[");
            }

            char lastChar = input.charAt(input.length() - 1);
            if (lastChar == ']') {
                return Collections.emptyList();
            }

            List<String> inputs = new ArrayList<>();
            if (lastChar == ',') {
                inputs.addAll(generateBaseInputs(input, context.getPlayer().getWorld()));
            } else {
                String current;
                if (input.contains(",")) {
                    current = input.substring(input.lastIndexOf(",") + 1);
                } else {
                    current = input.substring(input.indexOf('[') + 1);
                }
                current = current.toUpperCase(Locale.ROOT);

                if (Utils.SLIMEFUN_ITEMS.contains(current) || Utils.MATERIALS.containsKey(current)) {
                    inputs.add(input + "]");
                    inputs.addAll(generateBaseInputs(input + ",", context.getPlayer().getWorld()));
                    return inputs;
                }

                String base = input.substring(0, input.length() - current.length());
                for (String slimefunItem : Utils.SLIMEFUN_ITEMS) {
                    if (slimefunItem.startsWith(current)) {
                        inputs.add(base + slimefunItem);
                    }
                }

                World world = context.getPlayer().getWorld();
                for (Material material : Utils.MATERIALS.values()) {
                    String name = material.name();
                    if (material.isEnabledByFeature(world) && name.startsWith(current)) {
                        inputs.add(base + name);
                    }
                }
            }

            return inputs;
        }

        private Collection<String> generateBaseInputs(String input, World world) {
            List<String> inputs = new ArrayList<>();
            for (String slimefunItem : Utils.SLIMEFUN_ITEMS) {
                inputs.add(input + slimefunItem + ",");
            }

            for (Material material : Utils.MATERIALS.values()) {
                if (material.isEnabledByFeature(world)) {
                    inputs.add(input + material.name() + ",");
                }
            }
            return inputs;
        }

        @Override
        public InputsFlag ofValue(String value) {
            String[] segments = value.substring(1, value.length() - 1).split(",");
            List<ItemStack> inputs = new ArrayList<>();
            for (String input : segments) {
                input = input.toUpperCase(Locale.ROOT);
                SlimefunItem slimefunItem = SlimefunItem.getById(input);
                Material material = Utils.MATERIALS.get(input);
                if (slimefunItem != null) {
                    inputs.add(new CustomItemStack(slimefunItem.getItem(), slimefunItem.getItem().getMaxStackSize()));
                } else if (material != null) {
                    inputs.add(new ItemStack(material, material.getMaxStackSize()));
                }
            }
            return (InputsFlag) new InputsFlag().setValue(inputs);
        }
    }

    public static class RefillInputsFlag extends CommandFlag<Boolean> {

        private RefillInputsTask task;

        @Override
        public void apply(Player player, List<CommandFlag<?>> flags, SlimefunItem sfItem, Block block) {
            if (task == null) {
                task = new RefillInputsTask(sfItem);

                for (CommandFlag<?> otherFlag : flags) {
                    if (otherFlag instanceof TimeoutFlag timeoutFlag) {
                        task.setTimeout(timeoutFlag.getValue());
                    } else if (otherFlag instanceof InputsFlag inputsFlag) {
                        task.setInputs(inputsFlag.getValue());
                    }
                }
            }

            task.addBlock(block);
        }

        @Override
        public boolean canApply(SlimefunItem sfItem) {
            return this.value != null && this.value && Slimefun.getRegistry().getMenuPresets().containsKey(sfItem.getId());
        }

        @Override
        public Collection<String> getTabSuggestions(BukkitCommandCompletionContext context) {
            return List.of("true", "false");
        }

        @Override
        public RefillInputsFlag ofValue(String value) {
            return (RefillInputsFlag) new RefillInputsFlag().setValue(Boolean.parseBoolean(value));
        }
    }

    public static class VoidOutputsFlag extends CommandFlag<Boolean> {

        private VoidOutputsTask task;

        @Override
        public void apply(Player player, List<CommandFlag<?>> flags, SlimefunItem sfItem, Block block) {
            if (task == null) {
                task = new VoidOutputsTask(sfItem);

                for (CommandFlag<?> otherFlag : flags) {
                    if (otherFlag instanceof TimeoutFlag timeoutFlag) {
                        task.setTimeout(timeoutFlag.getValue());
                    }
                }
            }

            task.addBlock(block);
        }

        @Override
        public boolean canApply(SlimefunItem sfItem) {
            return this.value != null && this.value && Slimefun.getRegistry().getMenuPresets().containsKey(sfItem.getId());
        }

        @Override
        public Collection<String> getTabSuggestions(BukkitCommandCompletionContext context) {
            return List.of("true", "false");
        }

        @Override
        public VoidOutputsFlag ofValue(String value) {
            return (VoidOutputsFlag) new VoidOutputsFlag().setValue(Boolean.parseBoolean(value));
        }
    }

    public static class TimeoutFlag extends CommandFlag<Integer> {

        @Override
        public void apply(Player player, List<CommandFlag<?>> flags, SlimefunItem sfItem, Block block) {

        }

        @Override
        public boolean canApply(SlimefunItem sfItem) {
            return sfItem != null && this.value != null && this.value > 0;
        }

        @Override
        public Collection<String> getTabSuggestions(BukkitCommandCompletionContext context) {
            return List.of("30s", "5m", "1h");
        }

        @Override
        public TimeoutFlag ofValue(String value) {
            int multiplier = switch(value.charAt(value.length() -1)) {
                case 's' -> 20;
                case 'm' -> 20 * 60;
                case 'h' -> 20 * 60 * 60;
                default -> 0;
            };

            return (TimeoutFlag) new TimeoutFlag().setValue(multiplier * Integer.parseInt(value.substring(0, value.length() - 1)));
        }
    }
}
