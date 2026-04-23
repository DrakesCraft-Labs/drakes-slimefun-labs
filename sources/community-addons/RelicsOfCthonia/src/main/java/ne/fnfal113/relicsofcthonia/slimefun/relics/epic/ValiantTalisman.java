package ne.fnfal113.relicsofcthonia.slimefun.relics.epic;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;

public class ValiantTalisman extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public ValiantTalisman(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.EPIC, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            if(!player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)){
                offHand.subtract();
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 420, 1));
                Utils.sendRelicMessage("&eValiant Talisman got destroyed upon usage and has granted you with health boost!", player);
            }
        });
    }

}
