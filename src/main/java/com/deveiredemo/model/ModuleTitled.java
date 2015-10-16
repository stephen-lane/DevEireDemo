package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

public interface ModuleTitled extends Recordable {

    /**
     * Type safe shortcut to get the data modification.
     *
     * @return the data modification.
     */
    default Data getModuleTitledData() {
        return as(ModuleTitled.Data.class);
    }

    @BeanProperty("moduleTitled")
    static class Data extends Modification<ModuleTitled> {

        @ToolUi.DisplayFirst
        private LinkOrText title;

        public LinkOrText getTitle() {
            return title;
        }

        public void setTitle(LinkOrText title) {
            this.title = title;
        }
    }
}
