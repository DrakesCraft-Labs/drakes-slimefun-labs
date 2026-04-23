package com.github.drakescraft_labs.hotbarpets.groups;

import com.github.drakescraft_labs.hotbarpets.PetTexture;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.hotbarpets.HotbarPet;
import com.github.drakescraft_labs.hotbarpets.HotbarPets;
import com.github.drakescraft_labs.hotbarpets.PetGroup;
import com.github.drakescraft_labs.hotbarpets.pets.special.CookieSlimePet;
import com.github.drakescraft_labs.hotbarpets.pets.special.PatriotPet;
import com.github.drakescraft_labs.hotbarpets.pets.special.PurpliciousCowPet;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;

public final class SpecialPets implements PetGroup {

    public SpecialPets(HotbarPets plugin) {
        load(plugin);
    }

    @Override
    public String getName() {
        return "&9Special";
    }

    @Override
    public void load(HotbarPets plugin) {
        // @formatter:off
        new PurpliciousCowPet(plugin.getItemGroup(), new SlimefunItemStack("HOTBAR_PET_PURPLICIOUS_COW", PetTexture.PURPLICIOUS_COW_PET.getHash(), "&5Purplicious Cow Pet", getName(), "&7Favourite Food: Golden Apples", "", "&fRight-Click: &7Gives you Health Regeneration"), new ItemStack(Material.GOLDEN_APPLE), new ItemStack[] {
                new ItemStack(Material.COOKED_BEEF), new ItemStack(Material.DIAMOND), new ItemStack(Material.COOKED_BEEF),
                new ItemStack(Material.DIAMOND), new ItemStack(Material.EMERALD), new ItemStack(Material.DIAMOND),
                SlimefunItems.GOLD_20K, SlimefunItems.GOLD_20K, SlimefunItems.GOLD_20K
        }).register(plugin);

        new CookieSlimePet(plugin.getItemGroup(), new SlimefunItemStack("HOTBAR_PET_MRCOOKIESLIME", PetTexture.MR_COOKIE_SLIME_PET.getHash(), "&amrCookieSlime Pet", getName(), "&7Favourite Food: Cookies", "", "&fImmune to Fall Damage", "&fRight-Click: &7Gives you Health Regeneration"), new ItemStack(Material.COOKIE), new ItemStack[] {
                new ItemStack(Material.COOKIE), new ItemStack(Material.DIAMOND), new ItemStack(Material.COOKIE),
                new ItemStack(Material.DIAMOND), new ItemStack(Material.EMERALD), new ItemStack(Material.DIAMOND),
                SlimefunItems.GOLD_20K, SlimefunItems.GOLD_20K, SlimefunItems.GOLD_20K
        }).register(plugin);

        new PatriotPet(plugin.getItemGroup(), new SlimefunItemStack("HOTBAR_PET_PATRIOT", PetTexture.PATRIOT_PET.getHash(), "&5Patriot Pet", getName(), "&7Favourite Food: Nether Wart", "", "&fGives Resistance, Regeneration, Strength, ", "&fand Saturation"), new ItemStack(Material.NETHER_WART), new ItemStack[] {
                new ItemStack(Material.REDSTONE), SlimefunItems.MAGIC_LUMP_1, new ItemStack(Material.REDSTONE),
                SlimefunItems.MAGIC_LUMP_1, SlimefunItems.RAW_CARBONADO, SlimefunItems.MAGIC_LUMP_1,
                SlimefunItems.GOLD_4K, new ItemStack(Material.REDSTONE), SlimefunItems.GOLD_4K
        }).register(plugin);

        new HotbarPet(plugin.getItemGroup(), new SlimefunItemStack("HOTBAR_PET_WALSHRUS", PetTexture.WALSHRUS_PET.getHash(), "&bWalshrus Pet", getName(), "&7Favourite Food: Raw Cod", "", "&fCannot drown"), new ItemStack(Material.COD), new ItemStack[] {
                new ItemStack(Material.COD), new ItemStack(Material.DIAMOND), new ItemStack(Material.COD),
                new ItemStack(Material.DIAMOND), new ItemStack(Material.EMERALD), new ItemStack(Material.DIAMOND),
                SlimefunItems.GOLD_20K, SlimefunItems.GOLD_20K, SlimefunItems.GOLD_20K
        }).register(plugin);

        new HotbarPet(plugin.getItemGroup(), new SlimefunItemStack("HOTBAR_PET_EYAMAZ", PetTexture.EYAMAZ_PET.getHash(), "&4Eyamaz Pet", getName(), "&7Favourite Food: Souls", "", "&fBeware! Eyamaz makes a Soul Pie", "&fout of the Souls of everything you kill"), new ItemStack(Material.SOUL_SAND), new ItemStack[] {
                new ItemStack(Material.SOUL_SAND), new ItemStack(Material.DIAMOND), new ItemStack(Material.SOUL_SAND),
                new ItemStack(Material.DIAMOND), new ItemStack(Material.EMERALD), new ItemStack(Material.DIAMOND),
                SlimefunItems.GOLD_20K, SlimefunItems.GOLD_20K, SlimefunItems.GOLD_20K
        }).register(plugin);
        // @formatter:on
    }

}
