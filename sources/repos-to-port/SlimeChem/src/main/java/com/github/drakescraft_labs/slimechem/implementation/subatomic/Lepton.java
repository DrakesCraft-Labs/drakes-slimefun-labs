package com.github.drakescraft_labs.slimechem.implementation.subatomic;

import com.github.drakescraft_labs.slimechem.implementation.attributes.Itemable;
import com.github.drakescraft_labs.slimechem.lists.Constants;
import com.github.drakescraft_labs.slimechem.utils.StringUtil;
import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;

/**
 * Enum of leptons
 *
 * @author Mooy1
 */
@Getter
public enum Lepton implements Fermion, Itemable {

    ELECTRON(),
    ELECTRON_NEUTRINO(),
    MUON(),
    MUON_NEUTRINO(),
    TAU(),
    TAU_NEUTRINO();

    private final SlimefunItemStack item;

    Lepton() {
        String name = this.toString();
        if (!Constants.isTestingEnvironment) {
            this.item = new SlimefunItemStack(
                name,
                Material.YELLOW_DYE,
                "&7" + StringUtil.enumNameToTitleCaseString(name),
                "&7Type: lepton",
                "&7This particle does not interact via the strong force"
            );
        } else {
            this.item = null;
        }
    }
}
