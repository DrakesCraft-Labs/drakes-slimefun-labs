package com.github.drakescraft_labs.slimechem.implementation.atomic.isotopes;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.github.drakescraft_labs.slimechem.lists.Constants;
import com.github.drakescraft_labs.slimechem.utils.StringUtil;
import com.github.drakescraft_labs.slimechem.utils.SuperNum;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IsotopeLoader {
    private final String isotopeJSON;
    Map<Isotope, StringUtil.NumberAndString> decayProductMap = new HashMap<>();

    public IsotopeLoader() {
        if (!Constants.isTestingEnvironment) {
            for (DecayType decayType : DecayType.values()) {
                decayType.setParticles();
            }
        }
        try {
            this.isotopeJSON = StringUtil.getResourceAsString("isotopes.json");
        } catch (IOException e) {
            throw new JsonIOException("Failed to load isotope file", e);
        }
    }

    public void load() {

        this.decayProductMap.clear();

        JsonArray jsonArray = new JsonParser().parse(this.isotopeJSON).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            int mass = jsonObject.get("mass").getAsInt();
            String abbr = jsonObject.get("element").getAsString();
            DecayType decayType;
            try {
                decayType = DecayType.getByRepresentation(
                    jsonObject.get("decay").getAsString()
                );
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    "Failed to load decay type for isotope " + SuperNum.fromInt(mass) + abbr, e);
            }
            Isotope isotope = Isotope.addIsotope(mass, abbr, decayType);
            if (decayType != DecayType.STABLE) {
                this.decayProductMap.put(isotope, new StringUtil.NumberAndString(
                    jsonObject.get("pmass").getAsInt(),
                    jsonObject.get("pelement").getAsString()
                ));
            }
        }
    }

    public void loadDecayProducts() {
        for (Isotope isotope : this.decayProductMap.keySet()) {
            StringUtil.NumberAndString decayData = this.decayProductMap.get(isotope);

            try {
                isotope.loadDecayProduct(decayData.getNumber(), decayData.getString());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format(
                    "Failed to load decay product for isotope %s",
                    isotope.toString()
                ), e);
            }
        }
    }
}
