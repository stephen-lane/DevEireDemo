package com.deveiredemo.model;

import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

/**
 * Objects that have an associated section.
 */
public interface Sectionable extends Recordable {

    /**
     * Type safe shortcut to get the data modification.
     *
     * @return the data modification.
     */
    default Data getSectionableData() {
        return as(Sectionable.Data.class);
    }

    @FieldInternalNamePrefix("jsg.sectionable.")
    public static class Data extends Modification<Sectionable> {

        private Section section;

        public Section getSection() {
            return section;
        }

        public void setSection(Section section) {
            this.section = section;
        }
    }
}
