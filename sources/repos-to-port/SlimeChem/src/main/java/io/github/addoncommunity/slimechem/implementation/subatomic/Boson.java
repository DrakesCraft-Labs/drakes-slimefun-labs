package io.github.addoncommunity.slimechem.implementation.subatomic;

import io.github.addoncommunity.slimechem.implementation.attributes.Itemable;
import io.github.addoncommunity.slimechem.lists.Constants;
import io.github.addoncommunity.slimechem.utils.StringUtil;
import lombok.Getter;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;

/**
 * Enum of bosons
 *
 * @author Mooy1
 *
 */
@Getter
public enum Boson implements Itemable {

    PHOTON(),
    GLUON(),
    Z_BOSON(),
    W_BOSON(),

    HIGGS_BOSON();

    private final SlimefunItemStack item;

    Boson() {
        String name = this.toString();
        if (!Constants.isTestingEnvironment) {
            this.item = new SlimefunItemStack(
                name,
                Material.LIGHT_BLUE_DYE,
                "&7" + StringUtil.enumNameToTitleCaseString(name),
                "&7Type: boson",
                "&7A force-carrying particle"
            );
        } else {
            this.item = null;
        }
    }
}
