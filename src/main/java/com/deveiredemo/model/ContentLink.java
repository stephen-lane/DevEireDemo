package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;

public class ContentLink extends Link {

    @ToolUi.OnlyPathed
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public Object getHref() {
        return getContent();
    }

    @Override
    public LinkTarget getTarget() {
        return LinkTarget._top;
    }
}
