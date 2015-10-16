package com.deveiredemo.model;

public enum ModuleBackgroundTheme {

    WHITE("White", "white-back-jsg"),
    BLACK("Black", "black-back-jsg"),
    LIGHT_GRAY("Light Gray", "light-gray-back-jsg"),
    DARK_GRAY("Dark Gray", "dark-gray-back-jsg");

    private String label;
    private String cssClass;

    private ModuleBackgroundTheme(String label, String cssClass) {
        this.label = label;
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }

    public String getCssClass() {
        return cssClass;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
