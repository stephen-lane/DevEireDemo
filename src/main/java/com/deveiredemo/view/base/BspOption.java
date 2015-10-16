package com.deveiredemo.view.base;

public interface BspOption {

    String getKey();

    Object getValue();

    static BspOption fromKeyValue(String key, Object value) {
        return new BspOption() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Object getValue() {
                return value;
            }
        };
    }
}
