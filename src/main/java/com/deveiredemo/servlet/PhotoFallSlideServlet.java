package com.deveiredemo.servlet;

import com.psddev.cms.view.ViewOutput;
import com.psddev.cms.view.ViewRenderer;
import com.psddev.cms.view.ViewRequest;
import com.psddev.dari.db.Query;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.Image;
import com.deveiredemo.view.GalleryFullscreenSlideView;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(Constants.AJAX_ENDPOINT_PHOTO_FALL_SLIDE)
public class PhotoFallSlideServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {

        ViewRequest viewRequest = new AjaxViewRequest(httpRequest);

        Image image = Query.findById(Image.class, viewRequest.getParameter(UUID.class,
                GalleryFullscreenSlideView.FromImage.IMAGE_ID_PARAMETER).findFirst().orElse(null));

        if (image != null && image.getPublishDate() != null) {

            GalleryFullscreenSlideView view = viewRequest.createView(GalleryFullscreenSlideView.class, image);
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
