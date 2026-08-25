package dev.j3fftw.litexpansion.weapons;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.items.PassiveElectricRemoval;
import dev.j3fftw.litexpansion.machine.multiblock.MetalForge;
import dev.j3fftw.litexpansion.utils.Constants;
import dev.j3fftw.litexpansion.utils.Utils;
import com.github.drakescraft_labs.slimefun4.core.attributes.Rechargeable;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class NanoBlade extends SimpleSlimefunItem<ItemUseHandler> implements Rechargeable, PassiveElectricRemoval {

    public static final float CAPACITY = 4_000;
    public static final float PER_TICK_REMOVAL = 64;

    public NanoBlade() {
        super(Items.LITEXPANSION, Items.NANO_BLADE, MetalForge.RECIPE_TYPE, new ItemStack[] {
                new ItemStack(Material.GLOWSTONE_DUST), Items.ADVANCED_ALLOY, null,
                new ItemStack(Material.GLOWSTONE_DUST), Items.ADVANCED_ALLOY, null,
                Items.CARBON_PLATE, SlimefunItems.POWER_CRYSTAL, Items.CARBON_PLATE
            }
        );
    }

    @Override
    public float getMaxItemCharge(ItemStack item) {
        return CAPACITY;
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return event -> {
            final ItemMeta nanoBladeMeta = event.getItem().getItemMeta();
            /*
             * El estado real vive en el PDC (NANO_BLADE_ENABLED, mas abajo). El encantamiento
             * propio solo daba el brillo, y desde 1.21 Paper ya no permite registrarlo por la
             * API antigua: Enchantment.getByKey devuelve null y cualquier uso directo lanza
             * "Enchantment cannot be null". Por eso se lee el PDC y se alterna sobre el.
             */
            boolean enabled = !Utils.getOptionalBoolean(nanoBladeMeta, Constants.NANO_BLADE_ENABLED).orElse(false);

            int damage;

            if (enabled && getItemCharge(event.getItem()) > getRemovedChargePerTick()) {
                aplicarBrillo(nanoBladeMeta, true);
                nanoBladeMeta.setDisplayName(ChatColor.DARK_GREEN + "Nano Blade" + ChatColor.GREEN + " (On)");

                damage = 13; // Base is 7 so 7 + 13 = 20
            } else {
                aplicarBrillo(nanoBladeMeta, false);
                nanoBladeMeta.setDisplayName(ChatColor.DARK_GREEN + "Nano Blade" + ChatColor.RED + " (Off)");

                damage = -3; // Base is 7 so 7 - 3 = 4
            }

            PersistentDataAPI.setBoolean(nanoBladeMeta, Constants.NANO_BLADE_ENABLED, enabled);

            nanoBladeMeta.removeAttributeModifier(Attribute.ATTACK_DAMAGE);
            nanoBladeMeta.addAttributeModifier(Attribute.ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(), Attribute.ATTACK_DAMAGE.getKey().getKey(), damage,
                    AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND
                )
            );

            event.getItem().setItemMeta(nanoBladeMeta);
        };
    }

    @Override
    public float getRemovedChargePerTick() {
        return PER_TICK_REMOVAL;
    }

    @Override
    public float getCapacity() {
        return CAPACITY;
    }

    @Override
    public boolean isEnabled(@Nonnull ItemMeta meta) {
        final Optional<Boolean> opt = Utils.getOptionalBoolean(meta, Constants.NANO_BLADE_ENABLED);
        if (opt.isPresent() && opt.get()) {
            return true;
        }

        /*
         * Respaldo para hojas antiguas, anteriores a que el estado se guardara en el PDC.
         * getByKey devuelve null en 1.21 y hasEnchant(null) lanza IllegalArgumentException,
         * que es lo que inundaba la consola desde el ticker de PassiveElectricRemoval.
         */
        final Enchantment glow = Enchantment.getByKey(Constants.GLOW_ENCHANT);
        return glow != null && meta.hasEnchant(glow);
    }

    /**
     * Marca visualmente la hoja encendida.
     *
     * <p>Se prefiere el brillo nativo de 1.20.5+; si el encantamiento propio todavia
     * existe se mantiene por compatibilidad con hojas ya fabricadas.
     */
    private static void aplicarBrillo(@Nonnull ItemMeta meta, boolean encendida) {
        meta.setEnchantmentGlintOverride(encendida ? Boolean.TRUE : null);

        final Enchantment glow = Enchantment.getByKey(Constants.GLOW_ENCHANT);
        if (glow == null) {
            return;
        }
        if (encendida) {
            meta.addEnchant(glow, 1, false);
        } else {
            meta.removeEnchant(glow);
        }
    }
}
