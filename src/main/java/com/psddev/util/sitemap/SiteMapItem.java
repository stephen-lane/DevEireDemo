package com.psddev.util.sitemap;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

import java.util.List;

public interface SiteMapItem extends Recordable {

    static final double DEFAULT_PRIORITY = 0.5;

    /**
     * Returns the site map entries for this object. In most cases this will
     * only be a single entry since there is usually a 1-to-1 mapping between
     * content and its permalink.
     */
    List<SiteMapEntry> getSiteMapEntries();

    @FieldInternalNamePrefix("cms.sitemap.")
    static class Data extends Modification<SiteMapItem> {

        private static final String TAB_NAME = "SEO";

        @ToolUi.Tab(TAB_NAME)
        private SiteMapChangeFrequency changeFrequency;

        @ToolUi.Tab(TAB_NAME)
        private Double priority;

        // --- Getters / Setters ---

        public SiteMapChangeFrequency getChangeFrequency() {
            return changeFrequency;
        }

        public void setChangeFrequency(SiteMapChangeFrequency changeFrequency) {
            this.changeFrequency = changeFrequency;
        }

        public Double getPriority() {
            if (priority == null) {
                return DEFAULT_PRIORITY;
            }

            if (priority < 0) {
                return 0.0;
            }

            if (priority > 1) {
                return 1.0;
            }
            return priority;
        }

        public void setPriority(Double priority) {
            this.priority = priority;
        }
    }
}
