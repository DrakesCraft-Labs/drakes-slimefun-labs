package ne.fnfal113.relicsofcthonia.slimefun;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import com.github.drakescraft_labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.core.Keys;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RelicVoider extends UnplaceableBlock {

    private final boolean notifEnabled = RelicsOfCthonia.getInstance().getConfig().getBoolean("enable-relic-voider-notif", true);
    private final Rarity rarity;

    public RelicVoider(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Rarity rarity) {
        super(itemGroup, item, recipeType, recipe);
        this.rarity = rarity;
    }

    public static void setConditionQuota(ItemStack itemStack, int quota) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataAPI.setInt(meta, Keys.RELIC_CONDITION_QUOTA, quota);
        itemStack.setItemMeta(meta);
    }

    public static int getConditionQuota(ItemStack itemStack) {
        return PersistentDataAPI.getInt(itemStack.getItemMeta(), Keys.RELIC_CONDITION_QUOTA, 1);
    }

    public boolean onRelicPickup(EntityPickupItemEvent event, ItemStack voider, AbstractRelic relic, Item relicItem) {
        if (relic.getRarity().ordinal() > rarity.ordinal()) {
            return false;
        }

        if (AbstractRelic.getRelicCondition(relicItem.getItemStack()) <= getConditionQuota(voider)) {
            if (notifEnabled) {
                Utils.sendRelicMessage("&6Voided " + "&r" + relic.getItemName(), event.getEntity());
            }
            relicItem.remove();
            event.setCancelled(true);
            return true;
        }
        return false;
    }

}
