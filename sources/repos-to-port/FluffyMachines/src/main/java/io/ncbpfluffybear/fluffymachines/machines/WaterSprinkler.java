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
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import io.ncbpfluffybear.fluffymachines.utils.FluffyItems;

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
        new IntRangeSetting(this, "intervalo-minutos", 1, 7, 120);

    /** Cuando rego por ultima vez cada aspersor, por ubicacion. */
    private static final Map<String, Long> ultimoRiego = new ConcurrentHashMap<>();
    public static final int ENERGY_CONSUMPTION = 64;
    public static final int CHUNK_ENERGY_CONSUMPTION = 256;
    public static final int CAPACITY = 512;
    /**
     * Radio de riego, en bloques a cada lado.
     *
     * Lo pidio Chagui en su rama Warter-Sprinkler-modifications-chagui68, donde lo resolvia con
     * un campo estatico leido del config.yml del plugin. Se hace aqui como ItemSetting por dos
     * razones: encaja con los otros cuatro ajustes de esta misma maquina --se toca en Items.yml y
     * se recarga sin reiniciar-- y evita el problema de aquel enfoque, que inicializaba un
     * `static final` desde un getter y por tanto se fijaba al cargar la clase, antes incluso de
     * que el config estuviera leido.
     */
    public final ItemSetting<Integer> radio = new IntRangeSetting(this, "radio", 1, 3, MAX_RADIUS);

    /** Tope del radio. AREA se calcula sobre el, no sobre el valor actual, porque el maximo de
     *  blocks-per-cycle no puede encogerse por debajo de lo que un jugador ya tenga guardado. */
    private static final int MAX_RADIUS = 8;
    private static final int DIAMETER = MAX_RADIUS * 2 + 1;
    private static final int AREA = DIAMETER * DIAMETER;
    private static final int PROGRESS_SLOT = 4;
    private static final int UPGRADE_SLOT = 2;
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

                // El modulo permanece dentro y se devuelve al romper la maquina.
                blockMenuPreset.addItem(UPGRADE_SLOT, null);
                blockMenuPreset.addItem(PROGRESS_SLOT, noWaterItem);
            });

        addItemSetting(successChance, particles, blocksPerCycle, intervaloMinutos, radio);
    }

    public int getEnergyConsumption() {
        return ENERGY_CONSUMPTION;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    public int getRadius() {
        return radio.getValue();
    }

    @Override
    public int[] getInputSlots() {
        return new int[] {UPGRADE_SLOT};
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

        final boolean chunkUpgrade = hasChunkUpgrade(inv);
        final int activationCost = chunkUpgrade ? CHUNK_ENERGY_CONSUMPTION : getEnergyConsumption();
        if (getCharge(b.getLocation()) < activationCost) {
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

        final int minX = WaterSprinklerScanPlan.scanMin(chunkUpgrade, b.getX(), getRadius());
        final int maxX = WaterSprinklerScanPlan.scanMax(chunkUpgrade, b.getX(), getRadius());
        final int minZ = WaterSprinklerScanPlan.scanMin(chunkUpgrade, b.getZ(), getRadius());
        final int maxZ = WaterSprinklerScanPlan.scanMax(chunkUpgrade, b.getZ(), getRadius());

        // Una carga por barrido evita que el buffer limite el riego a ocho cultivos.
        removeCharge(b.getLocation(), activationCost);
        if (particles.getValue()) {
            spawnWaterSweep(b, minX, maxX, minZ, maxZ);
        }

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                Block crop = findCrop(b, x, z);
                if (crop != null) {
                    Material type = crop.getType();
                    grow(crop, type == Material.SUGAR_CANE ? null : crop.getBlockData());
                }
            }
        }
    }

    private boolean hasChunkUpgrade(BlockMenu menu) {
        SlimefunItem item = SlimefunItem.getByItem(menu.getItemInSlot(UPGRADE_SLOT));
        return item != null && item.getId().equals(FluffyItems.WATER_SPRINKLER_CHUNK_UPGRADE.getItemId());
    }

    /** Dibuja una cuadricula ligera que muestra el alcance real de la activacion. */
    private void spawnWaterSweep(Block sprinkler, int minX, int maxX, int minZ, int maxZ) {
        for (int x = minX; x <= maxX; x += 2) {
            for (int z = minZ; z <= maxZ; z += 2) {
                sprinkler.getWorld().spawnParticle(Particle.SPLASH,
                    sprinkler.getLocation().add(x + 0.5D, 0.8D, z + 0.5D), 2, 0.2D, 0.15D, 0.2D, 0.02D);
            }
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
