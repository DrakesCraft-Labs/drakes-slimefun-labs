package me.jasperchasetoq.compressioncraft;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import org.bukkit.NamespacedKey;

public class CompressionCraftRecipeTypes {

    private CompressionCraftRecipeTypes() {}

    public static final RecipeType ItemCompression = new RecipeType(new NamespacedKey(CompressionCraft.getInstance(), "ITEMCOMPRESSION"),new SlimefunItemStack("ITEMCOMPRESSION",CompressionCraftItems.JC_COMPRESSOR_1,"&fItem Compression","","&fused in the","&fCompressor and Decompressor"));
}
