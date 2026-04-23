package ne.fnfal113.relicsofcthonia.slimefun.relics.uncommon;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;

public class AgedWine extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public AgedWine(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.UNCOMMON, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            if (!player.hasPotionEffect(PotionEffectType.LEVITATION)) {
                offHand.subtract();
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 1));
                Utils.sendRelicMessage("&eAged wine consumed! you feeling the booz there ah.", player);
            }
        });
    }

}