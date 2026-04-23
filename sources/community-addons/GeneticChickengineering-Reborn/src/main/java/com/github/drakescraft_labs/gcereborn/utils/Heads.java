package com.github.drakescraft_labs.gcereborn.utils;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Heads {
    // https://minecraft-heads.com/custom-heads/animals/336-chicken
    CHICKEN("1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893");

    private final String texture;

    public ItemStack getItem() {
        return SlimefunUtils.getCustomHead(texture);
    }
}
