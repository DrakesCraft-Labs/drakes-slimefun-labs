package ne.fnfal113.relicsofcthonia.slimefun.relics.rare;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;

import javax.annotation.ParametersAreNonnullByDefault;

public class CrossedSwords extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public CrossedSwords(ItemGroup itemGroup, SlimefunItemStack item,   double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.RARE, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) ->
                Utils.sendRelicMessage("&eThese swords are too old and fragile to be used in combat, I'll trade it instead.", player));
    }

}
