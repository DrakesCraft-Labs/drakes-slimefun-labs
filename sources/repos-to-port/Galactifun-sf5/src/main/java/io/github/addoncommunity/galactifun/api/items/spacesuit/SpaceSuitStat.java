package io.github.addoncommunity.galactifun.api.items.spacesuit;

import java.util.Objects;


@SuppressWarnings("ClassCanBeRecord")
public final class SpaceSuitStat {

    public static final SpaceSuitStat HEAT_RESISTANCE = new SpaceSuitStat("&cHeat Resistance");
    public static final SpaceSuitStat COLD_RESISTANCE = new SpaceSuitStat("&bCold Resistance");
    public static final SpaceSuitStat RADIATION_RESISTANCE = new SpaceSuitStat("&4Radiation Resistance");

    private final String name;


    public SpaceSuitStat(String name) {
        this.name = name;
    }

    public String name() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceSuitStat that = (SpaceSuitStat) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
