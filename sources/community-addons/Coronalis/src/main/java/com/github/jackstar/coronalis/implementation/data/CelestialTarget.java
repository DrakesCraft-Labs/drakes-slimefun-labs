package com.github.jackstar.coronalis.implementation.data;

import javax.annotation.Nonnull;

/**
 * Catálogo completo de objetivos celestes del observatorio Coronalis.
 *
 * <p>Reemplaza el inner-class {@code AstroTarget} en {@code ControlConsole},
 * añadiendo sistema de tiers, requerimientos de telescopios y multiplicadores de XP.</p>
 */
public enum CelestialTarget {

    HORSEHEAD_NEBULA(
        "Nebulosa Cabeza de Caballo", 95.1, 14.5,
        "Nube de gas interestelar densa y fría en Orión. Revelada mediante radioastronomía de moléculas orgánicas.",
        Tier.EASY, 1.0, 10
    ),
    CRAB_NEBULA(
        "Nebulosa del Cangrejo", 83.6, 22.0,
        "Remanente de supernova brillante en Tauro. Emite radiación de sincrotrón continua de alta energía.",
        Tier.INTERMEDIATE, 1.1, 11
    ),
    ANDROMEDA(
        "Galaxia de Andrómeda", 310.2, 41.2,
        "Galaxia espiral vecina a 2.5 millones de años luz. Sus líneas HI de 21cm muestran desplazamiento al azul.",
        Tier.INTERMEDIATE, 1.2, 12
    ),
    PSR_B1919(
        "Púlsar PSR B1919+21", 295.4, 50.8,
        "Primera estrella de neutrones pulsante descubierta. Período regular de 1.337 segundos exactos.",
        Tier.HARD, 1.5, 13
    ),
    SAGITTARIUS_A(
        "Sagitario A*", 177.3, 29.1,
        "Agujero negro supermasivo de 4 millones de masas solares en el centro de la Vía Láctea.",
        Tier.HARD, 1.6, 14
    ),
    KEPLER_186F(
        "Exoplaneta Kepler-186f", 45.9, 68.7,
        "Planeta terrestre en zona habitable de su estrella anfitriona. Firmas térmicas infrarrojas detectadas.",
        Tier.LEGENDARY, 2.0, 15
    ),
    M87_BLACK_HOLE(
        "Agujero Negro M87*", 215.0, 37.8,
        "Agujero negro de 6.5 mil millones de masas solares con chorro relativista colosal.",
        Tier.LEGENDARY, 2.5, 16
    );

    /** Nombre para mostrar en menús (en español). */
    public final String displayName;
    /** Azimut objetivo en grados. */
    public final double az;
    /** Elevación objetivo en grados. */
    public final double el;
    /** Descripción científica del objeto. */
    public final String description;
    /** Nivel de dificultad. */
    public final Tier tier;
    /** Multiplicador de XP (aplicado al XP base del config). */
    public final double xpMultiplier;
    /** Slot del ChestMenu donde aparece este objetivo (fila 1, slots 10-16). */
    public final int menuSlot;

    CelestialTarget(@Nonnull String displayName, double az, double el,
                    @Nonnull String description, @Nonnull Tier tier,
                    double xpMultiplier, int menuSlot) {
        this.displayName  = displayName;
        this.az           = az;
        this.el           = el;
        this.description  = description;
        this.tier         = tier;
        this.xpMultiplier = xpMultiplier;
        this.menuSlot     = menuSlot;
    }

    /** Número mínimo de radiotelescopios para observar este objetivo. */
    public int getMinTelescopes() {
        return switch (tier) {
            case EASY         -> 1;
            case INTERMEDIATE -> 2;
            case HARD         -> 3;
            case LEGENDARY    -> 4;
        };
    }

    /**
     * Busca por displayName; devuelve el primer elemento (HORSEHEAD) como fallback.
     */
    @Nonnull
    public static CelestialTarget byName(@Nonnull String name) {
        for (CelestialTarget t : values()) {
            if (t.displayName.equals(name)) return t;
        }
        return HORSEHEAD_NEBULA;
    }

    // ── Tier ────────────────────────────────────────────────────────────────

    public enum Tier {
        EASY        ("§a✦ FÁCIL",       "§a"),
        INTERMEDIATE("§e✦ INTERMEDIO",  "§e"),
        HARD        ("§c✦ DIFÍCIL",     "§c"),
        LEGENDARY   ("§5✦ LEGENDARIO",  "§5");

        /** Etiqueta completa con color para el lore. */
        public final String label;
        /** Código de color para prefijos. */
        public final String color;

        Tier(@Nonnull String label, @Nonnull String color) {
            this.label = label;
            this.color = color;
        }
    }
}
