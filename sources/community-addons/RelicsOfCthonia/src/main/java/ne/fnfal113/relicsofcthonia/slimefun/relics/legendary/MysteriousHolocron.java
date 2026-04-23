package ne.fnfal113.relicsofcthonia.slimefun.relics.legendary;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.libraries.dough.protection.Interaction;
import ne.fnfal113.relicsofcthonia.api.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class MysteriousHolocron extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public MysteriousHolocron(ItemGroup itemGroup, SlimefunItemStack item, double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, Rarity.LEGENDARY, dropChance, piglinRewardAmount, defaultDropSize);
        addItemHandler((OffHandRightClickHandler) (event, player, offHand) -> {
            List<Entity> entities = new ArrayList<>();
            for (Entity entity : player.getNearbyEntities(30, 30, 30)) {
                if(!(entity instanceof Player) && entity instanceof LivingEntity living && !living.hasPotionEffect(PotionEffectType.GLOWING) && Slimefun.getProtectionManager().hasPermission(player, entity.getLocation(), Interaction.INTERACT_ENTITY)) {
                    living.addPotionEffect(PotionEffectType.GLOWING.createEffect(200, 1));
                    entities.add(entity);
                }
            }

            offHand.subtract();
            if(!entities.isEmpty()) {
                Utils.sendRelicMessage("&eWoah what happened to those entities! are they gone forever?!", player);
            } else {
                Utils.sendRelicMessage("&eMysterious holocron got destroyed?! I wonder if there are any nearby entities, what would happen eh", player);
            }
        });
    }

}