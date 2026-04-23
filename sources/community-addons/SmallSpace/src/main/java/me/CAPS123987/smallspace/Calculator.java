package me.CAPS123987.smallspace;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import me.CAPS123987.cargo.SpaceInterface;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;

public class Calculator {
	static FileConfiguration cfg = SmallSpace.instance.getConfig();
	
	public static Location getLoc(String id) {
		
		long x = -cfg.getLong("radius");
		long y = 3;
		int temp = 0;
		int down = 240;
		if(cfg.getBoolean("go_to_minus_on_y")){
			y=-63;
			temp = 62;
			down=312;
		}
		long z = -cfg.getLong("radius");
		long value = Integer.parseInt(id.replaceAll("[^0-9]", ""));
		long tempcalc = value*24;
		
		y = tempcalc-temp;
		while(y>250) {
			y = y-down;
			x = x + 32;
		}
		while(x>cfg.getLong("radius")) {
			x = x-cfg.getLong("radius");
			z = z + 32;
		}
		while(z>cfg.getLong("radius")) {
			z = z-cfg.getLong("radius");
			x = x + 32;
		}
		
		
		return new Location(Bukkit.getWorld("SmallSpace"),x,y,z);
	}
	public static void setRegi(Location l,double area) {
		
		World world = Bukkit.getWorld("SmallSpace");
		world.getBlockAt(l).setType(Material.AIR);
		world.loadChunk(l.getChunk());
		
		double oldx = l.getX();
		double oldy = l.getY();
		double oldz = l.getZ();
		
		for(double x = 0.0; x!=area; x=x+1.0) {
			for(double y = 0.0; y!=area; y=y+1.0) {
				for(double z = 0.0; z!=area; z=z+1.0) {
					
					Location loc = new Location(world,x+oldx,y+oldy,z+oldz);
					world.getBlockAt(loc).setType(Material.AIR);
				}
			}
		}
		
	}
	
	public static List<String> playersGet(String s) {
		List<String> options = new ArrayList<>();
		
		String text = "";
		for(int i = 0; s.length() != i; i++) {
			
			if(s.charAt(i) != ';') {
				text = text+s.charAt(i);
			}else {
				options.add(text);
				text = "";
			}
			
		}
		return options;
		
	}
	public static void SfRemove(Block b,List<Vector> blocks) {
    	
    	for (Vector entry : blocks) {
			
			Location loc = b.getLocation().add(entry);
			
			if(BlockStorage.hasBlockInfo(loc)) {
				SlimefunItem item1 = BlockStorage.check(loc);
				
				if ((item1 instanceof SpaceInterface)) {
					
					SpaceInterface.delBus(loc.getBlock());
					
					Object[] colec = item1.getDrops().toArray();
					
					BlockMenu menu = BlockStorage.getInventory(loc);
					menu.dropItems(loc, SpaceInterface.inputs);
					menu.dropItems(loc, SpaceInterface.outputs);
					
					loc.getBlock().getWorld().dropItemNaturally(loc, (ItemStack) colec[0]);
					loc.getBlock().setType(Material.AIR);;

					BlockStorage.clearBlockInfo(loc);
				}
				
			}
			
		}
    }
	public static Vector fac(String s) {
		switch(s){
			case "SOUTH":
				return new Vector(0,0,-1);
			case "EAST":
				return new Vector(-1,0,0);
			case "NORTH":
				return new Vector(0,0,1);
			case "WEST":
				return new Vector(1,0,0);
			default:
				return null;
		}
		
	}
	public static String side(Vector v) {
		switch(v.toString()){
			case "0.0,0.0,-1.0":
				return "SOUTH";
			case "-1.0,0.0,0.0":
				return "EAST";
			case "0.0,0.0,1.0":
				return "NORTH";
			case "1.0,0.0,0.0":
				return "WEST";
			default:
				return null;
		}
		
	}
	public static int size(String s) {
		int i = Integer.parseInt(s.replaceAll("[^0-9]", ""));
		switch(i) {
			case 1:
				return 3;
			case 2:
				return 6;
			case 3:
				return 9;
			case 4:
				return 12;
			default:
				return 0;
		}
	}
}
