package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.MainViewClass;
import com.psddev.cms.view.PageViewClass;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Singleton;
import com.psddev.dari.util.ObjectUtils;

import com.deveiredemo.view.ErrorPageMainView;
import com.deveiredemo.view.ErrorPageView;
import com.deveiredemo.view.PageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageViewClass(PageView.class)
@MainViewClass(ErrorPageMainView.class)

@ViewMapping(PageView.FromObject.class)
@ViewMapping(ErrorPageMainView.FromErrorPage.class)
@ViewMapping(ErrorPageView.FromErrorPage.class)

public class ErrorPage extends Content implements Directory.Item,
                                                  Singleton {

    private String name;

    private ErrorPageStatus defaultStatus;

    private List<ErrorPageStatus> statuses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ErrorPageStatus getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(ErrorPageStatus defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

    public List<ErrorPageStatus> getStatuses() {
        if (statuses == null) {
            statuses = new ArrayList<>();
        }
        return statuses;
    }

    public void setStatuses(List<ErrorPageStatus> statuses) {
        this.statuses = statuses;
    }

    public Map<Integer, ErrorPageStatus> getStatusesByCode() {

        Map<Integer, ErrorPageStatus> statusMap = new HashMap<>();

        for (ErrorPageStatus status : getStatuses()) {

            Integer statusCode = status.getStatusCode();

            if (statusCode != null && statusCode > 0) {
                statusMap.put(statusCode, status);
            }
        }

        return statusMap;
    }

    public String getTitleByStatusCode(Integer statusCode) {
        String title = null;

        ErrorPageStatus status = getStatusesByCode().get(statusCode);
        if (status != null) {
            title = status.getTitle();
        }

        if (title == null) {
            status = getDefaultStatus();
            if (status != null) {
                title = status.getTitle();
            }
        }

        return title;
    }

    public String getSubTitleByStatusCode(Integer statusCode) {
        String subTitle = null;

        ErrorPageStatus status = getStatusesByCode().get(statusCode);
        if (status != null) {
            subTitle = status.getSubTitle();
        }

        if (subTitle == null) {
            status = getDefaultStatus();
            if (status != null) {
                subTitle = status.getSubTitle();
            }
        }

        return subTitle;
    }

    public Image getImageByStatusCode(Integer statusCode) {
        Image image = null;

        ErrorPageStatus status = getStatusesByCode().get(statusCode);
        if (status != null) {
            image = status.getImage();
        }

        if (image == null) {
            status = getDefaultStatus();
            if (status != null) {
                image = status.getImage();
            }
        }

        return image;
    }

    public List<Link> getLinksByStatusCode(Integer statusCode) {
        List<Link> links = null;

        ErrorPageStatus status = getStatusesByCode().get(statusCode);
        if (status != null) {
            links = status.getLinks();
        }

        if (ObjectUtils.isBlank(links)) {
            status = getDefaultStatus();
            if (status != null) {
                links = status.getLinks();
            }
        }

        if (links == null) {
            links = new ArrayList<>();
        }

        return links;
    }

    @Override
    public String createPermalink(Site site) {
        return "/error";
    }
}
