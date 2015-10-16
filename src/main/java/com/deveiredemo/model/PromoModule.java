package com.deveiredemo.model;

import com.psddev.aod.UbikViewable;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;

import com.deveiredemo.view.PromoModuleListView;
import com.deveiredemo.view.PromoModuleSuperView;

@ViewMapping(PromoModuleListView.FromPromoModule.class)
@ViewMapping(PromoModuleSuperView.FromPromoModule.class)

@Recordable.DisplayName("Module (Promo)")
public class PromoModule extends Module implements ModuleTitled, UbikViewable {

    @ToolUi.DisplayBefore("backgroundTheme")
    private String description;

    @Required
    private PromoModuleSize moduleSize;

    @Embedded
    private PromoList content;

    private Link callToAction;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PromoModuleSize getModuleSize() {
        return moduleSize;
    }

    public void setModuleSize(PromoModuleSize moduleSize) {
        this.moduleSize = moduleSize;
    }

    public PromoList getContent() {
        return content;
    }

    public void setContent(PromoList content) {
        this.content = content;
    }

    public Link getCallToAction() {
        return callToAction;
    }

    public void setCallToAction(Link callToAction) {
        this.callToAction = callToAction;
    }

    @Override
    public String getLabel() {

        StringBuilder builder = new StringBuilder();

        if (moduleSize != null) {
            builder.append("[").append(moduleSize.getLabel()).append("] ");
        }

        String titleText = null;
        LinkOrText title = getModuleTitledData().getTitle();
        if (title != null) {
            titleText = title.getText();
        }

        if (titleText != null) {
            builder.append(titleText);

        } else if (content != null) {
            builder.append(content.getLabel());
        }

        if (builder.length() > 0) {
            return builder.toString();
        } else {
            return null;
        }
    }
}
