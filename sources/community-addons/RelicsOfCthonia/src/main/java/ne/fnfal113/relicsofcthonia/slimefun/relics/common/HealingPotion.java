package ne.fnfal113.relicsofcthonia.slimefun.relics.common;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.attribute.Attribute;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public class HealingPotion extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public HealingPotion(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.COMMON, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            double currentMaxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
            if (player.getHealth() <= currentMaxHealth - 2){
                offHand.subtract();
                player.setHealth(player.getHealth() + 2);
                Utils.sendRelicMessage("&e2 points of health are added to you after using the healing potion!", player);
                return;
            }
            Utils.sendRelicMessage("&cHealing potion cannot be used on your current health!", player);
        });
    }

}
