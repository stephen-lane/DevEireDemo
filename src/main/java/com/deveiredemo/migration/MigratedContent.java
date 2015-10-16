package com.deveiredemo.migration;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

import java.util.Map;
import java.util.UUID;

/**
 * To specify that this content could be migrated from another source, i.e. Jordan Spieth Version 2.x
 */
@ToolUi.Hidden
public interface MigratedContent extends Recordable {

    public static final String FIELD_NAME_PREFIX = "jsg.migration.";

    public static final String FOREIGN_ID_FIELD = FIELD_NAME_PREFIX + "foreignId";

    @FieldInternalNamePrefix(FIELD_NAME_PREFIX)
    public static class Data extends Modification<MigratedContent> {

        @Indexed
        @ToolUi.Hidden
        private UUID foreignId;

        @ToolUi.Hidden
        private Map<String, Object> foreignState;

        public UUID getForeignId() {
            return foreignId;
        }

        public void setForeignId(UUID foreignId) {
            this.foreignId = foreignId;
        }

        public Map<String, Object> getForeignState() {
            return foreignState;
        }

        public void setForeignState(Map<String, Object> foreignState) {
            this.foreignState = foreignState;
        }
    }
}
