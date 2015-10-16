package com.deveiredemo.servlet;

import com.psddev.cms.view.ViewOutput;
import com.psddev.cms.view.ViewRenderer;
import com.psddev.cms.view.ViewRequest;
import com.psddev.dari.db.Query;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.PromoListDynamic;
import com.deveiredemo.model.PromoModule;
import com.deveiredemo.model.PromoModuleSize;
import com.deveiredemo.view.PromoModuleListView;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(Constants.AJAX_ENDPOINT_PROMO_MODULE_LOAD_MORE)
public class PromoModuleLoadMoreServlet extends HttpServlet {

    public static final String PROMO_LIST_ID_PARAMETER = "id";
    public static final String PROMO_LIST_MODULE_SIZE_PARAMETER = "size";
    public static final String PROMO_LIST_PAGE_PARAMETER = "page";

    @Override
    protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {

        ViewRequest viewRequest = new AjaxViewRequest(httpRequest);

        PromoListDynamic dynamicList = Query.findById(PromoListDynamic.class, viewRequest.getParameter(UUID.class,
                PROMO_LIST_ID_PARAMETER).findFirst().orElse(null));

        if (dynamicList != null) {

            PromoModuleSize size = PromoModuleSize.findByInternalName(viewRequest.getParameter(String.class,
                    PROMO_LIST_MODULE_SIZE_PARAMETER).findFirst().orElse(null));

            if (size != null) {

                // update the dynamic list.

                int page = viewRequest.getParameter(Integer.class, PROMO_LIST_PAGE_PARAMETER).findFirst().orElse(1);
                if (page <= 0) {
                    page = 1;
                }

                int limit = dynamicList.getLimit();

                dynamicList.setOffset((page - 1) * limit);

                PromoModule module = new PromoModule();
                module.setModuleSize(size);
                module.setContent(dynamicList);

                // TODO: Move this to a helper method for all the servlets to use.
                PromoModuleListView view = viewRequest.createView(PromoModuleListView.class, module);
                if (view != null) {

                    ViewRenderer renderer = ViewRenderer.createRenderer(view);
                    if (renderer != null) {

                        ViewOutput viewOutput = renderer.render(view);
                        String output = viewOutput.get();
                        if (output != null) {
                            httpResponse.getWriter().write(output);
                        }
                    }
                }
            }
        }
    }
}
