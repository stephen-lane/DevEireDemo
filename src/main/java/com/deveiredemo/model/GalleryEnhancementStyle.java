package com.deveiredemo.model;

public enum GalleryEnhancementStyle {

    LOW_RES("Low Res"),
    HIGH_RES("High Res");

    private String label;

    private GalleryEnhancementStyle(String label) {
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
