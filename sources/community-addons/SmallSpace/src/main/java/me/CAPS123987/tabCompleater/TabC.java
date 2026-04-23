package me.CAPS123987.tabCompleater;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.CAPS123987.smallspace.Calculator;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;

public class TabC implements TabCompleter{
	
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		
		Calculator calc = new Calculator();
		List<String> options1 = new ArrayList<>();
		options1.add("memberAdd");
		options1.add("memberRemove");
		options1.add("help");
		
		//only for admins
		
    	if(args.length == 1) {
    		if(!sender.hasPermission("SmallSpace.admin")) {
    			return options1;
    		}
    			options1.add("tptoId");
    			options1.add("blockId");
    			options1.add("unblockId");
    			options1.add("teleportRemove");
    			return options1;
    	
    	}
    	if(args.length == 2 && !args[0].equals("memberAdd") && !args[0].equals("memberRemove")) {
			List<String> options = new ArrayList<>();
			options.add("<ID>");
			return options;
		}
    	
    	if(args.length == 2 &&  args[0].equals("memberRemove")) {
			
	    	final Player pp = (Player) sender;
	    	try {
	    		return calc.playersGet(BlockStorage.getLocationInfo(pp.getTargetBlockExact(20).getLocation(), "Players"));
	    	}catch(Exception e) {
	    		return null;
	    	}
		}
    	
		if((args.length == 2 && args[0].equals("memberAdd"))){
			List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
			List<String> options = new ArrayList<>();
	    	for(Player p : list) {
	    		options.add(p.getName());
	    	}
	    	
	    	return options;
		}
    	
		return null;
   }
}