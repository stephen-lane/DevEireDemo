package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

import java.util.ArrayList;
import java.util.List;

public interface Taggable extends Recordable {

    /**
     * Type safe shortcut to get the data modification.
     *
     * @return the data modification.
     */
    default Data getTaggableData() {
        return as(Taggable.Data.class);
    }

    @BeanProperty("taggable")
    public static class Data extends Modification<Taggable> {

        @ToolUi.Heading("Tags")
        @Indexed
        private List<Tag> tags;

        public List<Tag> getTags() {
            if (tags == null) {
                tags = new ArrayList<>();
            }
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }
    }
}
