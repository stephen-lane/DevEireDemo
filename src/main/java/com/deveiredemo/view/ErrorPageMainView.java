package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.model.ErrorPage;
import com.deveiredemo.view.base.BspFullPageLayoutView;
import com.deveiredemo.view.base.BspMainView;

import java.util.Collections;

public interface ErrorPageMainView extends BspFullPageLayoutView {

    static class FromErrorPage extends AbstractViewCreator<ErrorPage> implements ErrorPageMainView {

        @Override
        public BspMainView getMain() {
            return () -> {
                Object mainView = request.createView(ErrorPageView.class, model);
                return mainView != null ? Collections.singletonList(mainView) : Collections.emptyList();
            };
        }
    }
}
