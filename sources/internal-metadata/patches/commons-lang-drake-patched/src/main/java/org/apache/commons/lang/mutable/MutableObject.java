package org.apache.commons.lang.mutable;

/**
 * Minimal MutableObject compatibility class for legacy Slimefun tests.
 */
public class MutableObject {

    private Object value;

    public MutableObject() {
    }

    public MutableObject(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
