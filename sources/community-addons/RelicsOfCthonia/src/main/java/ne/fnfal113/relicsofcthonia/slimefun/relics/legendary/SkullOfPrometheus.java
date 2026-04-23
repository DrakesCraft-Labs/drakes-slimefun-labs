package ne.fnfal113.relicsofcthonia.slimefun.relics.legendary;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;

public class SkullOfPrometheus extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public SkullOfPrometheus(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.LEGENDARY, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            if(!player.hasPotionEffect(PotionEffectType.CONDUIT_POWER)){
                offHand.subtract();
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 500, 1));
                Utils.sendRelicMessage("&eThis skull is a powerful one, it gave me conduit power for trying to wear it but it got destroyed.", player);
            }
        });
    }

}
