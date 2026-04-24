package com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.Surprise;
import dev.drake.dough.items.CustomItemStack;

public final class LuckyAxeSurprise implements Surprise {
	
	private final ItemStack axe;
	
	public LuckyAxeSurprise() {
		axe = new CustomItemStack(Material.GOLDEN_AXE, "&e&lLucky Axe");
		axe.addUnsafeEnchantment(Enchantment.SHARPNESS, 10);
		axe.addUnsafeEnchantment(Enchantment.EFFICIENCY, 10);
		axe.addUnsafeEnchantment(Enchantment.FORTUNE, 10);
		axe.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
	}
	
	@Override
	public String getName() {
		return "Lucky Axe";
	}

	@Override
	public void activate(Random random, Player p, Location l) {
		l.getWorld().dropItemNaturally(l, axe.clone());
	}

	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.LUCKY;
	}

}
