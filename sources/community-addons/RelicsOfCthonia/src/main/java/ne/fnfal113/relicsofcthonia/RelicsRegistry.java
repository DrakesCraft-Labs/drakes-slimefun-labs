package ne.fnfal113.relicsofcthonia;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import lombok.experimental.UtilityClass;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class RelicsRegistry {
    public final Map<EntityType, List<AbstractRelic>> ENTITY_SOURCES = new HashMap<>();
    public final Map<Material, List<AbstractRelic>> BLOCK_SOURCES = new HashMap<>();
    public final Map<AbstractRelic, List<ItemStack>> RELIC_OUTPUTS = new HashMap<>();
}
