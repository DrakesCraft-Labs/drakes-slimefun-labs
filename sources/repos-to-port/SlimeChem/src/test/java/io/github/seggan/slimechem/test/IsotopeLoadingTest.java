package com.github.drakescraft_labs.slimechem.test;

import com.github.drakescraft_labs.slimechem.implementation.atomic.Element;
import com.github.drakescraft_labs.slimechem.implementation.atomic.isotopes.Isotope;
import com.github.drakescraft_labs.slimechem.implementation.atomic.isotopes.IsotopeLoader;
import com.github.drakescraft_labs.slimechem.lists.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.EnumMap;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IsotopeLoadingTest {

    @BeforeAll
    public static void setUp() {
        Constants.isTestingEnvironment = true;
        Isotope.getIsotopes().clear();
    }

    @Test
    @Order(1)
    public void testIsotopeLoading() {
        IsotopeLoader isotopeLoader = new IsotopeLoader();
        isotopeLoader.load();
        isotopeLoader.loadDecayProducts();
        int i = 0;
        for (Set<Isotope> isotopes : Isotope.getIsotopes().values()) {
            for (Isotope isotope : isotopes) {
                i++;
            }
        }
        System.out.println(i);
    }

    @Test
    @Order(2)
    public void testIsotopeDecayProducts() {
        final EnumMap<Element, Set<Isotope>> isotopes = Isotope.getIsotopes();

        for (Element el : isotopes.keySet()) {
            for (Isotope iso : isotopes.get(el)) {
                iso.getDecayProduct();
            }
        }
    }
}
