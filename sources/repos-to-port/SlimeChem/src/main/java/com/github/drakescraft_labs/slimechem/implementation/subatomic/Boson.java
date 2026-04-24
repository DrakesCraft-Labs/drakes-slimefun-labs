package com.github.drakescraft_labs.slimechem.implementation.subatomic;

import com.github.drakescraft_labs.slimechem.implementation.attributes.Itemable;
import com.github.drakescraft_labs.slimechem.lists.Constants;
import com.github.drakescraft_labs.slimechem.utils.StringUtil;
import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
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
