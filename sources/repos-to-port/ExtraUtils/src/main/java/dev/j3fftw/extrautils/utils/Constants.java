package dev.j3fftw.extrautils.utils;

import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;

public final class Constants {

    public static final int SERVER_TICK_RATE = 20;

    public static final int CUSTOM_TICKER_DELAY = Slimefun.getCfg().getInt("URID.custom-ticker-delay");

    //TODO
//    public static final NamespacedKey GLOW_ENCHANT = new NamespacedKey(LiteXpansion.getInstance(),
//        "glow_enchant");

    private Constants() {}

}
