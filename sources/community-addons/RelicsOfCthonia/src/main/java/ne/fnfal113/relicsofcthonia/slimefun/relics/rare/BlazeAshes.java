package ne.fnfal113.relicsofcthonia.slimefun.relics.rare;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.Particle;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlazeAshes extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public BlazeAshes(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.RARE, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            for (double i = 0; i < 360; i++) {
                player.getWorld().spawnParticle(Particle.ASH, player.getLocation().clone().add(Math.cos(i), i / 180, Math.sin(i)), 0);
            }
            offHand.subtract();
            Utils.sendRelicMessage("&eWoah, what was that effect. I'll just trade this relic instead next time.", player);
        });
    }

}