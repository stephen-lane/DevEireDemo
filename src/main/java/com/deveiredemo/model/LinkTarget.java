package com.deveiredemo.model;

public enum LinkTarget {

    _blank("New Window/Tab"),
    _top("Same Window");

    private String label;

    private LinkTarget(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public String getHtmlAttributeValue() {
        return name();
    }
}
