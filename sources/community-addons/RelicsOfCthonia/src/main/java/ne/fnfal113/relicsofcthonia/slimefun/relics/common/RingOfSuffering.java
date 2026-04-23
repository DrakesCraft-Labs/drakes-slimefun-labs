package ne.fnfal113.relicsofcthonia.slimefun.relics.common;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;

public class RingOfSuffering extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public RingOfSuffering(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.COMMON, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            if(!player.hasPotionEffect(PotionEffectType.WEAKNESS)){
                offHand.subtract();
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 160, 1));
                Utils.sendRelicMessage("&eRing of Suffering got destroyed upon usage and has punished you with weakness!", player);
            }
        });
    }

}
