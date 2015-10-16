package com.deveiredemo.util;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.Tool;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.util.JspUtils;
import com.psddev.dari.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.ServletContext;

/**
 * Base implementation of a Tool that makes it easier to configure custom
 * CSS and Javascript in the CMS using Grunt as the front-end build system.
 */
public abstract class AbstractGruntTool extends Tool {

    @ToolUi.Tab("Debug")
    private boolean useNonMinifiedCss;

    @ToolUi.Tab("Debug")
    private boolean useNonMinifiedJavaScript;

    /**
     * @return true if the CSS should not be minified, likely for debugging
     * purposes.
     */
    public final boolean isUseNonMinifiedCss() {
        return useNonMinifiedCss;
    }

    public final void setUseNonMinifiedCss(boolean useNonMinifiedCss) {
        this.useNonMinifiedCss = useNonMinifiedCss;
    }

    /**
     * @return true if the Javascript should not be minified, likely for
     * debugging purposes.
     */
    public final boolean isUseNonMinifiedJavaScript() {
        return useNonMinifiedJavaScript;
    }

    public final void setUseNonMinifiedJavaScript(boolean useNonMinifiedJavaScript) {
        this.useNonMinifiedJavaScript = useNonMinifiedJavaScript;
    }

    /**
     * @return the base path for styles in the project.
     */
    protected abstract String getBaseStylePath();

    /**
     * @return the base path for scripts in the project.
     */
    protected abstract String getBaseScriptPath();

    /**
     * @return the list of paths to CSS or LESS files that should be included
     * on the CMS tool page.
     */
    protected abstract List<String> getToolCssPaths();

    /**
     * @return the list of paths to JS files that should be included on the CMS
     * tool page.
     */
    protected abstract List<String> getToolJsPaths();

    @Override
    public void writeHeaderAfterStyles(ToolPageContext page) throws IOException {
        boolean isMinified = !isUseNonMinifiedCss(page);
        getToolCssPaths().stream()
                .map((path) -> toolResource(page, getResourcePath(getBaseStylePath(), path, !isUseNonMinifiedCss(page))))
                .filter((path) -> path != null)
                .forEach((path) -> {
                    try {
                        page.writeTag("link",
                                "rel", "stylesheet",
                                "type", "text/" + (isMinified && !path.endsWith(".less") ? "css" : "less"),
                                "href", path);
                    } catch (IOException e) {
                        // do nothing
                    }
                });
    }

    @Override
    public void writeHeaderAfterScripts(ToolPageContext page) throws IOException {
        boolean isMinified = !isUseNonMinifiedJavaScript(page);
        getToolJsPaths().stream()
                .map((path) -> toolResource(page, getResourcePath(getBaseScriptPath(), path, isMinified)))
                .filter((path) -> path != null)
                .forEach((path) -> {
                    try {
                        page.writeStart("script",
                                "type", "text/javascript",
                                "src", path)
                                .writeEnd();
                    } catch (IOException e) {
                        // do nothing
                    }
                });
    }

    private boolean isUseNonMinifiedCss(ToolPageContext page) {
        return isUseNonMinifiedCss();
    }

    private boolean isUseNonMinifiedJavaScript(ToolPageContext page) {
        return isUseNonMinifiedJavaScript();
    }

    private String getResourcePath(String basePath, String resourcePath, boolean isMinified) {

        if (basePath == null || resourcePath == null) {
            return null;
        }

        if (isMinified) {

            // /assets/script/cms/project-cms.js --> /assets/script.min/cms/project-cms.js
            if (resourcePath.endsWith(".js")) {
                basePath = StringUtils.removeEnd(basePath, "/") + ".min";
            }

            // /assets/style/cms/project-cms.less --> /assets/style/cms/project-cms.min.css
            if (resourcePath.endsWith(".less")) {
                resourcePath = StringUtils.removeEnd(resourcePath, ".less") + ".min.css";
            }
        }

        return StringUtils.ensureSurrounding(basePath, "/") + StringUtils.removeStart(resourcePath, "/");
    }

    /**
     * Copied and modified from {@link ToolPageContext}. This is how the CMS
     * prevents caching of its resources.
     */
    private String toolResource(ToolPageContext page, String path, Object... parameters) {

        if (path == null) {
            return null;
        }

        ServletContext context = page.getServletContext();

        // Temp fix until the grunt js/less approach is solidified on the BSP side.
        if (path.startsWith("/_resource/") || path.startsWith("/assets/")) {
            path = JspUtils.getAbsolutePath(page.getRequest(), path, parameters);
        } else {
            path = page.toolUrl(this, path, parameters);
        }

        long lastModified = 0;

        try {
            URL resource = context.getResource(path);

            if (resource != null) {
                URLConnection resourceConnection = resource.openConnection();
                InputStream resourceInput = resourceConnection.getInputStream();

                try {
                    lastModified = resourceConnection.getLastModified();
                } finally {
                    resourceInput.close();
                }
            }

        } catch (IOException error) {
            // do nothing
        }

        if (lastModified == 0) {
            lastModified = (long) (Math.random() * Long.MAX_VALUE);
        }

        return StringUtils.addQueryParameters(
                StringUtils.addQueryParameters(path, parameters),
                "_", lastModified);
    }
}
