package io.ncbpfluffybear.fluffymachines.machines;

import com.github.drakescraft_labs.slimefun4.api.items.ItemSetting;
import com.github.drakescraft_labs.slimefun4.api.items.settings.IntRangeSetting;
import com.github.drakescraft_labs.slimefun4.implementation.items.electric.machines.accelerators.AbstractGrowthAccelerator;
import com.github.drakescraft_labs.slimefun4.implementation.items.electric.machines.accelerators.CropGrowthAccelerator;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@link WaterSprinkler} speeds up the growth of nearby crops
 * when water is under the machine
 * Essentially a modified {@link CropGrowthAccelerator}
 *
 * @author FluffyBear
 */
public class WaterSprinkler extends AbstractGrowthAccelerator {

    public final ItemSetting<Double> successChance = new ItemSetting<>(this, "success-chance", 0.5);

    /**
     * Cada cuantos minutos riega, y cuando lo hace lo hace del todo.
     *
     * Antes subia un nivel de crecimiento con un 50% de probabilidad por ciclo, lo que en la
     * practica se notaba tan poco que los jugadores lo daban por roto. Ahora la maquina espera y
     * luego madura los cultivos de golpe: se entiende de un vistazo lo que hace, y sigue teniendo
     * un coste real en tiempo y energia.
     */
    public final ItemSetting<Integer> intervaloMinutos =
        new IntRangeSetting(this, "intervalo-minutos", 1, 15, 120);

    /** Cuando rego por ultima vez cada aspersor, por ubicacion. */
    private static final Map<String, Long> ultimoRiego = new ConcurrentHashMap<>();
    public static final int ENERGY_CONSUMPTION = 16;
    public static final int CAPACITY = 128;
    private static final int RADIUS = 2;
    private static final int DIAMETER = RADIUS * 2 + 1;
    private static final int AREA = DIAMETER * DIAMETER;
    private static final int PROGRESS_SLOT = 4;
    private static final Set<Material> AGEABLE_CROPS = EnumSet.of(
        Material.WHEAT,
        Material.CARROTS,
        Material.POTATOES,
        Material.BEETROOTS,
        Material.NETHER_WART,
        Material.COCOA,
        Material.SWEET_BERRY_BUSH,
        Material.CAVE_VINES,
        Material.CAVE_VINES_PLANT,
        Material.PITCHER_CROP,
        Material.TORCHFLOWER_CROP
    );
    private static final CustomItemStack noWaterItem = new CustomItemStack(Material.BUCKET,
        "&cNo water found",
        "",
        "&cPlease place water under the sprinkler!"
    );
    private static final CustomItemStack waterFoundItem = new CustomItemStack(Material.WATER_BUCKET,
        "&bWater detected"
    );
    private final ItemSetting<Boolean> particles = new ItemSetting<>(this, "particles", true);
    private final ItemSetting<Integer> blocksPerCycle = new IntRangeSetting(this, "blocks-per-cycle", 1, 4, AREA);

    public WaterSprinkler(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        createPreset(this, "&bWater Sprinkler",
            blockMenuPreset -> {
                for (int i = 0; i < 9; i++)
                    blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());

                blockMenuPreset.addItem(PROGRESS_SLOT, noWaterItem);
            });

        addItemSetting(successChance, particles, blocksPerCycle, intervaloMinutos);
    }

    public int getEnergyConsumption() {
        return ENERGY_CONSUMPTION;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    public int getRadius() {
        return RADIUS;
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    protected void tick(@Nonnull Block b) {
        if (this.isDisabled()) {
            return;
        }

        final BlockMenu inv = BlockStorage.getInventory(b);
        boolean open = inv.hasViewer();

        if (b.getRelative(BlockFace.DOWN).getType() == Material.WATER) {
            if (open) {
                inv.replaceExistingItem(PROGRESS_SLOT, waterFoundItem);
            }
        } else {
            if (open) {
                inv.replaceExistingItem(PROGRESS_SLOT, noWaterItem);
            }
            return;
        }

        int availableCharge = getCharge(b.getLocation());
        if (availableCharge < getEnergyConsumption()) {
            return;
        }

        // Solo riega cuando toca. El resto del tiempo la maquina esta encendida y consumiendo,
        // pero sin hacer nada visible: es el coste de esperar.
        final String clave = b.getWorld().getName() + ":" + b.getX() + ":" + b.getY() + ":" + b.getZ();
        final long ahora = System.currentTimeMillis();
        final long periodo = intervaloMinutos.getValue() * 60_000L;
        final Long previo = ultimoRiego.get(clave);
        if (previo != null && ahora - previo < periodo) {
            return;
        }
        ultimoRiego.put(clave, ahora);

        // Cuando toca regar se riega el area entera, no un trozo. El reparto por ciclos existia
        // para no hacer todo el trabajo en un solo tick cuando el aspersor actuaba constantemente;
        // ahora actua una vez cada cuarto de hora, asi que puede permitirse la pasada completa.
        int[] indexes = new int[AREA];
        for (int i = 0; i < AREA; i++) {
            indexes[i] = i;
        }

        for (int index : indexes) {
            int x = index / DIAMETER - getRadius();
            int z = index % DIAMETER - getRadius();
            Block crop = findCrop(b, x, z);
            if (crop == null) {
                continue;
            }
            Material type = crop.getType();

            if (availableCharge < getEnergyConsumption()) {
                return;
            }

            if (particles.getValue()) {
                crop.getWorld().spawnParticle(Particle.SPLASH, crop.getLocation().add(0.5D, 0.5D, 0.5D),
                    1, 0.1F, 0.1F, 0.1F);
            }

            grow(crop, type == Material.SUGAR_CANE ? null : crop.getBlockData());
            removeCharge(b.getLocation(), getEnergyConsumption());
            availableCharge -= getEnergyConsumption();
        }
    }

    /**
     * Finds a supported crop without forcing every farm to place the machine at one exact Y.
     * Existing farms commonly put the sprinkler over the water source, one block above or below
     * the crop hitbox. The old same-level-only lookup detected water and energy but never found
     * those crops, making the GUI look healthy while the machine did no work.
     */
    private Block findCrop(Block sprinkler, int x, int z) {
        for (int y : new int[] {0, -1, 1}) {
            Block candidate = sprinkler.getRelative(x, y, z);
            if (isSupportedCrop(candidate.getType())) {
                return candidate;
            }
        }
        return null;
    }

    static boolean isSupportedCrop(Material material) {
        return material == Material.SUGAR_CANE || AGEABLE_CROPS.contains(material);
    }

    private void grow(@Nonnull Block crop, BlockData blockData) {

        // Sin tirada de suerte: cuando le toca regar, riega. La probabilidad estaba encima del
        // incremento de un solo nivel y hacia que el efecto fuera practicamente invisible.
        {
            if (crop.getType() == Material.SUGAR_CANE) {
                for (int i = 1; i < 3; i++) {
                    final Block above = crop.getRelative(BlockFace.UP, i);
                    if (above.getType().isAir()) {
                        above.setType(Material.SUGAR_CANE);
                        break;
                    } else if (above.getType() != Material.SUGAR_CANE) {
                        return;
                    }
                }
            } else if (blockData instanceof Ageable ageable) {
                if (ageable.getAge() < ageable.getMaximumAge()) {

                    // Al maximo de una vez, no un nivel.
                    ageable.setAge(ageable.getMaximumAge());
                    crop.setBlockData(ageable);

                    crop.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, crop.getLocation().add(0.5D, 0.5D, 0.5D),
                        2, 0.1F, 0.1F, 0.1F);
                }
            }
        }
    }

}
