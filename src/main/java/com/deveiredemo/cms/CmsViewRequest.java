package com.deveiredemo.cms;

import com.psddev.cms.view.ViewCreator;
import com.psddev.cms.view.ViewRequest;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.util.JspUtils;
import com.psddev.dari.util.ObjectUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

public class CmsViewRequest implements ViewRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsViewRequest.class);

    private HttpServletRequest request;

    public CmsViewRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public <V> V createView(Class<V> viewClass, Object model) {

        ViewCreator<Object, V> vc = ViewCreator.createCreator(model, viewClass);

        if (vc != null) {
            return vc.createView(model, this);

        } else {
            LOGGER.warn("Could not find view creator of [" + viewClass
                    + "] for object of type [" + (model != null ? model.getClass() : "null") + "]!");
            return null;
        }
    }

    @Override
    public <T> Stream<T> getParameter(Class<T> returnType, String name) {
        String[] values = request.getParameterValues(name);
        return values != null && values.length > 0
                ? Arrays.stream(values)
                    .map((param) -> ObjectUtils.to(returnType, param))
                    .filter((param) -> param != null)
                : Stream.empty();
    }

    public Object getObject() {
        return request.getAttribute("object");
    }

    public ObjectField getField() {
        return (ObjectField) request.getAttribute("field");
    }

    public String getInputName() {
        return (String) request.getAttribute("inputName");
    }

    public boolean isFormPost() {
        return ObjectUtils.to(boolean.class, request.getAttribute("isFormPost"));
    }

    public String createId() {
        return JspUtils.createId(request);
    }
}
