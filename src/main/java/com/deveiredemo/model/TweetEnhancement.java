package com.deveiredemo.model;

import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Reference;

import com.deveiredemo.view.IframeView;

@ViewMapping(IframeView.FromTweetEnhancement.class)

@Recordable.DisplayName("Tweet Enhancement Overrides")
public class TweetEnhancement extends Reference {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Tweet getObject() {
        Object object = super.getObject();
        if (object instanceof Tweet) {
            return (Tweet) object;
        } else {
            return null;
        }
    }
}
