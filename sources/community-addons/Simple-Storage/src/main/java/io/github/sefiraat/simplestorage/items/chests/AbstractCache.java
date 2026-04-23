package io.github.sefiraat.simplestorage.items.chests;

import io.github.sefiraat.simplestorage.items.Skulls;
import io.github.sefiraat.simplestorage.utils.Theme;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;

import java.util.Map;

public abstract class AbstractCache {

    protected final BlockMenu blockMenu;
    protected int pages;
    protected int page = 1;
    protected int prevPage = 0;
    protected final Map<Integer, String> guiNumbers;

    protected AbstractCache(BlockMenu blockMenu) {

        this.blockMenu = blockMenu;

        guiNumbers = Skulls.infoSkulls();

        blockMenu.addMenuClickHandler(NetworkChest.SLOT_BACK, (player, i, itemStack, clickAction) -> {
            if (page > 1) {
                page-= 1;
            }
            int skullID = page;
            if (page > 20) {
                skullID = 21;
            }
            blockMenu.replaceExistingItem(0, new CustomItemStack(
                    PlayerHead.getItemStack(PlayerSkin.fromBase64(guiNumbers.get(skullID))),
                    Theme.GUI_HEAD + "Info",
                    "",
                    Theme.CLICK_INFO + "Page number : " + page
            ));
            return false;
        });

        blockMenu.addMenuClickHandler(NetworkChest.SLOT_FORWARD, (player, i, itemStack, clickAction) -> {
            if (page < pages) {
                page+= 1;
            }
            int skullID = page;
            if (page > 20) {
                skullID = 21;
            }
            blockMenu.replaceExistingItem(0, new CustomItemStack(
                    PlayerHead.getItemStack(PlayerSkin.fromBase64(guiNumbers.get(skullID))),
                    Theme.GUI_HEAD + "Info",
                    "",
                    Theme.CLICK_INFO + "Page number : " + page
            ));
            return false;
        });

        blockMenu.addMenuClickHandler(SimpleChest.SLOT_INFO, ((player, i, itemStack, clickAction) -> false));
    }
}
