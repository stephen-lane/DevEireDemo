package com.deveiredemo.model;

import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;

import com.deveiredemo.view.SharedModuleView;

@ViewMapping(SharedModuleView.FromSharedModule.class)

@Recordable.Embedded
public class SharedModule extends Module {

    @DisplayName("Module")
    private Module delegate;

    public Module getDelegate() {
        return delegate;
    }

    public void setDelegate(Module delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getLabel() {
        return delegate != null ? delegate.getLabel() : null;
    }
}
