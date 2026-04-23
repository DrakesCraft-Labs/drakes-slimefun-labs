package me.CAPS123987.cargo;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.libraries.dough.skins.PlayerHead;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import me.CAPS123987.implementation.SizedBlock;
import me.CAPS123987.items.Items;
import me.CAPS123987.smallspace.Calculator;
import me.CAPS123987.smallspace.ETInventoryBlock;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;

public class SpaceInterface extends SimpleSlimefunItem<BlockTicker> implements ETInventoryBlock{
	public final static int[] inputs = {9,10,11,18,19,20,27,28,29};
	public final static int[] outputs = {15,16,17,24,25,26,33,34,35};
	public final int[] border = {0,1,2,3,4,5,6,7,8,13,22,31,36,37,38,39,40,41,42,43,44};
	public final int[] inputBorder = {12,21,30};
	public final int[] outputBorder = {14,23,32};
	
	public SpaceInterface() {
		super(Items.smallSpace, Items.SPACE_INTERFACE, RecipeType.ENHANCED_CRAFTING_TABLE, Items.recipe_SPACE_INTERFACE);
		createPreset(this, this::constructMenu);
		addItemHandler(onBreak(), BlockPlaceHandler());
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getInputSlots() {
		// TODO Auto-generated method stub
		return inputs;
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return outputs;
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
				
			}
	
		};
	}
	public BlockPlaceHandler BlockPlaceHandler() {
		return new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				Block b1 = e.getBlock();
				
				if(!b1.getType().equals(Material.PLAYER_WALL_HEAD)) {
					BlockStorage.clearBlockInfo(b1);
					e.setCancelled(true);
				}
				Vector v = null;
				Vector v1 = null;
				try {
					Directional bmeta = (Directional) b1.getBlockData();
					v = Calculator.fac(bmeta.getFacing().toString()) ;
					v1 = Calculator.fac(bmeta.getFacing().toString()) ;
				}catch(Exception e1) {
					
				}
				Location loc = b1.getLocation().add(v);
				
				SlimefunItem item = BlockStorage.check(loc);
				Block sizBlo = loc.getBlock();
				
				if(!(item instanceof SizedBlock)) {
					BlockStorage.clearBlockInfo(b1);
					e.setCancelled(true);
				}
				e.getBlock().getWorld().setChunkForceLoaded(b1.getLocation().getChunk().getX(), b1.getLocation().getChunk().getZ(), true);
				String Tier = BlockStorage.getLocationInfo(sizBlo.getLocation(),"Tier");
				String id = BlockStorage.getLocationInfo(sizBlo.getLocation(),"name");
				
				Location in = inloc(Tier, id, v);
				Location out = outloc(Tier, id, v1);
				
				in.getBlock().setType(Material.STONE);
				out.getBlock().setType(Material.MAGMA_BLOCK);
				
				BlockStorage.store(in.getBlock(), "IMPORT_BUS");
				BlockStorage.store(out.getBlock(), "EXPORT_BUS");
				
				BlockStorage.addBlockInfo(in.getBlock(), "x", String.valueOf(b1.getLocation().getX()));
				BlockStorage.addBlockInfo(in.getBlock(), "y", String.valueOf(b1.getLocation().getY()));
				BlockStorage.addBlockInfo(in.getBlock(), "z", String.valueOf(b1.getLocation().getZ()));
				BlockStorage.addBlockInfo(in.getBlock(), "world", String.valueOf(b1.getLocation().getWorld().getName()));
				
				
				BlockStorage.addBlockInfo(out.getBlock(), "x", String.valueOf(b1.getLocation().getX()));
				BlockStorage.addBlockInfo(out.getBlock(), "y", String.valueOf(b1.getLocation().getY()));
				BlockStorage.addBlockInfo(out.getBlock(), "z", String.valueOf(b1.getLocation().getZ()));
				BlockStorage.addBlockInfo(out.getBlock(), "world", String.valueOf(b1.getLocation().getWorld().getName()));
				
			}
			
		};
	}
	
	public static Location inloc(String Tier, String id, Vector v) {
		
		Location corner = Calculator.getLoc(id);
		int size = Calculator.size(Tier);
		
		Location center = corner.add((size/2)-0.5, (size/2)-0.5, (size/2)-0.5);
		
		Vector blo = v.multiply((size/2)+0.5);
		
		center.add(blo);
		
		return center;	
	}
	public static Location outloc(String Tier, String id, Vector v) {
		
		Location corner = Calculator.getLoc(id);
		int size = Calculator.size(Tier);

		Location center = corner.add((size/2)+0.0, (size/2)+0.0, (size/2)+0.0);
		
		Vector blo = v.multiply((size/2)+0.5);
		
		center.add(blo);
		
		
		return center;	
	}
	
	
	public BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {

            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
            	
                Block b = e.getBlock();
                BlockMenu inv = BlockStorage.getInventory(b);

                e.getBlock().getWorld().setChunkForceLoaded(b.getLocation().getChunk().getX(), b.getLocation().getChunk().getZ(), false);
                
                if (inv != null) {
                    inv.dropItems(b.getLocation(), getInputSlots());
                    inv.dropItems(b.getLocation(), getOutputSlots());
                }
                delBus(b);
                
				
                
            }
        };
    }
	
	
	
	public static void delBus(Block b) {
		Directional bmeta = (Directional) b.getBlockData();
		Vector v = Calculator.fac(bmeta.getFacing().toString()) ;
		Vector v1 = Calculator.fac(bmeta.getFacing().toString()) ;
		Location loc = b.getLocation().add(v);
		
		Block sizBlo = loc.getBlock();
		
		b.getWorld().setChunkForceLoaded(b.getLocation().getChunk().getX(), b.getLocation().getChunk().getZ(), false);
		String Tier = BlockStorage.getLocationInfo(sizBlo.getLocation(),"Tier");
		String id = BlockStorage.getLocationInfo(sizBlo.getLocation(),"name");
		
		Location in = inloc(Tier, id, v);
		Location out = outloc(Tier, id, v1);
		
		
		BlockMenu menu1 = BlockStorage.getInventory(in.getBlock());
		BlockMenu menu2 = BlockStorage.getInventory(out.getBlock());
		
		
		
		menu1.dropItems(in, ImportBus.output);
		menu2.dropItems(out, ExportBus.input);
		
		in.getBlock().setType(Material.BEDROCK);
		out.getBlock().setType(Material.BEDROCK);
		
		BlockStorage.clearBlockInfo(in.getBlock());
		BlockStorage.clearBlockInfo(out.getBlock());
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
