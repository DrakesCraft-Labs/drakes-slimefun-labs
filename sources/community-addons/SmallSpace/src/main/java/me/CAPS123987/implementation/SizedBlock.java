package me.CAPS123987.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.github.drakescraft-labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft-labs.slimefun4.api.events.BlockPlacerPlaceEvent;
import com.github.drakescraft-labs.slimefun4.api.items.ItemHandler;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft-labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import com.github.drakescraft-labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft-labs.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import me.CAPS123987.cargo.SpaceInterface;
import me.CAPS123987.dimension.SmallSpaceDim;
import me.CAPS123987.items.Items;
import me.CAPS123987.smallspace.Calculator;
import me.CAPS123987.smallspace.SmallSpace;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.hover.content.Item;
import com.github.drakescraft-labs.slimefun4.api.events.PlayerRightClickEvent;
import org.bukkit.event.player.PlayerEvent;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockUseHandler;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;

public class SizedBlock extends SimpleSlimefunItem<BlockTicker> implements EnergyNetComponent{
	private final int Tier;
	private int decrement = 0;
	public List<Vector> blocks = new ArrayList<>();;
	
	
	public SizedBlock(int i, SlimefunItemStack item, ItemStack[] recipe) {
		super(Items.smallSpace, item, RecipeType.ENHANCED_CRAFTING_TABLE,
				recipe);
		blocks.add(new Vector(1,0,0));
		blocks.add(new Vector(-1,0,0));
		blocks.add(new Vector(0,0,1));
		blocks.add(new Vector(0,0,-1));
		blocks.add(new Vector(0,1,0));
		blocks.add(new Vector(0,-1,0));
		addItemHandler(BlockPlaceHandler());
		addItemHandler(BlockBreakHandler());
		addItemHandler(BlockUseHandler());
		this.Tier=i;
	}

	@Override
	public EnergyNetComponentType getEnergyComponentType() {
		// TODO Auto-generated method stub
		return EnergyNetComponentType.CONSUMER;
	}
	
	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return 2048;
	}
	
	
	public BlockUseHandler BlockUseHandler() {
		return new BlockUseHandler() {

			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				
				
				Location loc = Calculator.getLoc(BlockStorage.getLocationInfo(e.getClickedBlock().get().getLocation(), "name"));
				String srt = BlockStorage.getLocationInfo(e.getClickedBlock().get().getLocation(), "Tier");
				int value = Integer.parseInt(srt.replaceAll("[^0-9]", ""));
				World world = Bukkit.getWorld("SmallSpace");
				String owner = BlockStorage.getLocationInfo(e.getClickedBlock().get().getLocation(), "owner");
				Location newloc = new Location(world,loc.getX()+0.5,loc.getY(),loc.getZ()+0.5);
				Location newloc2 = new Location(world,loc.getX()+0.5,loc.getY()+1,loc.getZ()+0.5);
				Location newloc22 = new Location(world,loc.getX()+0.5,loc.getY()+2,loc.getZ()+0.5);
				Location newloc222 = new Location(world,loc.getX()+0.5,loc.getY()+3,loc.getZ()+0.5);
				Location newloc3 = new Location(world,loc.getX()+0.5,loc.getY()-1,loc.getZ()+0.5);
				
				if(!(e.getPlayer().getName().equals(owner)|| e.getPlayer().hasPermission("SmallSpace.admin") || Calculator.playersGet(BlockStorage.getLocationInfo(e.getClickedBlock().get().getLocation(), "Players")).contains(e.getPlayer().getName()))) {
					e.getPlayer().sendMessage(ChatColor.RED+"You are not the owner or member of this space!");
					return;
				}
				
				if(e.getPlayer().isSneaking()) {
					if(loc.getBlock().getType() == Material.BEDROCK) {
						e.getPlayer().sendMessage(ChatColor.RED+"Your space was never initialized try right click");
						return;
					}
					if(loc.getBlock().getType() == Material.BARRIER) {
						e.getPlayer().sendMessage(ChatColor.DARK_RED+"Sorry your space is disabled");
						return;
					}
					
					newloc2.getBlock().setType(Material.AIR);
					newloc22.getBlock().setType(Material.AIR);
					newloc222.getBlock().setType(Material.AIR);
					e.getPlayer().sendMessage("Blocks were removed");
				}else {
					if(loc.getBlock().getType() == Material.BEDROCK) {
						switch(value) {
						case 1:
							Calculator.setRegi(loc,3.0);
							break;
						case 2:
							Calculator.setRegi(loc,6.0);
							break;
						case 3:
							Calculator.setRegi(loc,9.0);
							break;
						case 4:
							Calculator.setRegi(loc,12.0);
							break;
						default:
							break;
						}
						world.getBlockAt(newloc3).setType(Material.GOLD_BLOCK);
						
						BlockStorage.store(newloc3.getBlock(), "TELEPORT");
						
						BlockStorage.addBlockInfo(newloc3.getBlock(), "tpX",String.valueOf(e.getClickedBlock().get().getLocation().getX()));
						BlockStorage.addBlockInfo(newloc3.getBlock(), "tpY", String.valueOf(e.getClickedBlock().get().getLocation().getY()));
						BlockStorage.addBlockInfo(newloc3.getBlock(), "tpZ", String.valueOf(e.getClickedBlock().get().getLocation().getZ()));
						BlockStorage.addBlockInfo(newloc3.getBlock(), "world", String.valueOf(e.getClickedBlock().get().getLocation().getWorld().getName()));
						
					}
					
					if(newloc.getBlock().getType()==Material.BARRIER) {
						e.getPlayer().sendMessage(ChatColor.DARK_RED+"Sorry your space is disabled");
						return;
					}
					if(newloc2.getBlock().getType()==Material.AIR && newloc22.getBlock().getType()==Material.AIR&& newloc222.getBlock().getType()==Material.AIR) {
						//if player is owner or admin
						
						e.getPlayer().teleport(newloc);
						
					}else {
						e.getPlayer().sendMessage(ChatColor.RED+"Spawn Location in your space is obstructed! To remove Blocks press Shift and right click this again");
						return;
					}
					
				}
			}

			
		};
	}

	
	public BlockBreakHandler BlockBreakHandler() {
		return new BlockBreakHandler(false, false) {

			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				Block b = e.getBlock();
				String name = BlockStorage.getLocationInfo(b.getLocation(), "name");
				String owner = BlockStorage.getLocationInfo(e.getBlock().getLocation(), "owner");
				
				if(name == "null") {
					return;
				}
				
				//if player is owner
				
				if(!e.getPlayer().getName().equals(owner)) {
					//if player is admin
					if(e.getPlayer().hasPermission("SmallSpace.admin")) {
						
					}else {
						e.getPlayer().sendMessage(ChatColor.RED+"You are not the owner of this space!");
						e.setCancelled(true);
						return;
					}
				}
				//break space interface
				Calculator.SfRemove(b,blocks);
				
				e.getBlock().getWorld().setChunkForceLoaded(e.getBlock().getLocation().getChunk().getX(), e.getBlock().getLocation().getChunk().getZ(), false);
				
				//drop item with custom lore
				ItemStack item2 =SizedBlock.this.getItem().clone();
				e.setDropItems(false);
				
				List<String> lore = new ArrayList<String>();
				lore.add(name);
				ItemMeta im=item2.getItemMeta();
				im.setLore(lore);
				item2.setItemMeta(im);
				
				e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item2);
				
				
			}
			
		};
	}
	
	
	public BlockPlaceHandler BlockPlaceHandler() {
		return new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				e.getBlock().getWorld().setChunkForceLoaded(e.getBlock().getLocation().getChunk().getX(), e.getBlock().getLocation().getChunk().getZ(), true);
				// TODO Auto-generated method stub
				BlockStorage.addBlockInfo(e.getBlock().getLocation(), "Tier", String.valueOf(getTier())) ;
				if(e.getItemInHand().getItemMeta().getLore().get(0).equals("§4Put to Block Assigner")) {
					e.setCancelled(true);
					BlockStorage.clearBlockInfo(e.getBlock());
					e.getPlayer().sendMessage("You need to put this block to Block Block Assigner");
					return;
					}
					BlockStorage.addBlockInfo(e.getBlock().getLocation(), "Players", "");
					BlockStorage.addBlockInfo(e.getBlock().getLocation(), "name", e.getItemInHand().getItemMeta().getLore().get(0));
					BlockStorage.addBlockInfo(e.getBlock().getLocation(), "owner", e.getPlayer().getName());
					
					Location loc = Calculator.getLoc(BlockStorage.getLocationInfo(e.getBlock().getLocation(), "name"));
					World world = Bukkit.getWorld("SmallSpace");
					Location newloc3 = new Location(world,loc.getX()+0.5,loc.getY()-1,loc.getZ()+0.5);
					world.getBlockAt(newloc3).setType(Material.GOLD_BLOCK);
					
					BlockStorage.store(newloc3.getBlock(), "TELEPORT");
					
					BlockStorage.addBlockInfo(newloc3.getBlock(), "tpX",String.valueOf(e.getBlock().getLocation().getX()));
					BlockStorage.addBlockInfo(newloc3.getBlock(), "tpY", String.valueOf(e.getBlock().getLocation().getY()));
					BlockStorage.addBlockInfo(newloc3.getBlock(), "tpZ", String.valueOf(e.getBlock().getLocation().getZ()));
					BlockStorage.addBlockInfo(newloc3.getBlock(), "world", String.valueOf(e.getBlock().getLocation().getWorld().getName()));
					
					
				}
			};
	}
	
	
	
	@Override
	public BlockTicker getItemHandler() {
		
		return new BlockTicker() {
	    	
			@Override
            // Fires first!! The method tick() fires after this
            public void uniqueTick() {
                
				
                if (decrement == 2) {
                    decrement = 0;
                    return;
                }
                decrement++;

            }
	        
	        public void tick(Block b, SlimefunItem sf, Config data) {
	        	if (decrement != 0) {
                    return;
                }
	        	
	        	
	        	
	        	if(getTier()*256<=getCharge(b.getLocation())) {
	        		removeCharge(b.getLocation(), getTier()*256);
	        		return;
	        	}
	        	
	        	
	        	int size = Calculator.size(BlockStorage.getLocationInfo(b.getLocation(),"Tier"));
	        	Location loc = Calculator.getLoc(BlockStorage.getLocationInfo(b.getLocation(),"name"))	;
	        	Vector v = new Vector(size/2,size/2,size/2);
	        	Location loc2 = loc.add(v);
	        	
	        	
	        	
	        	Collection<Entity> Entity = Bukkit.getWorld("SmallSpace").getNearbyEntities(loc2, size/2, size/2, size/2);
	        	if(Entity.isEmpty()) {
	        		return;
	        	}
	        	for(Entity e : Entity) {
	        		if(e instanceof Player) {
	        			Player p = (Player) e;
	        			p.sendMessage(ChatColor.MAGIC+"III"+ChatColor.RESET+ChatColor.DARK_RED+"ERROR"+ChatColor.RESET+ChatColor.MAGIC+"III"+ChatColor.RESET+ChatColor.WHITE+" NO ENERGY");
	        			p.setHealth(0.0);
	        			
	        		}
	        	}
	        	
	        	
	        }

			@Override
			public boolean isSynchronized() {
				// TODO Auto-generated method stub
				return true;
			}
		};
	}
	
	public int getTier() {
		return Tier;
	}
	

	
}
