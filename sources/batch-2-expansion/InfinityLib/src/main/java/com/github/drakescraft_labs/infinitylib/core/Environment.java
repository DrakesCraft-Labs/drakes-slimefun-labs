package com.github.drakescraft_labs.infinitylib.core;

/**
 * Represents the current runtime environment
 */
public enum Environment {

    /**
     * This is used when InfinityLib is testing
     */
    LIBRARY_TESTING,

    /**
     * This is used when the Addon is testing
     */
    TESTING,

    /**
     * This is used when the Addon is running on an actual server
     */
    LIVE

}
