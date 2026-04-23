package io.github.sefiraat.simplestorage.categories;

import io.github.sefiraat.simplestorage.SimpleStorage;
import io.github.sefiraat.simplestorage.items.Skulls;
import io.github.sefiraat.simplestorage.utils.Theme;
import com.github.drakescraft-labs.slimefun4.api.items.groups.NestedItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.groups.SubItemGroup;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;
import org.bukkit.NamespacedKey;

public final class Categories {

    private Categories() {
        throw new IllegalStateException("Utility class");
    }

    public static CustomItemStack catSimpleStorageMain() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.CAT_CORE)),
                Theme.MAIN + "Simple Storage"
        );
    }
    public static CustomItemStack catSimpleStorageMaterials() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.CAT_MATERIALS)),
                Theme.MAIN + "Simple Storage Materials"
        );
    }
    public static CustomItemStack catSimpleStorageItems() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.CAT_ITEMS)),
                Theme.MAIN + "Simple Storage Items"
        );
    }
    public static CustomItemStack catSimpleStorageMachines() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.CAT_MACHINES)),
                Theme.MAIN + "Simple Storage Machines"
        );
    }
    public static CustomItemStack catSimpleStorageChests() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.CAT_CHESTS)),
                Theme.MAIN + "Simple Storage Chests/Cells"
        );
    }


    public static final NestedItemGroup CORE = new NestedItemGroup(new NamespacedKey(SimpleStorage.inst(), "simple-storage"), catSimpleStorageMain());
    public static final SubItemGroup MAT = new SubItemGroup(new NamespacedKey(SimpleStorage.inst(), "simple-storage-materials"), CORE, catSimpleStorageMaterials());
    public static final SubItemGroup ITM = new SubItemGroup(new NamespacedKey(SimpleStorage.inst(), "simple-storage-items"), CORE, catSimpleStorageItems());
    public static final SubItemGroup MCN = new SubItemGroup(new NamespacedKey(SimpleStorage.inst(), "simple-storage-machines"), CORE, catSimpleStorageMachines());
    public static final SubItemGroup CHT = new SubItemGroup(new NamespacedKey(SimpleStorage.inst(), "simple-storage-chests"), CORE, catSimpleStorageChests());

    public static void set(SimpleStorage p) {
        CORE.register(p);
        MAT.register(p);
        ITM.register(p);
        MCN.register(p);
        CHT.register(p);
    }

}
