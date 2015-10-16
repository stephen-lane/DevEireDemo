package com.deveiredemo.servlet;

import com.psddev.cms.view.ViewOutput;
import com.psddev.cms.view.ViewRenderer;
import com.psddev.cms.view.ViewRequest;

import com.deveiredemo.model.Constants;
import com.deveiredemo.view.GalleryFallPageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(Constants.AJAX_ENDPOINT_GALLERY_FALL)
public class GalleryFallServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(GalleryFallServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ViewRequest viewRequest = new AjaxViewRequest(request);

        int page = viewRequest.getParameter(int.class, GalleryFallPageView.PAGE_PARAMETER).findFirst().orElse(1);

        List<Object> views = GalleryFallPageView.createComponents(viewRequest, page);

        for (Object view : views) {

            ViewRenderer renderer = ViewRenderer.createRenderer(view);

            if (renderer != null) {

                ViewOutput viewOutput = renderer.render(view);
                String output = viewOutput.get();
                if (output != null) {
                    response.getWriter().write(output);
                }
            }
        }
    }
}
