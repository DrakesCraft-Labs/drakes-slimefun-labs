package ne.fnfal113.relicsofcthonia.slimefun.relics.uncommon;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;

import javax.annotation.ParametersAreNonnullByDefault;

public class SkullHat extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public SkullHat(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.UNCOMMON, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) ->
                Utils.sendRelicMessage("&eThis skull hat can be traded to piglins only, it has no uses!", player));
    }

}