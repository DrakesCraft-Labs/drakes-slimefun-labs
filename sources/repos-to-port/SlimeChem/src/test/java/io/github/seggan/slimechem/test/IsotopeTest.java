package com.github.drakescraft_labs.slimechem.test;

import io.github.addoncommunity.slimechem.implementation.atomic.isotopes.DecayType;
import io.github.addoncommunity.slimechem.implementation.atomic.isotopes.Isotope;
import io.github.addoncommunity.slimechem.implementation.atomic.isotopes.IsotopeLoader;
import io.github.addoncommunity.slimechem.lists.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

public class IsotopeTest {

    @BeforeAll
    public static void setUp() {
        Constants.isTestingEnvironment = true;
    }

    @Test
    public void testGetDecayProduct() {
        Isotope.getIsotopes().clear();

        Asserts.assertEquals(Isotope.addIsotope(1, "H", DecayType.STABLE).getDecayProduct(),
            Optional.empty());

        Isotope isotope = Isotope.addIsotope(2, "H", DecayType.ALPHA);
        isotope.loadDecayProduct(1, "H");
        isotope.getDecayProduct();

        Asserts.assertThrows(
            () -> Isotope.addIsotope(3, "H", DecayType.ALPHA).getDecayProduct(),
            IllegalStateException.class
        );
    }

    @Test
    public void testSameDecay() {
        Isotope.getIsotopes().clear();
        IsotopeLoader isotopeLoader = new IsotopeLoader();
        isotopeLoader.load();
        isotopeLoader.loadDecayProducts();

        for (Set<Isotope> isotopeSet : Isotope.getIsotopes().values()) {
            for (Isotope isotope : isotopeSet) {
                Optional<Isotope> decayProduct = isotope.getDecayProduct();

                decayProduct.ifPresent(i -> {
                    if (i.getElement() == isotope.getElement() &&
                        !(isotope.getDecayType().toString().contains("NEUTRON"))) {
                        System.out.println(isotope);
                    }
                });
            }
        }
    }

    /*@Test
    public void testIsotopeRadioactivity() {
        Isotope.getIsotopes().clear();
        IsotopeLoader isotopeLoader = new IsotopeLoader();
        isotopeLoader.load();
        isotopeLoader.loadDecayProducts();

        Isotope biggestRadioactivity;

        for (int i = 0; i < 1000; i++) {
            biggestRadioactivity = Isotope.getIsotope(1, Element.HYDROGEN);

            for (Set<Isotope> isotopeSet : Isotope.getIsotopes().values()) {
                for (Isotope isotope : isotopeSet) {
                    biggestRadioactivity = isotope.getRadiationLevel() > biggestRadioactivity.getRadiationLevel() ?
                        isotope : biggestRadioactivity;
                }
            }

            System.out.println(biggestRadioactivity.toString() + ' ' + biggestRadioactivity.getRadiationLevel());

            Isotope.getIsotopes().get(biggestRadioactivity.getElement()).remove(biggestRadioactivity);
        }
    }*/

    /*private static class SortComparator implements Comparator<Integer> {
        private final Map<Integer, Integer> freqMap;

        // Assign the specified map
        SortComparator(Map<Integer, Integer> tFreqMap)
        {
            this.freqMap = tFreqMap;
        }

        // Compare the values
        @Override
        public int compare(Integer k1, Integer k2)
        {

            // Compare value by frequency
            int freqCompare = freqMap.get(k2).compareTo(freqMap.get(k1));

            // Compare value if frequency is equal
            int valueCompare = k1.compareTo(k2);

            // If frequency is equal, then just compare by value, otherwise -
            // compare by the frequency.
            if (freqCompare == 0)
                return valueCompare;
            else
                return freqCompare;
        }
    }

    @Test
    public void testModeRadiation() {
        Isotope.getIsotopes().clear();
        IsotopeLoader isotopeLoader = new IsotopeLoader();
        isotopeLoader.load();
        isotopeLoader.loadDecayProducts();

        List<Integer> modesList = new ArrayList<>();

        for (Set<Isotope> isotopeSet : Isotope.getIsotopes().values()) {
            for (Isotope isotope : isotopeSet) {
                if (isotope.isRadioactive()) modesList.add(isotope.getRadiationLevel());
            }
        }
        int[] modes = new int[modesList.size()];
        for (int i = 0; i < modes.length; i++) {
            modes[i] = modesList.get(i);
        }

        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> outputArray = new ArrayList<>();

        for (int current : modes) {
            int count = map.getOrDefault(current, 0);
            map.put(current, count + 1);
            outputArray.add(current);
        }

        SortComparator comp = new SortComparator(map);

        outputArray.sort(comp);

        // Final Output
        for (Integer i : outputArray) {
            System.out.print(i + " ");
        }
    }*/
}
