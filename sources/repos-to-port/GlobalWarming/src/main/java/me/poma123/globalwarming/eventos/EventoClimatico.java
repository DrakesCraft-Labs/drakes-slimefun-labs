/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 */
package me.poma123.globalwarming.eventos;

import javax.annotation.Nonnull;

public enum EventoClimatico {
    OLA_DE_CALOR("&6&lOLA DE CALOR", "&eEl aire arde. La temperatura sube en todo el mundo.", 12.0, Cielo.DESPEJADO, 0.2),
    OLA_DE_FRIO("&b&lOLA DE FRIO", "&fUn frente frio cubre el mundo. Cuidado con lo que se congela.", -12.0, Cielo.DESPEJADO, 0.2),
    NEVADA("&f&lNEVADA", "&7Esta nevando en sitios donde no suele nevar.", -8.0, Cielo.LLUVIA, 0.15),
    TORMENTA("&8&lTORMENTA", "&7El cielo se cierra. Se viene una buena.", -6.0, Cielo.TORMENTA, 0.25),
    BOCHORNO("&e&lBOCHORNO", "&eCalor pegajoso y aire quieto. No corre ni una gota de viento.", 6.0, Cielo.DESPEJADO, 0.2);

    private final String titulo;
    private final String descripcion;
    private final double desviacionCelsius;
    private final Cielo cielo;
    private final double peso;

    private EventoClimatico(String titulo, String descripcion, double desviacionCelsius, Cielo cielo, double peso) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.desviacionCelsius = desviacionCelsius;
        this.cielo = cielo;
        this.peso = peso;
    }

    @Nonnull
    public String getTitulo() {
        return this.titulo;
    }

    @Nonnull
    public String getDescripcion() {
        return this.descripcion;
    }

    public double getDesviacionCelsius() {
        return this.desviacionCelsius;
    }

    @Nonnull
    public Cielo getCielo() {
        return this.cielo;
    }

    public double getPeso() {
        return this.peso;
    }

    public boolean nieva() {
        return this == NEVADA;
    }

    @Nonnull
    public String getClaveConfig() {
        return this.name().toLowerCase().replace('_', '-');
    }

    public static enum Cielo {
        DESPEJADO,
        LLUVIA,
        TORMENTA,
        IGUAL;

    }
}

