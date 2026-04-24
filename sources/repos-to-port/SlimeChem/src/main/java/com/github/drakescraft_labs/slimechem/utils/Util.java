package com.github.drakescraft_labs.slimechem.utils;

import com.github.drakescraft_labs.slimechem.implementation.atomic.isotopes.Isotope;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactivity;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;

import java.util.List;

/**
 * General utility methods
 * 
 * @author Seggan
 * @author Mooy1
 * 
 */
public final class Util {
    
    public static <T> void trimList(List<T> list, int size) {
        while (list.size() > size) {
            list.remove(list.size() - 1);
        }
    }

    public static SlimefunItemStack getIsotopeItem(Isotope isotope) {
        return isotope.isMainIsotope() ? isotope.getCorrespondingElement().getItem() : isotope.getItem();
    }

    public static Radioactivity fromRadioactivityInt(int radioactivityValue) {
        switch (radioactivityValue) {
            case 1:
                return Radioactivity.LOW;
            case 2:
            case 3:
            case 4:
                return Radioactivity.MODERATE;
            case 5:
            case 6:
            case 7:
            case 8:
                return Radioactivity.HIGH;
            case 9:
            case 10:
            case 11:
            case 12:
                return Radioactivity.VERY_HIGH;
            default:
                return Radioactivity.VERY_DEADLY;
        }
    }
}
