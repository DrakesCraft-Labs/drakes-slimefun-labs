package com.github.drakescraft-labs.gcereborn.items.chicken;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;

import com.github.drakescraft-labs.gcereborn.GeneticChickengineering;

import lombok.Getter;

@Getter
public class ChickenProduct {

    private final String name;
    private final ItemStack product;

    @ParametersAreNonnullByDefault
    public ChickenProduct(String name, ItemStack product) {
        this.name = name.toUpperCase(Locale.ROOT);
        this.product = product;
    }

    @ParametersAreNonnullByDefault
    public ChickenProduct(ItemStack product) {
        if (product instanceof SlimefunItemStack sfProduct) {
            this.name = sfProduct.getItemId();
        } else {
            this.name = product.getType().name().toUpperCase(Locale.ROOT);
        }
        this.product = product;

    }

    @Nonnull
    public String getProductName() {
        return GeneticChickengineering.getLocalization().getString("products." + name);
    }
}
