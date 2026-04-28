package io.github.sefiraat.slimetinker.items.tinkermaterials.setup;

import io.github.sefiraat.slimetinker.items.Materials;
import io.github.sefiraat.slimetinker.items.tinkermaterials.TinkerMaterial;
import io.github.sefiraat.slimetinker.utils.Ids;
import io.github.sefiraat.slimetinker.utils.SkullTextures;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
public final class TinkersMaterialsDynatech {

    private TinkersMaterialsDynatech() {
        throw new UnsupportedOperationException("Utility Class");
    }

    private static final Map<String, TinkerMaterial> CM_MAP = new HashMap<>();

    static {
        registerStainlessSteel();
        registerVexGem();
        registerStardust();
        registerGhostlyEssence();
        registerTesseract();
    }

    public static Map<String, TinkerMaterial> getCmMap() {
        return CM_MAP;
    }

    private static ItemStack resolveItem(String id) {
        SlimefunItem item = SlimefunItem.getById(id);
        return item == null ? null : item.getItem();
    }

    private static void registerStainlessSteel() {
        ItemStack stack = resolveItem("STAINLESS_STEEL");
        if (stack == null) {
            return;
        }
        CM_MAP.put(
            Ids.STAINLESS_STEEL,
            new TinkerMaterial(Ids.STAINLESS_STEEL, stack, "#d1d1d1")
                .setLiquidTexture(SkullTextures.ALLOY_SILVER)
                .setTraitToolHead(Traits.DYN_STAINLESS_STEEL_HEAD)
                .setTraitToolRod(Traits.DYN_STAINLESS_STEEL_ROD)
                .setTraitArmorPlates(Traits.DYN_STAINLESS_STEEL_PLATES)
                .setTraitArmorLinks(Traits.DYN_STAINLESS_STEEL_LINKS)
                .setFormNugget(Materials.NUGGET_CAST_STAINLESSSTEEL.getItemId())
                .setFormIngot("STAINLESS_STEEL")
                .setFormBlock(Materials.BLOCK_CAST_STAINLESSSTEEL.getItemId())
                .addAlloyRecipe(
                    TinkersMaterialsCore.getCmMap().get(Ids.IRON).getLiquidItemStack(2),
                    TinkersMaterialsCore.getCmMap().get(Ids.ZINC).getLiquidItemStack(1)
                )
                .build()
        );
    }

    private static void registerVexGem() {
        ItemStack stack = resolveItem("VEX_GEM");
        if (stack == null) {
            return;
        }
        CM_MAP.put(
            Ids.VEX_GEM,
            new TinkerMaterial(Ids.VEX_GEM, stack, "#38c0c2")
                .setLiquidTexture(SkullTextures.ALLOY_BLUE_PALE)
                .setTraitToolBinder(Traits.DYN_VEX_GEM_BINDING)
                .setTraitArmorGambeson(Traits.DYN_VEX_GEM_GAMBESON)
                .setFormGem("VEX_GEM")
                .build()
        );
    }

    private static void registerStardust() {
        ItemStack stack = resolveItem("STAR_DUST");
        if (stack == null) {
            return;
        }
        CM_MAP.put(
            Ids.STARDUST,
            new TinkerMaterial(Ids.STARDUST, stack, "#fdff96")
                .setLiquidTexture(SkullTextures.ALLOY_SILVER)
                .setTraitToolHead(Traits.DYN_STAR_DUST_HEAD)
                .setTraitToolRod(Traits.DYN_STAR_DUST_ROD)
                .setTraitArmorPlates(Traits.DYN_STAR_DUST_PLATES)
                .setTraitArmorLinks(Traits.DYN_STAR_DUST_LINKS)
                .setFormGem("STAR_DUST")
                .build()
        );
    }

    private static void registerGhostlyEssence() {
        ItemStack stack = resolveItem("GHOSTLY_ESSENCE");
        if (stack == null) {
            return;
        }
        CM_MAP.put(
            Ids.GHOSTLY_ESSENCE,
            new TinkerMaterial(Ids.GHOSTLY_ESSENCE, stack, "#d4ffef")
                .setLiquidTexture(SkullTextures.ALLOY_BROWN)
                .setTraitToolBinder(Traits.DYN_GHOSTLY_ESSENCE_BINDING)
                .setTraitToolRod(Traits.DYN_GHOSTLY_ESSENCE_ROD)
                .setTraitArmorGambeson(Traits.DYN_GHOSTLY_ESSENCE_GAMBESON)
                .setTraitArmorLinks(Traits.DYN_GHOSTLY_ESSENCE_LINKS)
                .setFormDust("GHOSTLY_ESSENCE")
                .build()
        );
    }

    private static void registerTesseract() {
        ItemStack stack = resolveItem("TESSERACTING_OBJ");
        if (stack == null) {
            return;
        }
        CM_MAP.put(
            Ids.TESSERACT,
            new TinkerMaterial(Ids.TESSERACT, stack, "#c7ba9f")
                .setLiquidTexture(SkullTextures.ALLOY_TAN)
                .setTraitToolHead(Traits.DYN_TESSERACT_HEAD)
                .setTraitArmorPlates(Traits.DYN_TESSERACT_PLATES)
                .setTraitArmorLinks(Traits.DYN_TESSERACT_LINKS)
                .setFormBlock("TESSERACTING_OBJ")
                .build()
        );
    }
}
