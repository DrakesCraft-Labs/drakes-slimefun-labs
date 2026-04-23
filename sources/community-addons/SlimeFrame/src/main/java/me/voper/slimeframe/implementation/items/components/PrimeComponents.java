package me.voper.slimeframe.implementation.items.components;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.utils.SlimefunUtils;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.implementation.SFrameStacks;
import me.voper.slimeframe.implementation.SFrameTheme;
import me.voper.slimeframe.implementation.items.relics.RelicItemStack;
import me.voper.slimeframe.utils.Colors;
import me.voper.slimeframe.utils.HeadTextures;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.ChatColor;

@Getter
@Setter
@Accessors(chain = true)
@ParametersAreNonnullByDefault
public class PrimeComponents {

    public static final Map<SlimefunItemStack, PrimeComponents> COMPONENTS_MAP = new HashMap<>();

    // Machines & Generators components
    private SlimefunItemStack controlUnit;
    private SlimefunItemStack powerCell;
    private SlimefunItemStack coreModule;

    public PrimeComponents(SlimefunItemStack item) {
        controlUnit = createControlUnit(item);
        powerCell = createPowerCell(item);
        coreModule = createCoreModule(item);
        COMPONENTS_MAP.put(item, this);
    }

    public PrimeComponents() {
    }

    @Nonnull
    public static SlimefunItemStack createControlUnit(SlimefunItemStack item) {
        if (COMPONENTS_MAP.get(item) != null && COMPONENTS_MAP.get(item).getControlUnit() != null) {
            return COMPONENTS_MAP.get(item).getControlUnit();
        }

        String displayName = ChatColor.stripColor(item.getDisplayName()).replace("Prime ", "");
        String itemId = item.getItemId();

        SlimefunItemStack controlUnit = SFrameTheme.sfStackFromTheme(
                itemId + "_CONTROL_UNIT",
                HeadTextures.MACHINE_CONTROL,
                SFrameStacks.PRIME_COMPONENTS_THEME.withNameColor(Colors.BRONZE),
                displayName + " Control",
                "Empowering Prime-tier machines with seamless",
                "coordination and precision, the Control Unit",
                "is a vital nexus of technological prowess",
                "Required to craft: " + item.getDisplayName()
        );

        PrimeComponents primeComponents = COMPONENTS_MAP.get(item);
        if (primeComponents == null) {
            COMPONENTS_MAP.put(item, new PrimeComponents().setControlUnit(controlUnit));
        } else if (primeComponents.getControlUnit() == null) {
            COMPONENTS_MAP.put(item, primeComponents.setControlUnit(controlUnit));
        }
        return controlUnit;
    }

    @Nonnull
    public static SlimefunItemStack createPowerCell(SlimefunItemStack item) {
        if (COMPONENTS_MAP.get(item) != null && COMPONENTS_MAP.get(item).getPowerCell() != null) {
            return COMPONENTS_MAP.get(item).getPowerCell();
        }

        String displayName = ChatColor.stripColor(item.getDisplayName()).replace("Prime ", "");
        String itemId = item.getItemId();

        SlimefunItemStack powerCell = SFrameTheme.sfStackFromTheme(
                itemId + "_POWER_CELL",
                HeadTextures.MACHINE_POWER_CELL,
                SFrameStacks.PRIME_COMPONENTS_THEME.withNameColor(Colors.SILVER),
                displayName + " Power Cell",
                "The Power Cell, an essential component for",
                "Prime-tier machines, holds boundless energy",
                "within its compact form, fueling the pinnacle",
                "of technological achievements",
                "Required to craft: " + item.getDisplayName()
        );

        PrimeComponents primeComponents = COMPONENTS_MAP.get(item);
        if (primeComponents == null) {
            COMPONENTS_MAP.put(item, new PrimeComponents().setPowerCell(powerCell));
        } else if (primeComponents.getPowerCell() == null) {
            COMPONENTS_MAP.put(item, primeComponents.setPowerCell(powerCell));
        }
        return powerCell;
    }

    @Nonnull
    public static SlimefunItemStack createCoreModule(SlimefunItemStack item) {
        if (COMPONENTS_MAP.get(item) != null && COMPONENTS_MAP.get(item).getCoreModule() != null) {
            return COMPONENTS_MAP.get(item).getCoreModule();
        }

        String displayName = ChatColor.stripColor(item.getDisplayName()).replace("Prime ", "");
        String itemId = item.getItemId();

        SlimefunItemStack coreModule = SFrameTheme.sfStackFromTheme(
                itemId + "_CORE",
                HeadTextures.MACHINE_CORE,
                SFrameStacks.PRIME_COMPONENTS_THEME.withNameColor(Colors.GOLD_2),
                displayName + " Core Module",
                "The Core Module, a crucial component in",
                "crafting Prime-tier machines, unlocks true",
                "potential with advanced functionalities",
                "and intricate circuitry",
                "Required to craft: " + item.getDisplayName()
        );

        PrimeComponents primeComponents = COMPONENTS_MAP.get(item);
        if (primeComponents == null) {
            COMPONENTS_MAP.put(item, new PrimeComponents().setCoreModule(coreModule));
        } else if (primeComponents.getCoreModule() == null) {
            COMPONENTS_MAP.put(item, primeComponents.setCoreModule(coreModule));
        }
        return coreModule;
    }

    public static void registerAll(SlimeFrame plugin) {
        COMPONENTS_MAP.forEach((slimefunItemStack, components) -> {
            try {
                register(components, plugin);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private static void register(PrimeComponents components, SlimeFrame plugin) throws IllegalAccessException {
        Component coreModuleSF = new Component(components.getCoreModule());
        Component powerCellSF = new Component(components.getPowerCell());
        Component controlUnitSF = new Component(components.getControlUnit());

        List<ItemStack> relicsCoreModule = new ArrayList<>();
        List<ItemStack> relicsPowerCell = new ArrayList<>();
        List<ItemStack> relicsControlUnit = new ArrayList<>();

        for (Field field : SFrameStacks.class.getDeclaredFields()) {
            if (field.getType() != RelicItemStack.class) continue;
            RelicItemStack relic = (RelicItemStack) field.get(null);

            for (SlimefunItemStack common : relic.getCommonDrops()) {
                if (!SlimefunUtils.isItemSimilar(components.getControlUnit(), common, true)) continue;
                relicsControlUnit.add(relic);
            }

            for (SlimefunItemStack uncommon : relic.getUncommonDrops()) {
                if (!SlimefunUtils.isItemSimilar(components.getPowerCell(), uncommon, true)) continue;
                relicsPowerCell.add(relic);
            }

            if (SlimefunUtils.isItemSimilar(components.getCoreModule(), relic.getRareDrop(), true)) {
                relicsCoreModule.add(relic);
            }
        }

        coreModuleSF.setRelics(relicsCoreModule);
        powerCellSF.setRelics(relicsPowerCell);
        controlUnitSF.setRelics(relicsControlUnit);

        coreModuleSF.register(plugin);
        powerCellSF.register(plugin);
        controlUnitSF.register(plugin);
    }

}
