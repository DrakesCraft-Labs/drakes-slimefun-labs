package ne.fnfal113.relicsofcthonia.slimefun.relics.epic;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;

public class BottleOPower extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public BottleOPower(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.EPIC, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            offHand.subtract();
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 400, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 400, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 0));
            Utils.sendRelicMessage("&eBottle o Power has granted you special effects! Time to rekt pvp'iers!", player);
        });
    }

}
