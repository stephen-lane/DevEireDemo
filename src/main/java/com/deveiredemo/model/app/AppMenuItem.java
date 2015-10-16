package com.deveiredemo.model.app;

import com.psddev.aod.NavigationScreen;
import com.psddev.aod.UbikContent;
import com.psddev.aod.UbikViewable;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Recordable.Embedded
public class AppMenuItem extends Record implements UbikContent {

    private String text;

    private StorageItem image;

    @ToolUi.Note("Screen to be pushed onto the Navigation Stack on click of this Menu Item.")
    private NavigationScreen onClickScreen;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public StorageItem getImage() {
        return image;
    }

    public void setImage(StorageItem image) {
        this.image = image;
    }

    public NavigationScreen getOnClickScreen() {
        return onClickScreen;
    }

    public void setOnClickScreen(NavigationScreen onClickScreen) {
        this.onClickScreen = onClickScreen;
    }

    @Override
    public UbikViewable getActionObject() {
        return onClickScreen;
    }

    @Override
    public String getCardTitle() {
        return text;
    }

    @Override
    public StorageItem getCardImage() {
        return image;
    }
}
