package ne.fnfal113.relicsofcthonia.slimefun.relics.legendary;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class EyeOfSauron extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public EyeOfSauron(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.LEGENDARY, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            List<Player> players = new ArrayList<>();
            for (Entity entity : player.getNearbyEntities(100, 60, 100)) {
                if (entity instanceof Player other){
                    Location location = other.getLocation();
                    Utils.sendRelicMessage("&eEye of Sauron found a nearby player named " + player.getName()
                            + " at x: " + location.getBlockX() + " y: " + location.getBlockY() + " z: " + location.getBlockZ(), player);
                    players.add(other);
                }
            }
            if (players.isEmpty()) {
                Utils.sendRelicMessage("&eEye of Sauron did not find any nearby players!", player);
            }
            offHand.subtract();
        });
    }

}