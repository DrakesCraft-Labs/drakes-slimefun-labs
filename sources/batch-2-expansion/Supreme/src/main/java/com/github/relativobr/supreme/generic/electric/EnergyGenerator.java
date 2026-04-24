package com.github.relativobr.supreme.generic.electric;

import com.github.relativobr.supreme.Supreme;
import com.github.relativobr.supreme.util.UtilEnergy;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetProvider;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import javax.annotation.Nonnull;

import dev.drake.dough.items.CustomItemStack;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.DirtyChestMenu;
import dev.drake.infinitylib.machines.MenuBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class EnergyGenerator extends MenuBlock implements EnergyNetProvider {

  private int energy;
  private int buffer;
  private int generate = 0;
  private int currentDelay = 0;
  private GenerationType type;


  public EnergyGenerator(ItemGroup categories, SlimefunItemStack item, ItemStack[] recipe) {
    super(categories, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
  }

  public GenerationType getType() {
    return type;
  }

  public EnergyGenerator setType(GenerationType value) {
    this.type = value;
    return this;
  }

  public EnergyGenerator setEnergy(int value) {
    this.energy = value;
    return this;
  }

  public EnergyGenerator setBuffer(int value) {
    this.buffer = value;
    return this;
  }

  @Nonnull
  @Override
  public EnergyNetComponentType getEnergyComponentType() {
    return EnergyNetComponentType.GENERATOR;
  }

  @Override
  protected void setup(BlockMenuPreset blockMenuPreset) {
    blockMenuPreset.drawBackground(new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26
    });
  }

  @Nonnull
  @Override
  protected int[] getInputSlots(DirtyChestMenu dirtyChestMenu, ItemStack itemStack) {
    return new int[0];
  }

  @Override
  protected int[] getInputSlots() {
    return new int[0];
  }

  @Override
  protected int[] getOutputSlots() {
    return new int[0];
  }

  @Override
  public int getGeneratedOutput(Location l, Config data) {

    if(this.generate > 0 && (this.currentDelay < Supreme.getSupremeOptions().getDelayTimeValidGenerators())){
      this.currentDelay++;
    } else {
      // check block
      this.generate = this.type.generate(l.getWorld(), l.getBlock(), this.energy);
      this.currentDelay = 0;
    }


    BlockMenu inv = BlockStorage.getInventory(l);
    if (inv != null && inv.hasViewer()) {
      if (this.generate == 0) {
        inv.replaceExistingItem(13, new CustomItemStack(
                Material.RED_STAINED_GLASS_PANE,
                "&cNot generating",
                "&7Type: &6" + this.type,
                "&7Stored: &6" + UtilEnergy.format(getCharge(l)) + " J",
                "&7Capacity: &6" + UtilEnergy.format(this.buffer) + " J"
        ));
      } else {
        inv.replaceExistingItem(13, new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                "&aGeneration",
                "&7Type: &6" + this.type,
                "&7Generating: &6" + UtilEnergy.format(this.generate) + " J/tick ",
                "&7Stored: &6" + UtilEnergy.format(getCharge(l)) + " J",
                "&7Capacity: &6" + UtilEnergy.format(this.buffer) + " J"
        ));
      }
    }

    return this.generate;
  }

  @Override
  public int getCapacity() {
    return this.buffer;
  }


}