/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
 *  com.github.drakescraft_labs.slimefun4.api.items.ItemHandler
 *  com.github.drakescraft_labs.slimefun4.api.items.ItemSetting
 *  com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
 *  com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
 *  com.github.drakescraft_labs.slimefun4.api.items.settings.DoubleRangeSetting
 *  com.github.drakescraft_labs.slimefun4.api.items.settings.IntRangeSetting
 *  com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
 *  com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent
 *  com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler
 *  com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType
 *  com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem
 *  com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker
 *  com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage
 *  javax.annotation.Nonnull
 *  javax.annotation.ParametersAreNonnullByDefault
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package me.poma123.globalwarming.items.machines;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.ItemHandler;
import com.github.drakescraft_labs.slimefun4.api.items.ItemSetting;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.items.settings.DoubleRangeSetting;
import com.github.drakescraft_labs.slimefun4.api.items.settings.IntRangeSetting;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.poma123.globalwarming.eventos.ProteccionHelper;
import me.poma123.globalwarming.eventos.RegistroClimatizadores;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Climatizador
extends SimpleSlimefunItem<BlockTicker>
implements EnergyNetComponent {
    private final IntRangeSetting radio;
    private final DoubleRangeSetting objetivo;
    private final int consumo;
    private final int capacidad;

    private static final Map<Location, CachedZona> ZONA_CACHE = new ConcurrentHashMap<Location, CachedZona>();

    private static final class CachedZona {
        final ProteccionHelper.Zona zona;
        final long expira;

        CachedZona(ProteccionHelper.Zona zona, long expira) {
            this.zona = zona;
            this.expira = expira;
        }
    }

    @ParametersAreNonnullByDefault
    public Climatizador(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int consumo, int capacidad) {
        super(itemGroup, item, recipeType, recipe);
        this.consumo = consumo;
        this.capacidad = capacidad;
        this.radio = new IntRangeSetting((SlimefunItem)this, "radio", 4, 24, 64);
        this.objetivo = new DoubleRangeSetting((SlimefunItem)this, "temperatura-objetivo", -20.0, 21.0, 50.0);
        this.addItemSetting(new ItemSetting[]{this.radio, this.objetivo});
        this.addItemHandler(new ItemHandler[]{this.alRomper()});
    }

    @Nonnull
    private BlockBreakHandler alRomper() {
        return new BlockBreakHandler(false, false){

            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                Climatizador.olvidar(e.getBlock().getLocation());
            }
        };
    }

    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    public int getCapacity() {
        return this.capacidad;
    }

    public int getEnergyConsumption() {
        return this.consumo;
    }

    private ProteccionHelper.Zona obtenerZonaCacheada(Location loc, int radioDefecto) {
        long ahora = System.currentTimeMillis();
        CachedZona cached = ZONA_CACHE.get(loc);
        if (cached != null && cached.expira > ahora) {
            return cached.zona;
        }
        ProteccionHelper.Zona zona = ProteccionHelper.obtenerZona(loc, radioDefecto);
        ZONA_CACHE.put(loc, new CachedZona(zona, ahora + 30000L));
        return zona;
    }

    public int calcularConsumo(ProteccionHelper.Zona zona) {
        if (zona == null || "radio".equals(zona.tipo)) {
            return this.consumo;
        }
        long chunks = (zona.area + 255L) / 256L;
        if (chunks <= 9L) {
            return this.consumo;
        }
        long extraChunks = chunks - 9L;
        long calculado = (long) this.consumo + (extraChunks * 2L);
        return (int) Math.min(calculado, 256L);
    }

    public BlockTicker getItemHandler() {
        return new BlockTicker(){

            public void tick(Block b, SlimefunItem item, Config data) {
                Location loc = b.getLocation();
                int radioValor = ((Integer)Climatizador.this.radio.getValue()).intValue();
                ProteccionHelper.Zona zona = Climatizador.this.obtenerZonaCacheada(loc, radioValor);
                int gasto = Climatizador.this.calcularConsumo(zona);

                if (Climatizador.this.getCharge(loc, data) < gasto) {
                    return;
                }
                Climatizador.this.removeCharge(loc, gasto);
                RegistroClimatizadores.anunciar(loc, zona, (Double)Climatizador.this.objetivo.getValue());
            }

            public boolean isSynchronized() {
                return true;
            }
        };
    }

    public static void olvidar(@Nonnull Location l) {
        RegistroClimatizadores.olvidar(l);
        ZONA_CACHE.remove(l);
        BlockStorage.clearBlockInfo((Location)l);
    }
}

