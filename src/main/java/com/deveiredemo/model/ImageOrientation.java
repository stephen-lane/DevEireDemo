package com.deveiredemo.model;

public enum ImageOrientation {

    LANDSCAPE("Landscape"),
    PORTRAIT("Portrait"),
    SQUARE("Square"),
    UNKNOWN("Unknown");

    private String label;

    private ImageOrientation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
