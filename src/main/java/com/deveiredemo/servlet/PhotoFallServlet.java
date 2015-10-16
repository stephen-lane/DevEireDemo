package com.deveiredemo.servlet;

import com.psddev.cms.view.ViewOutput;
import com.psddev.cms.view.ViewRenderer;
import com.psddev.cms.view.ViewRequest;
import com.psddev.dari.db.Query;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.PhotoFallPage;
import com.deveiredemo.view.GalleryGridView;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(Constants.AJAX_ENDPOINT_PHOTO_FALL)
public class PhotoFallServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ViewRequest viewRequest = new AjaxViewRequest(request);

        PhotoFallPage photoFallPage = Query.from(PhotoFallPage.class).first();
        if (photoFallPage != null) {

            GalleryGridView view = viewRequest.createView(GalleryGridView.FromPhotoFallPage.class, photoFallPage);
            if (view != null) {

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
}
