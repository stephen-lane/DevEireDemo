package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.dari.db.Recordable;

import java.util.ArrayList;
import java.util.List;

@Recordable.Embedded
public class ErrorPageStatus extends Content {

    private Integer statusCode;

    private String title;

    private String subTitle;

    private Image image;

    private List<Link> links;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Link> getLinks() {
        if (links == null) {
            links = new ArrayList<>();
        }
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public String getLabel() {
        StringBuilder builder = new StringBuilder();
        if (statusCode != null) {
            builder.append(statusCode);
        }

        if (statusCode != null && title != null) {
            builder.append(" - ");
        }

        if (title != null) {
            builder.append(title);
        }

        return builder.toString();
    }
}
