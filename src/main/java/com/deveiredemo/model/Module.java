package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;

public abstract class Module extends Content {

    @ToolUi.DisplayFirst
    @ToolUi.Note(Constants.TOOL_UI_NOTE_CMS_USE_ONLY)
    private String name;

    private ModuleBackgroundTheme backgroundTheme = ModuleBackgroundTheme.WHITE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Never null.
     *
     * @return the background theme.
     */
    public ModuleBackgroundTheme getBackgroundTheme() {
        if (backgroundTheme == null) {
            backgroundTheme = ModuleBackgroundTheme.WHITE;
        }
        return backgroundTheme;
    }

    public void setBackgroundTheme(ModuleBackgroundTheme backgroundTheme) {
        this.backgroundTheme = backgroundTheme;
    }

    @Override
    public String getLabel() {
        if (this instanceof ModuleTitled) {
            LinkOrText title = ((ModuleTitled) this).getModuleTitledData().getTitle();
            return title != null ? title.getText() : null;

        } else {
            return null;
        }
    }
}
