package com.deveiredemo.cms;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.cms.view.ViewRequest;

public class AbstractCmsViewCreator<M> extends AbstractViewCreator<M> {

    protected CmsViewRequest request;

    @Override
    public Object createView(M model, ViewRequest request) {
        super.createView(model, request);

        if (request instanceof CmsViewRequest) {
            this.request = (CmsViewRequest) request;

        } else {
            throw new IllegalArgumentException("Expected request of type ["
                    + CmsViewRequest.class.getName() + "] but got ["
                    + request.getClass().getName() + "] instead.");
        }
        return this;
    }
}
