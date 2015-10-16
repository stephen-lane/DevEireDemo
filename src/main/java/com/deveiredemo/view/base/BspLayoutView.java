package com.deveiredemo.view.base;

public interface BspLayoutView {

    default Options getOptions() {
        return null;
    }

    static interface Options {

        String getModifierClass();
    }
}
