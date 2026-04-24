package com.github.drakescraft_labs.slimechem.implementation.atomic.isotopes;

import com.github.drakescraft_labs.slimechem.implementation.subatomic.Lepton;
import com.github.drakescraft_labs.slimechem.implementation.atomic.Element;
import com.github.drakescraft_labs.slimechem.implementation.subatomic.Boson;
import com.github.drakescraft_labs.slimechem.implementation.subatomic.Nucleon;
import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DecayType {

    ALPHA("alpha"),
    BETA_PLUS("beta+"),
    BETA_MINUS("beta-"),
    DOUBLE_BETA_MINUS("double beta-"),
    PROTON("p"),
    PROTON_2("2p"),
    PROTON_3("3p"),
    NEUTRON("n"),
    NEUTRON_2("2n"),
    NEUTRON_3("3n"),
    NEUTRON_4("4n"),
    ELECTRON_CAPTURE("electron capture"),
    DOUBLE_ELECTRON_CAPTURE("double electron capture"),
    STABLE("stable");

    private static final Map<String, DecayType> reprmap = new HashMap<>();
    private final String representation;
    @Getter
    private final List<SlimefunItemStack> particles = new ArrayList<>();

    DecayType(String representation) {
        this.representation = representation;
    }

    // This is required for unit tests
    void setParticles() {
        switch (this) {
            case ALPHA:
                this.particles.add(Element.HELIUM.getItem());
                break;
            case BETA_PLUS:
            case BETA_MINUS:
                this.particles.addAll(Arrays.asList(Lepton.ELECTRON.getItem(), Lepton.ELECTRON_NEUTRINO.getItem()));
                break;
            case DOUBLE_BETA_MINUS:
                this.particles.addAll(Arrays.asList(new SlimefunItemStack(Lepton.ELECTRON.getItem(), 2),
                    new SlimefunItemStack(Lepton.ELECTRON_NEUTRINO.getItem(), 2)));
                break;
            case PROTON:
                this.particles.add(Element.HYDROGEN.getItem());
                break;
            case PROTON_2:
                this.particles.add(new SlimefunItemStack(Element.HYDROGEN.getItem(), 2));
                break;
            case PROTON_3:
                this.particles.add(new SlimefunItemStack(Element.HYDROGEN.getItem(), 3));
                break;
            case NEUTRON:
                this.particles.add(Nucleon.NEUTRON.getItem());
                break;
            case NEUTRON_2:
                this.particles.add(new SlimefunItemStack(Nucleon.NEUTRON.getItem(), 2));
                break;
            case NEUTRON_3:
                this.particles.add(new SlimefunItemStack(Nucleon.NEUTRON.getItem(), 3));
                break;
            case NEUTRON_4:
                this.particles.add(new SlimefunItemStack(Nucleon.NEUTRON.getItem(), 4));
                break;
            case ELECTRON_CAPTURE:
                this.particles.addAll(Arrays.asList(Lepton.ELECTRON_NEUTRINO.getItem(), Boson.PHOTON.getItem()));
                break;
        }
    }

    @Nonnull
    public static DecayType getByRepresentation(String representation) {
        if (reprmap.isEmpty()) {
            for (DecayType decayType : values()) {
                reprmap.put(decayType.representation, decayType);
            }
        }

        DecayType decayType = reprmap.get(representation);
        if (decayType == null) {
            throw new IllegalArgumentException("Invalid representation, got: " + representation);
        }

        return decayType;
    }
}
