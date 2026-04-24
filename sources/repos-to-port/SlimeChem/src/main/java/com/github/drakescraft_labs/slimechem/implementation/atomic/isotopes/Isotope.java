package com.github.drakescraft_labs.slimechem.implementation.atomic.isotopes;

import com.github.drakescraft_labs.slimechem.implementation.atomic.MoleculeIngredient;
import com.github.drakescraft_labs.slimechem.implementation.attributes.Ingredient;
import com.github.drakescraft_labs.slimechem.lists.Constants;
import com.github.drakescraft_labs.slimechem.utils.SubNum;
import com.github.drakescraft_labs.slimechem.utils.SuperNum;
import com.github.drakescraft_labs.slimechem.utils.Util;
import com.github.drakescraft_labs.infinitylib.common.StackUtils;
import com.github.drakescraft_labs.slimechem.implementation.atomic.Element;
import com.github.drakescraft_labs.slimechem.implementation.attributes.Atom;
import com.github.drakescraft_labs.slimefun4.utils.LoreBuilder;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Class for isotopes
 *
 * @author Seggan
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Isotope implements Ingredient, Atom {
    @Getter
    private static final EnumMap<Element, Set<Isotope>> isotopes = new EnumMap<>(Element.class);

    private final int mass;
    @EqualsAndHashCode.Include
    private final int protons;
    @EqualsAndHashCode.Include
    private final int neutrons;
    private final Element element;

    private final String name;
    private final String formula;

    private final DecayType decayType;
    @Getter(AccessLevel.NONE)
    private Isotope decayProduct = null;
    private final int radiationLevel;

    private final SlimefunItemStack item;
    @Getter(AccessLevel.NONE)
    private final String id;

    private Isotope(int mass, String abbr, DecayType decayType) {
        this.element = Element.getByAbbr(abbr);

        this.mass = mass;
        this.protons = this.element.getNumber();
        this.neutrons = mass - this.protons;

        this.name = this.element.getName() + "-" + this.mass;
        this.formula = SuperNum.fromInt(this.mass) + abbr;

        this.decayType = decayType;

        if (this.isRadioactive()) {
            this.radiationLevel = (int) (this.element.getRadiationLevel() +
                Math.abs(Math.round(this.element.getMass()) - this.mass));
        } else {
            this.radiationLevel = 0;
        }

        if (!Constants.isTestingEnvironment) {
            if (this.isMainIsotope()) {
                this.item = this.element.getItem();
            } else {
                this.item = new SlimefunItemStack(
                    String.format("ISOTOPE_%s_%d", this.element.name(), this.mass),
                    Objects.requireNonNull(Material.getMaterial(this.element.getSeries().getColor() + "_DYE")),
                    "&b" + this.name,
                    "&7" + this.formula,
                    "&7Mass: " + this.mass,
                    this.isRadioactive() ? LoreBuilder.radioactive(Util.fromRadioactivityInt(this.radiationLevel)) : ""
                );
            }
            this.id = this.item.getItemId();
        } else {
            this.item = null;
            this.id = null;
        }
    }

    @Nonnull
    public static Isotope addIsotope(int mass, String abbr, DecayType decayType) {
        Isotope isotope = new Isotope(mass, abbr, decayType);
        Element element = isotope.getElement();
        isotopes.computeIfAbsent(element, ununsed -> new HashSet<>()).add(isotope);

        return isotope;
    }

    @Nullable
    public static Isotope getIsotope(int mass, @Nonnull Element element) {
        for (Isotope isotope : isotopes.get(element)) {
            if (isotope.getElement() == element && isotope.getMass() == mass) {
                return isotope;
            }
        }

        return null;
    }

    public void loadDecayProduct(int mass, String abbr) {
        for (Isotope isotope : isotopes.get(Element.getByAbbr(abbr))) {
            if (isotope.getMass() == mass) {
                this.decayProduct = isotope;
                return;
            }
        }
        if (this.decayProduct == null) {
            isotopes.get(this.element).remove(this);
            if (Constants.isTestingEnvironment) {
                System.out.println(this);
            }
        }
    }

    /**
     * Gets the decay product of the isotope
     *
     * @return {@link Optional}; empty if the isotope is stable, otherwise contains the decay product
     * @throws IllegalStateException if the decay product has not been loaded <i>and</i> the isotope is radioactive
     */
    @Nonnull
    public Optional<Isotope> getDecayProduct() {
        if (this.decayType == DecayType.STABLE) return Optional.empty();

        if (this.decayProduct == null) {
            throw new IllegalStateException("Decay product not initialized!");
        } else {
            return Optional.of(this.decayProduct);
        }
    }

    public boolean isRadioactive() {
        return this.decayType != DecayType.STABLE;
    }

    /**
     * This method returns an {@link Element}mif this isotope is the main isotope.
     * Otherwise, returns null. For example, Hydrogen-1 would return {@link Element#HYDROGEN},
     * but Hydrogen-2 would return null.
     *
     * @return {@link Element}
     * @implNote the check uses {@link #isMainIsotope()}
     */
    public Element getCorrespondingElement() {
        return isMainIsotope() ? this.element : null;
    }

    /**
     * Checks if this isotope is the main isotope of the element
     *
     * @return true if the isotope is the main isotope of the element, false otherwise
     * @implNote executes {@code Math.round(this.element.getMass()) == this.mass}
     */
    public boolean isMainIsotope() {
        return Math.round(this.element.getMass()) == this.mass;
    }

    @Nullable
    public static Isotope getByItem(ItemStack item) {
        if (item == null) return null;

        String id = StackUtils.getID(item);
        if (id == null) return null;

        for (Set<Isotope> isotopeSet : isotopes.values()) {
            for (Isotope isotope : isotopeSet) {
                if (isotope.id.equals(id)) {
                    return isotope;
                }
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.element.getName() + '-' + this.mass;
    }

    @Nonnull
    @Override
    public String getFormula(int i) {
        return this.formula + SubNum.fromInt(i);
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

    @Override
    public int getNumber() {
        return this.protons;
    }

    @Override
    public double getMass() {
        return this.mass;
    }
}
