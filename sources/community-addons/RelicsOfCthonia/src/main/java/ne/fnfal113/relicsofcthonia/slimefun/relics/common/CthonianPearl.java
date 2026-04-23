package ne.fnfal113.relicsofcthonia.slimefun.relics.common;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;

public class CthonianPearl extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public CthonianPearl(ItemGroup itemGroup, SlimefunItemStack item, double defaultDropChance, int defaultPiglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.COMMON, defaultDropChance, defaultPiglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            Block block = player.getTargetBlockExact(70);
            if (block == null || block.getType() == Material.AIR){
                return;
            }
            offHand.subtract();
            player.teleport(block.getLocation());
            Utils.sendRelicMessage("&eYou have been teleported to your target location using Cthonian Pearl", player);
        });
    }

}
