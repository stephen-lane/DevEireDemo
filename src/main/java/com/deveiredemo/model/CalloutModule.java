package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;

import com.deveiredemo.view.CalloutModuleView;

@ViewMapping(CalloutModuleView.FromCalloutModule.class)

@Recordable.DisplayName("Module (Callout)")
public class CalloutModule extends Module {

    public static final String TAB_NAME = "Colors";

    private String title;

    private String accentTitle;

    private Link link;

    @ToolUi.Tab(TAB_NAME)
    @ToolUi.ColorPicker
    private String textColor;

    @ToolUi.Tab(TAB_NAME)
    @ToolUi.ColorPicker
    private String backgroundColor;

    @ToolUi.Tab(TAB_NAME)
    @ToolUi.ColorPicker
    private String buttonColor;

    @ToolUi.Tab(TAB_NAME)
    @ToolUi.ColorPicker
    private String buttonTextColor;

    public String getTitle() {
        return title;
    }

    public String getAccentTitle() {
        return accentTitle;
    }

    public Link getLink() {
        return link;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public String getButtonTextColor() {
        return buttonTextColor;
    }

    @Override
    public String getLabel() {
        String label = "";
        String title = getTitle();
        String accentTitle = getAccentTitle();

        if (title != null) {
            label += title;
        }

        if (accentTitle != null) {
            if (label.length() > 0) {
                label += " | ";
            }
            label += accentTitle;
        }

        return label;
    }
}
