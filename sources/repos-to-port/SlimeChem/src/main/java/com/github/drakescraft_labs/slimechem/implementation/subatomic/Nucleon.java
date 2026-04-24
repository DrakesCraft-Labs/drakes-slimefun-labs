package com.github.drakescraft_labs.slimechem.implementation.subatomic;

import com.github.drakescraft_labs.slimechem.implementation.atomic.Element;
import com.github.drakescraft_labs.slimechem.implementation.attributes.Itemable;
import com.github.drakescraft_labs.slimechem.utils.StringUtil;
import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * Enum of nucleons
 *
 * @author Mooy1
 * @see Element
 * @see Quark
 */
@Getter
public enum Nucleon implements Itemable {

    NEUTRON(Quark.UP, Quark.DOWN, Quark.DOWN),
    PROTON(Quark.UP, Quark.UP, Quark.DOWN);

    private final Quark[] quarks;
    private final SlimefunItemStack item;

    Nucleon(@Nonnull Quark... quarks) {
        this.quarks = quarks;

        String name = this.toString();
        this.item = new SlimefunItemStack(
            name,
            Material.WHITE_DYE,
            "&7" + StringUtil.enumNameToTitleCaseString(name),
            "&7Type: nucleon",
            "&7This particle does not interact via the strong force"
        );
    }

}
