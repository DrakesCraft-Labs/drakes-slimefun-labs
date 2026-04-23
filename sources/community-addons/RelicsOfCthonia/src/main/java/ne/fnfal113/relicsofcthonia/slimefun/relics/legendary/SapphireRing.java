package ne.fnfal113.relicsofcthonia.slimefun.relics.legendary;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;

import javax.annotation.ParametersAreNonnullByDefault;

public class SapphireRing extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public SapphireRing(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.LEGENDARY, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) ->
                Utils.sendRelicMessage("&eWhat a precious stone this ring have, piglin traders want this so bad.", player));
    }

}