package com.deveiredemo.view;

import com.psddev.cms.db.PageFilter;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.PageContextFilter;

import javax.servlet.http.HttpServletRequest;

public abstract class JsgViewCreator<M> extends AbstractViewCreator<M> {

    protected Object getMainModel() {
        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();
        if (request != null) {
            return PageFilter.Static.getMainObject(request);
        }
        return null;
    }
}
