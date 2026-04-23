package ne.fnfal113.relicsofcthonia.slimefun.relics.rare;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;

import javax.annotation.ParametersAreNonnullByDefault;

public class GoldenJar extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public GoldenJar(ItemGroup itemGroup, SlimefunItemStack item,   double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.RARE, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) ->
                Utils.sendRelicMessage("&eTime to find a piglin trader, this jar might be something worth trading off!", player));
    }

}