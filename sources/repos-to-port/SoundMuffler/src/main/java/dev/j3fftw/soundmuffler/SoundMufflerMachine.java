package dev.j3fftw.soundmuffler;

import dev.drake.dough.items.CustomItemStack;

import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.api.items.ItemHandler;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.protection.Interaction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class SoundMufflerMachine extends SlimefunItem implements EnergyNetComponent {

    public static final int DISTANCE = 8;
    private static final int[] border = {1, 2, 3, 4, 5, 6, 7};
    private static final String name = "&3Sound Muffler";
    private static final String id = "SOUND_MUFFLER";

    public SoundMufflerMachine() {
        super(SoundMuffler.SOUND_MUFFLER,
            new SlimefunItemStack(id, Material.WHITE_CONCRETE, name,
                "", "&7Muffles all sound in a", "&78 block radius", "", "&e\u26A1 Requires power to use"
            ),
            id,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.WHITE_WOOL), SlimefunItems.STEEL_PLATE, new ItemStack(Material.WHITE_WOOL),
                SlimefunItems.STEEL_PLATE, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.STEEL_PLATE,
                new ItemStack(Material.WHITE_WOOL), SlimefunItems.STEEL_PLATE, new ItemStack(Material.WHITE_WOOL)
            }

        );
        addItemHandler(onPlace());

        new BlockMenuPreset(id, name) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(final BlockMenu menu, final Block b) {
                int volume = 10;
                boolean enabled = false;
                if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "enabled") == null) {
                    BlockStorage.addBlockInfo(b, "volume", String.valueOf(volume));
                    BlockStorage.addBlockInfo(b, "enabled", String.valueOf(false));

                } else {
                    volume = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "volume"));
                    enabled = Boolean.parseBoolean(BlockStorage.getLocationInfo(b.getLocation(), "enabled"));
                }

                menu.replaceExistingItem(8, new CustomItemStack((enabled ? Material.REDSTONE : Material.GUNPOWDER),
                    "&7Enabled: " + (enabled ? "&a\u2714" : "&4\u2718"), "", "&e> Click to enable this Machine"));
                menu.replaceExistingItem(0, new CustomItemStack(Material.PAPER,
                    "&eVolume: &b" + volume,
                    "&7Valid value range: 0-100",
                    "&7L-click: -10",
                    "&7R-click: +10",
                    "&7With shift held: +/-1"));

                final int finalVolume = volume;
                menu.addMenuClickHandler(0, (p, arg1, arg2, arg3) -> {
                    int newVolume;

                    if (arg3.isRightClicked()) {
                        if (arg3.isShiftClicked())
                            newVolume = Math.min(finalVolume + 1, 100);
                        else
                            newVolume = Math.min(finalVolume + 10, 100);
                    } else {
                        if (arg3.isShiftClicked())
                            newVolume = Math.max(finalVolume - 1, 0);
                        else
                            newVolume = Math.max(finalVolume - 10, 0);
                    }

                    BlockStorage.addBlockInfo(b, "volume", String.valueOf(newVolume));
                    newInstance(menu, b);
                    return false;
                });
                menu.addMenuClickHandler(8, (p, arg1, arg2, arg3) -> {
                    final String isEnabled = BlockStorage.getLocationInfo(b.getLocation(), "enabled");
                    if (isEnabled != null && isEnabled.equals("true"))
                        BlockStorage.addBlockInfo(b, "enabled", "false");
                    else
                        BlockStorage.addBlockInfo(b, "enabled", "true");
                    newInstance(menu, b);
                    return false;
                });
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                    || SlimefunPlugin.getProtectionManager()
                    .hasPermission(p, b, Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };


        registerBlockHandler(id, (p, b, item, reason) -> {
            BlockStorage.clearBlockInfo(b);
            return true;
        });
    }

    protected void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " "),
                (player, i1, itemStack, clickAction) -> false);
        }
    }


    private ItemHandler onPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlock(), "enabled", "false");
                BlockStorage.addBlockInfo(e.getBlock(), "volume", "10");
            }
        };
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    public int getEnergyConsumption() {
        return 8;
    }

    @Override
    public int getCapacity() {
        return 352;
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                try {
                    SoundMufflerMachine.this.tick(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void uniqueTick() {
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });

    }

    private void tick(Block b) {
        if ((BlockStorage.getLocationInfo(b.getLocation(), "enabled").equals("true"))
            && (getCharge(b.getLocation()) > 8)) {
            removeCharge(b.getLocation(), getEnergyConsumption());
        }
    }
}

