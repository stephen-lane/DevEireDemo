package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Singleton;

@ToolUi.Referenceable(via = QuoteEnhancement.class)
public class Quote extends Record implements Singleton {

    @ToolUi.ReadOnly
    private String name = "Quote";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
