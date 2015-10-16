package com.deveiredemo.model;

public enum PromoModuleSize {

    SMALL("Small", "small", "Rows of 3."),
    MEDIUM("Medium", "medium", "Rows of 2."),
    LARGE("Large", "large", "More than 1 converts it to a carousel."),
    SUPER("Super", "super", "More than 1 converts it to a carousel.");

    private String label;

    private String internalName;

    private String note;

    private PromoModuleSize(String label, String internalName, String note) {
        this.label = label;
        this.internalName = internalName;
        this.note = note;
    }

    public String getLabel() {
        return label;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    public static PromoModuleSize findByInternalName(String internalName) {

        for (PromoModuleSize size : PromoModuleSize.values()) {
            if (size.getInternalName().equals(internalName)) {
                return size;
            }
        }

        return null;
    }
}
