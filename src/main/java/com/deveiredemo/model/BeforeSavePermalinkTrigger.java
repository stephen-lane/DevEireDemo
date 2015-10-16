package com.deveiredemo.model;

import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.tool.AuthenticationFilter;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PageContextFilter;

import com.deveiredemo.util.ContentStateUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Attempts to generate a permalink on beforeSave() if there are no permalinks
 * currently present on the object.
 */
public interface BeforeSavePermalinkTrigger extends Recordable {

    public static final String SKIP_PERMALINK_STATE_EXTRA = BeforeSavePermalinkTrigger.class.getName() + ".skip";

    default Data getBeforeSavePermalinkTriggerData() {
        return as(BeforeSavePermalinkTrigger.Data.class);
    }

    public static class Data extends Modification<BeforeSavePermalinkTrigger> {

        @Override
        public void beforeSave() {
            if (getState().isNew() && !ContentStateUtils.isContentStateRequest()
                    && !ObjectUtils.to(boolean.class, getState().getExtra(SKIP_PERMALINK_STATE_EXTRA))) {

                Directory.Data dirData = as(Directory.Data.class);

                if (!Directory.PathsMode.MANUAL.equals(dirData.getPathsMode()) && dirData.getPaths().isEmpty()) {

                    Site site = null;
                    ToolUser currentToolUser = getCurrentToolUser();
                    if (currentToolUser != null) {
                        site = currentToolUser.getCurrentSite();
                    }

                    for (Directory.Path path : State.getInstance(this).as(Directory.ObjectModification.class).createPaths(site)) {
                        dirData.addPath(path.getSite(), path.getPath(), path.getType());
                    }
                }
            }
        }

        private ToolUser getCurrentToolUser() {
            HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();
            return request != null ? AuthenticationFilter.Static.getUser(request) : null;
        }
    }

    /**
     * Sets a flag on the object's State extras indicating that the automatic
     * permalink generation on beforeSave() should be skipped.
     *
     * @param object the object to update.
     */
    public static void skipPermalinkTrigger(BeforeSavePermalinkTrigger object) {
        object.getState().getExtras().put(SKIP_PERMALINK_STATE_EXTRA, true);
    }

    /**
     * Checks whether permalink trigger will fire for the object on beforeSave() or not.
     *
     * @param object the object to check.
     * @return true if the automatic permalink trigger will be skipped for the object.
     */
    public static boolean isSkipPermalinkTrigger(BeforeSavePermalinkTrigger object) {
        return ObjectUtils.to(boolean.class, object.getState().getExtra(SKIP_PERMALINK_STATE_EXTRA));
    }
}
