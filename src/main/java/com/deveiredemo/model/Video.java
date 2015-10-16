package com.deveiredemo.model;

import com.psddev.aod.UbikContent;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

import com.deveiredemo.migration.MigratedContent;

@ToolUi.Hidden(value = false)
@ToolUi.PreviewField("getThumbnail")
public interface Video extends Directory.Item,
                               MigratedContent,
                               Promotable,
                               Recordable,
                               Taggable,
                               UbikContent {

    boolean isPlayable();

    StorageItem getThumbnail();

    static class Data extends Modification<Video> {

        @ToolUi.Filterable
        @Indexed @ToolUi.Hidden
        public boolean isPlayable() {
            return getOriginalObject().isPlayable();
        }

        @Indexed @ToolUi.Hidden
        public StorageItem getThumbnail() {
            return getOriginalObject().getThumbnail();
        }
    }
}
