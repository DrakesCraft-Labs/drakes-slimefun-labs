package com.github.drakescraft_labs.slimechem.implementation.atomic;

import com.github.drakescraft_labs.slimechem.implementation.atomic.isotopes.Isotope;
import com.github.drakescraft_labs.slimechem.implementation.attributes.Atom;
import com.github.drakescraft_labs.slimechem.implementation.attributes.Ingredient;
import com.github.drakescraft_labs.slimechem.lists.Constants;
import com.github.drakescraft_labs.slimechem.utils.SubNum;
import com.github.drakescraft_labs.slimechem.utils.Util;
import com.github.drakescraft_labs.slimechem.implementation.subatomic.Nucleon;
import com.github.drakescraft_labs.slimefun4.utils.LoreBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Enum of chemical elements: name, mass, number, symbol, series
 *
 * @author Mooy1
 * @see Ingredient
 * @see Nucleon
 */
@Getter
public enum Element implements Ingredient, Atom {

    HYDROGEN(1.0079, "Hydrogen", "H", 1, Series.NONMETALS),
    HELIUM(4.0026, "Helium", "He", 2, Series.NOBLE_GASES),
    LITHIUM(6.941, "Lithium", "Li", 3, Series.ALKALI_METALS),
    BERYLLIUM(9.0122, "Beryllium", "Be", 4, Series.ALKALINE_EARTH_METALS),
    BORON(10.811, "Boron", "B", 5, Series.METALLOIDS),
    CARBON(12.0107, "Carbon", "C", 6, Series.NONMETALS),
    NITROGEN(14.0067, "Nitrogen", "N", 7, Series.NONMETALS),
    OXYGEN(15.9994, "Oxygen", "O", 8, Series.NONMETALS),
    FLUORINE(18.9984, "Fluorine", "F", 9, Series.NONMETALS),
    NEON(20.1797, "Neon", "Ne", 10, Series.NOBLE_GASES),
    SODIUM(22.9897, "Sodium", "Na", 11, Series.ALKALI_METALS),
    MAGNESIUM(24.305, "Magnesium", "Mg", 12, Series.ALKALINE_EARTH_METALS),
    ALUMINUM(26.9815, "Aluminum", "Al", 13, Series.POST_TRANSITION_METALS),
    SILICON(28.0855, "Silicon", "Si", 14, Series.METALLOIDS),
    PHOSPHORUS(30.9738, "Phosphorus", "P", 15, Series.NONMETALS),
    SULFUR(32.065, "Sulfur", "S", 16, Series.NONMETALS),
    CHLORINE(35.453, "Chlorine", "Cl", 17, Series.NONMETALS),
    ARGON(39.948, "Argon", "Ar", 18, Series.NOBLE_GASES),
    POTASSIUM(39.0983, "Potassium", "K", 19, Series.ALKALI_METALS),
    CALCIUM(40.078, "Calcium", "Ca", 20, Series.ALKALINE_EARTH_METALS),
    SCANDIUM(44.9559, "Scandium", "Sc", 21, Series.TRANSITION_METALS),
    TITANIUM(47.867, "Titanium", "Ti", 22, Series.TRANSITION_METALS),
    VANADIUM(50.9415, "Vanadium", "V", 23, Series.TRANSITION_METALS),
    CHROMIUM(51.9961, "Chromium", "Cr", 24, Series.TRANSITION_METALS),
    MANGANESE(54.938, "Manganese", "Mn", 25, Series.TRANSITION_METALS),
    IRON(55.845, "Iron", "Fe", 26, Series.TRANSITION_METALS),
    NICKEL(58.6934, "Nickel", "Ni", 28, Series.TRANSITION_METALS),
    COBALT(58.9332, "Cobalt", "Co", 27, Series.TRANSITION_METALS),
    COPPER(63.546, "Copper", "Cu", 29, Series.TRANSITION_METALS),
    ZINC(65.39, "Zinc", "Zn", 30, Series.TRANSITION_METALS),
    GALLIUM(69.723, "Gallium", "Ga", 31, Series.POST_TRANSITION_METALS),
    GERMANIUM(72.64, "Germanium", "Ge", 32, Series.METALLOIDS),
    ARSENIC(74.9216, "Arsenic", "As", 33, Series.METALLOIDS),
    SELENIUM(78.96, "Selenium", "Se", 34, Series.NONMETALS),
    BROMINE(79.904, "Bromine", "Br", 35, Series.NONMETALS),
    KRYPTON(83.8, "Krypton", "Kr", 36, Series.NOBLE_GASES),
    RUBIDIUM(85.4678, "Rubidium", "Rb", 37, Series.ALKALI_METALS),
    STRONTIUM(87.62, "Strontium", "Sr", 38, Series.ALKALINE_EARTH_METALS),
    YTTRIUM(88.9059, "Yttrium", "Y", 39, Series.TRANSITION_METALS),
    ZIRCONIUM(91.224, "Zirconium", "Zr", 40, Series.TRANSITION_METALS),
    NIOBIUM(92.9064, "Niobium", "Nb", 41, Series.TRANSITION_METALS),
    MOLYBDENUM(95.94, "Molybdenum", "Mo", 42, Series.TRANSITION_METALS),
    TECHNETIUM(98, "Technetium", "Tc", 43, Series.TRANSITION_METALS, 4),
    RUTHENIUM(101.07, "Ruthenium", "Ru", 44, Series.TRANSITION_METALS),
    RHODIUM(102.9055, "Rhodium", "Rh", 45, Series.TRANSITION_METALS),
    PALLADIUM(106.42, "Palladium", "Pd", 46, Series.TRANSITION_METALS),
    SILVER(107.8682, "Silver", "Ag", 47, Series.TRANSITION_METALS),
    CADMIUM(112.411, "Cadmium", "Cd", 48, Series.TRANSITION_METALS),
    INDIUM(114.818, "Indium", "In", 49, Series.POST_TRANSITION_METALS),
    TIN(118.71, "Tin", "Sn", 50, Series.POST_TRANSITION_METALS),
    ANTIMONY(121.76, "Antimony", "Sb", 51, Series.METALLOIDS),
    TELLURIUM(127.6, "Tellurium", "Te", 52, Series.METALLOIDS),
    IODINE(126.9045, "Iodine", "I", 53, Series.NONMETALS),
    XENON(131.293, "Xenon", "Xe", 54, Series.NOBLE_GASES),
    CESIUM(132.9055, "Cesium", "Cs", 55, Series.ALKALI_METALS),
    BARIUM(137.327, "Barium", "Ba", 56, Series.ALKALINE_EARTH_METALS),
    LANTHANUM(138.9055, "Lanthanum", "La", 57, Series.LANTHANOIDS),
    CERIUM(140.116, "Cerium", "Ce", 58, Series.LANTHANOIDS),
    PRASEODYMIUM(140.9077, "Praseodymium", "Pr", 59, Series.LANTHANOIDS),
    NEODYMIUM(144.24, "Neodymium", "Nd", 60, Series.LANTHANOIDS),
    PROMETHIUM(145, "Promethium", "Pm", 61, Series.LANTHANOIDS, 9),
    SAMARIUM(150.36, "Samarium", "Sm", 62, Series.LANTHANOIDS),
    EUROPIUM(151.964, "Europium", "Eu", 63, Series.LANTHANOIDS),
    GADOLINIUM(157.25, "Gadolinium", "Gd", 64, Series.LANTHANOIDS),
    TERBIUM(158.9253, "Terbium", "Tb", 65, Series.LANTHANOIDS),
    DYSPROSIUM(162.5, "Dysprosium", "Dy", 66, Series.LANTHANOIDS),
    HOLMIUM(164.9303, "Holmium", "Ho", 67, Series.LANTHANOIDS),
    ERBIUM(167.259, "Erbium", "Er", 68, Series.LANTHANOIDS),
    THULIUM(168.9342, "Thulium", "Tm", 69, Series.LANTHANOIDS),
    YTTERBIUM(173.04, "Ytterbium", "Yb", 70, Series.LANTHANOIDS),
    LUTETIUM(174.967, "Lutetium", "Lu", 71, Series.LANTHANOIDS),
    HAFNIUM(178.49, "Hafnium", "Hf", 72, Series.TRANSITION_METALS),
    TANTALUM(180.9479, "Tantalum", "Ta", 73, Series.TRANSITION_METALS),
    TUNGSTEN(183.84, "Tungsten", "W", 74, Series.TRANSITION_METALS),
    RHENIUM(186.207, "Rhenium", "Re", 75, Series.TRANSITION_METALS),
    OSMIUM(190.23, "Osmium", "Os", 76, Series.TRANSITION_METALS),
    IRIDIUM(192.217, "Iridium", "Ir", 77, Series.TRANSITION_METALS),
    PLATINUM(195.078, "Platinum", "Pt", 78, Series.TRANSITION_METALS),
    GOLD(196.9665, "Gold", "Au", 79, Series.TRANSITION_METALS),
    MERCURY(200.59, "Mercury", "Hg", 80, Series.TRANSITION_METALS),
    THALLIUM(204.3833, "Thallium", "Tl", 81, Series.POST_TRANSITION_METALS),
    LEAD(207.2, "Lead", "Pb", 82, Series.POST_TRANSITION_METALS),
    BISMUTH(208.9804, "Bismuth", "Bi", 83, Series.POST_TRANSITION_METALS, 1),
    POLONIUM(209, "Polonium", "Po", 84, Series.POST_TRANSITION_METALS, 8),
    ASTATINE(210, "Astatine", "At", 85, Series.METALLOIDS, 14),
    RADON(222, "Radon", "Rn", 86, Series.NOBLE_GASES, 12),
    FRANCIUM(223, "Francium", "Fr", 87, Series.ALKALI_METALS, 16),
    RADIUM(226, "Radium", "Ra", 88, Series.ALKALINE_EARTH_METALS, 7),
    ACTINIUM(227, "Actinium", "Ac", 89, Series.ACTINOIDS, 9),
    THORIUM(232.0381, "Thorium", "Th", 90, Series.ACTINOIDS, 1),
    PROTACTINIUM(231.0359, "Protactinium", "Pa", 91, Series.ACTINOIDS, 5),
    URANIUM(238.0289, "Uranium", "U", 92, Series.ACTINOIDS, 2),
    NEPTUNIUM(237, "Neptunium", "Np", 93, Series.ACTINOIDS, 4),
    PLUTONIUM(244, "Plutonium", "Pu", 94, Series.ACTINOIDS, 3),
    AMERICIUM(243, "Americium", "Am", 95, Series.ACTINOIDS, 6),
    CURIUM(247, "Curium", "Cm", 96, Series.ACTINOIDS, 3),
    BERKELIUM(247, "Berkelium", "Bk", 97, Series.ACTINOIDS, 7),
    CALIFORNIUM(251, "Californium", "Cf", 98, Series.ACTINOIDS, 7),
    EINSTEINIUM(252, "Einsteinium", "Es", 99, Series.ACTINOIDS, 9),
    FERMIUM(257, "Fermium", "Fm", 100, Series.ACTINOIDS, 10),
    MENDELEVIUM(258, "Mendelevium", "Md", 101, Series.ACTINOIDS, 11),
    NOBELIUM(259, "Nobelium", "No", 102, Series.ACTINOIDS, 15),
    LAWRENCIUM(266, "Lawrencium", "Lr", 103, Series.ACTINOIDS, 13),
    RUTHERFORDIUM(267, "Rutherfordium", "Rf", 104, Series.TRANSITION_METALS, 15),
    DUBNIUM(268, "Dubnium", "Db", 105, Series.TRANSITION_METALS, 12),
    SEABORGIUM(269, "Seaborgium", "Sg", 106, Series.TRANSITION_METALS, 16),
    BOHRIUM(270, "Bohrium", "Bh", 107, Series.TRANSITION_METALS, 17),
    HASSIUM(277, "Hassium", "Hs", 108, Series.TRANSITION_METALS, 19),
    MEITNERIUM(278, "Meitnerium", "Mt", 109, Series.UNKNOWN, 21),
    DARMSTADTIUM(281, "Darmstadtium", "Ds", 110, Series.UNKNOWN, 19),
    ROENTGENIUM(282, "Roentgenium", "Rg", 111, Series.UNKNOWN, 17),
    COPERNICIUM(285, "Copernicium", "Cn", 112, Series.UNKNOWN, 18),
    NIHONIUM(286, "Nihonium", "Nh", 113, Series.UNKNOWN, 20),
    FLEROVIUM(289, "Flerovium", "Fl", 114, Series.UNKNOWN, 22),
    MOSCOVIUM(290, "Moscovium", "Mc", 115, Series.UNKNOWN, 23),
    LIVERMORIUM(293, "Livermorium", "Lv", 116, Series.UNKNOWN, 24),
    TENNESSINE(294, "Tennessine", "Ts", 117, Series.UNKNOWN, 24),
    OGANESSON(294, "Oganesson", "Og", 118, Series.UNKNOWN, 25),
    MOOYIUM(315, "Mooyium", "My", 119, Series.CUSTOM),
    SEGGANESSON(336, "Segganesson", "Gg", 120, Series.CUSTOM);

    private final double mass;
    @Nonnull
    private final String name;
    @Nonnull
    private final String symbol;
    private final int number;
    @Nonnull
    private final Series series;
    private final int neutrons;
    private final boolean radioactive;
    private final int radiationLevel;
    private final SlimefunItemStack item;

    Element(double mass, @Nonnull String name, @Nonnull String symbol, int number, @Nonnull Series series, int radiationLevel) {
        this.mass = mass;
        this.name = name;
        this.symbol = symbol;
        this.number = number;
        this.series = series;
        this.neutrons = (int) Math.round(mass) - number;
        this.radioactive = radiationLevel != 0;

        this.radiationLevel = radiationLevel;

        if (!Constants.isTestingEnvironment) {
            this.item = new SlimefunItemStack(
                "ELEMENT_" + this.name(),
                Objects.requireNonNull(Material.getMaterial(series.getColor() + "_DYE")),
                "&b" + name,
                "&7" + symbol + " " + number,
                "&7Mass: " + mass,
                this.isRadioactive() ? LoreBuilder.radioactive(Util.fromRadioactivityInt(this.radiationLevel)) : ""
            );
        } else {
            this.item = null;
        }
    }

    Element(double mass, @Nonnull String name, @Nonnull String symbol, int number, @Nonnull Series series) {
        this(mass, name, symbol, number, series, 0);
    }

    @Nullable
    public static Element getByNumber(int i) {
        for (Element e : values()) {
            if (e.getNumber() == i) return e;
        }
        return null;
    }

    @Nonnull
    public static Element getByAbbr(String abbr) {
        for (Element e : values()) {
            if (e.symbol.equals(abbr)) return e;
        }
        throw new IllegalArgumentException("Invalid abbreviation, got: " + abbr);
    }

    @Nonnull
    public Isotope getCorrespondingIsotope() {
        return Objects.requireNonNull(Isotope.getIsotope((int) Math.round(this.mass), this), () -> "For element " + this.name);
    }

    @Nonnull
    @Override
    public String getFormula(int i) {
        return this.symbol + SubNum.fromInt(i);
    }

    @Nonnull
    @Override
    public MoleculeIngredient asIngredient(int amount) {
        return new MoleculeIngredient(this, amount);
    }

    @Nonnull
    @Override
    public MoleculeIngredient asIngredient() {
        return this.asIngredient(1);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Series {

        ALKALI_METALS("Alkali Metals", "ORANGE"),
        ALKALINE_EARTH_METALS("Alkaline Earth Metals", "YELLOW"),
        LANTHANOIDS("Lanthanoids", "LIME"),
        ACTINOIDS("Actinoids", "GREEN"),
        TRANSITION_METALS("Transition Metals", "GRAY"),
        POST_TRANSITION_METALS("Post Transition Metals", "BLUE"),
        METALLOIDS("Metalloids", "CYAN"),
        NONMETALS("Nonmetals", "LIGHT_BLUE"),
        NOBLE_GASES("Noble Gases", "PINK"),
        UNKNOWN("Unknown", "PURPLE"),
        CUSTOM("Custom", "MAGENTA");

        @Nonnull
        private final String name;
        @Nonnull
        private final String color;

    }
}
