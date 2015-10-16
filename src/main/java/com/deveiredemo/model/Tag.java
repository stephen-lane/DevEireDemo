package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Taxon;
import com.psddev.cms.db.ToolUi;

import com.deveiredemo.migration.MigratedContent;

import java.util.ArrayList;
import java.util.List;

@ToolUi.GlobalFilter
public class Tag extends Content implements MigratedContent,
                                            Taxon {

    private String name;

    @Indexed
    @ToolUi.Hidden
    private Tag parent;

    @JunctionField("parent")
    private List<Tag> children;

    private boolean isInternal;

    @ToolUi.OnlyPathed
    private Content callToAction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setInternal(boolean isInternal) {
        this.isInternal = isInternal;
    }

    @Override
    public List<Tag> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public Content getCallToAction() {
        return callToAction;
    }

    public void setCallToAction(Content callToAction) {
        this.callToAction = callToAction;
    }
}
