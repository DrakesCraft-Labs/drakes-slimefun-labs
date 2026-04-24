package io.github.sefiraat.simplestorage.items.chests.network;

import io.github.sefiraat.simplestorage.items.Skulls;
import io.github.sefiraat.simplestorage.utils.Theme;
import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class NetworkElement {

    private final Block block;
    private NetworkElementType type;
    private Material material;
    private String skullTexture;
    private int slotClose;
    private int slotRename;
    private int slotSetBlock;

    public Block getBlock() {
        return block;
    }
    public NetworkElementType getType() {
        return type;
    }

    public void setType(NetworkElementType type) {
        this.type = type;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getSkullTexture() {
        return skullTexture;
    }

    public void setSkullTexture(String skullTexture) {
        this.skullTexture = skullTexture;
    }

    public int getSlotClose() {
        return slotClose;
    }

    public void setSlotClose(int slotClose) {
        this.slotClose = slotClose;
    }

    public int getSlotRename() {
        return slotRename;
    }

    public void setSlotRename(int slotRename) {
        this.slotRename = slotRename;
    }

    public int getSlotSetBlock() {
        return slotSetBlock;
    }

    public void setSlotSetBlock(int slotSetBlock) {
        this.slotSetBlock = slotSetBlock;
    }

    public int getBarrelAmount() {
        String amount = BlockStorage.getLocationInfo(block.getLocation(), "stored");
        if (amount != null) {
            return Integer.parseInt(amount);
        }
        return 0;
    }

    public String getDisplayName() {
        return BlockStorage.getLocationInfo(block.getLocation(), "cellname");
    }

    public Material getDisplayMaterial() {
        String mat = BlockStorage.getLocationInfo(block.getLocation(), "cellmaterial");
        if (mat != null) {
            return Material.valueOf(mat);
        }
        return null;
    }

    public NetworkElement(Block block, int slotClose, int slotRename, int slotSetBlock) {
        this.block = block;
        this.slotClose = slotClose;
        this.slotRename = slotRename;
        this.slotSetBlock = slotSetBlock;
    }

    public enum NetworkElementType {
        INVENTORY_CELL,
        INFINITY_BARREL,
        FLUFFY_BARREL,
        NONE
    }

    public static String networkElementTypeName(NetworkElementType type) {
        switch (type) {
            case INVENTORY_CELL: return Theme.MAIN + "Simple Storage Cell";
            case INFINITY_BARREL: return ChatColor.AQUA + "Infinity Storage Unit";
            case FLUFFY_BARREL: return ChatColor.GOLD + "Fluffy Barrel";
            default: return "Uh oh, something went a bit wrong...";
        }
    }

    public static ItemStack getItemStack(NetworkElement ne) {
        if (ne.getDisplayMaterial() != null) {
            return new ItemStack(ne.getDisplayMaterial());
        } else if (ne.getType() == NetworkElementType.INVENTORY_CELL) {
            return PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.BLOCK_CELL_BASIC));
        } else {
            return new ItemStack(ne.block.getType());
        }
    }
}
