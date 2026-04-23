package ne.fnfal113.relicsofcthonia.slimefun.relics.epic;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;

public class ThunderInABottle extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public ThunderInABottle(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.EPIC, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            Block block = player.getTargetBlockExact(70);
            if (block == null || block.getType() == Material.AIR) {
                return;
            }
            offHand.subtract();
            player.getWorld().strikeLightning(block.getLocation());
            Utils.sendRelicMessage("&eYou have summoned a lightning on the target location!", player);
        });
    }

}