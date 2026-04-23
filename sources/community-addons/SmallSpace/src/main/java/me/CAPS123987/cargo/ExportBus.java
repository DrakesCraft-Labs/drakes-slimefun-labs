package me.CAPS123987.cargo;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft-labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import com.github.drakescraft-labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft-labs.slimefun4.utils.ChestMenuUtils;
import me.CAPS123987.items.Items;
import me.CAPS123987.smallspace.ETInventoryBlock;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenuPreset;

public class ExportBus extends SimpleSlimefunItem<BlockTicker> implements ETInventoryBlock{
	int[] border = {};
	int[] inputBorder = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44};
	int[] outputBorder = {};
	int[] output = {};
	static final int[] input = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34};
	
	public ExportBus() {
		super(Items.smallSpace, Items.EXPORT_BUS, RecipeType.NULL, new ItemStack[0]);
		this.setHidden(true);
		addItemHandler(onBreak());
		createPreset(this, this::constructMenu);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getInputSlots() {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return output;
	}

	@Override
	public BlockTicker getItemHandler() {
		// TODO Auto-generated method stub
		return new BlockTicker() {

			@Override
			public boolean isSynchronized() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				// TODO Auto-generated method stub
				
				String x = BlockStorage.getLocationInfo(b.getLocation(),"x");
				String y = BlockStorage.getLocationInfo(b.getLocation(),"y");
				String z = BlockStorage.getLocationInfo(b.getLocation(),"z");
				String world = BlockStorage.getLocationInfo(b.getLocation(),"world");
				Location l = new Location(Bukkit.getWorld(world),Double.valueOf(x),Double.valueOf(y),Double.valueOf(z));
				
				BlockMenu from = BlockStorage.getInventory(b);
				BlockMenu to = BlockStorage.getInventory(l);
				
				for(int i : input) {
					ItemStack it = from.getItemInSlot(i);
					if(!(it == null || it == new ItemStack(Material.AIR))){
						ItemStack over = to.pushItem(it, SpaceInterface.outputs);
						if(over == null) {
							from.replaceExistingItem(i, new ItemStack(Material.AIR));
						}
					}
					
				}
				
				
			}
		};
		
	}
	public BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {

			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				e.setCancelled(true);
				
			}
        	
        };
	}
	private void constructMenu(BlockMenuPreset preset) {
    	
    	
        for (int i : border) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : inputBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : outputBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {

                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor,
                                       ClickAction action) {
                    return cursor == null || cursor.getType() == Material.AIR;
                }
            });
       }
}
	

}
