package org.apache.commons.lang.math;

/**
 * Minimal IntRange compatibility class for Commons Lang 2 callers.
 */
public final class IntRange {

    private final int minimum;
    private final int maximum;

    public IntRange(int value) {
        this(value, value);
    }

    public IntRange(int minimum, int maximum) {
        if (minimum > maximum) {
            throw new IllegalArgumentException("Minimum value must not be greater than maximum value");
        }
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public int getMinimumInteger() {
        return minimum;
    }

    public int getMaximumInteger() {
        return maximum;
    }

    public boolean containsInteger(int value) {
        return value >= minimum && value <= maximum;
    }
}
