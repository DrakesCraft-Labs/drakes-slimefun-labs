package ne.fnfal113.relicsofcthonia.core;

import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import org.bukkit.NamespacedKey;

public class RecipeTypes {

    public static final RecipeType DROP_TYPE = new RecipeType(
            new NamespacedKey(RelicsOfCthonia.getInstance(), "relic_drop_type_generic"),
            ItemStacks.RELIC_DROP_TYPE
    );
    
}
