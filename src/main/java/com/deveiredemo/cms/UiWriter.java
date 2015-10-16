package com.deveiredemo.cms;

import com.psddev.cms.db.ImageTag;
import com.psddev.cms.db.StandardImageSize;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;

import java.io.IOException;
import java.io.StringWriter;
import java.util.UUID;

import javax.servlet.jsp.PageContext;

/**
 * Helper class for writing dynamic {@link com.psddev.cms.db.ToolUi @ToolUi.NoteHtml} annotations.
 */
public class UiWriter extends ToolPageContext {

    private final PageContext pageContext;
    private final StringWriter writer;

    /** Creates an instance based on the given {@code pageContext}. */
    public UiWriter(PageContext pageContext) {
        super(pageContext);
        this.pageContext = pageContext;
        setDelegate(writer = new StringWriter());
    }

    public PageContext getPageContext() {
        return pageContext;
    }

    /**
     * Writes the HTML for a CMS content edit link, which will generate a popup
     * window when clicked.
     *
     * @param object either a UUID or a Recordable object.
     * @param linkText the link text
     * @return the current writer
     * @throws java.io.IOException
     */
    public UiWriter writeCmsObjectLink(Object object, String linkText) throws IOException {
        return writeCmsObjectLinkHelper(object, null, linkText, null);
    }

    /**
     * Writes the HTML for a CMS content edit link (with icon), which will
     * generate a popup window when clicked.
     *
     * @param object either a UUID or a Recordable object.
     * @param linkText the link text
     * @return the current writer
     * @throws java.io.IOException
     */
    public UiWriter writeCmsObjectEditLink(Object object, String linkText) throws IOException {
        return writeCmsObjectLinkHelper(object, null, linkText, "objectId-edit");
    }

    private UiWriter writeCmsObjectLinkHelper(Object object, UUID typeId, String linkText, String linkClass) throws IOException {

        UUID id = null;
        String label = null;
        State state = null;

        if (object instanceof UUID) {
            id = (UUID) object;

        } else {
            state = State.getInstance(object);
            if (state != null) {
                id = state.getId();
            }
        }

        if (!ObjectUtils.isBlank(linkText)) {
            label = linkText;

        } else if (state != null) {
            label = state.getLabel();
        }

        if (id != null && label != null) {

            writeStart("a",
                    "class", linkClass,
                    "target", "objectId-" + createId(),
                    "href", cmsUrl("/content/edit.jsp", "id", id, "typeId", typeId),
                    "style", "display: inline;");

                writeHtml(label);
            writeEnd();
        }

        return this;
    }

    public UiWriter writeCmsPreviewImage(StorageItem imageFile) throws IOException {
        return writeCmsPreviewImage(imageFile, 100);
    }

    public UiWriter writeCmsPreviewImage(StorageItem imageFile, Integer height) throws IOException {
        writeTag("img",
                "src", new ImageTag.Builder(imageFile).setHeight(height).toUrl(),
                "style", "width: auto; height: " + height + "px; border:solid 1px #cdcdcd; padding: 3px;");
        return this;
    }

    public UiWriter writeCmsPreviewImage(StorageItem imageFile, String size) throws IOException {
        StandardImageSize standardImageSize = null;
        for (StandardImageSize standardSize : StandardImageSize.findAll()) {
            if (standardSize.getInternalName().equals(size)) {
                standardImageSize = standardSize;
                break;
            }
        }

        writeTag("img",
                "src", new ImageTag.Builder(imageFile).setStandardImageSize(standardImageSize).setHeight(100).toUrl(),
                "style", "border:solid 1px #cdcdcd; padding: 3px;");
        return this;
    }

    public UiWriter writeWarningHtml(String html) throws IOException {

        writeStart("span", "style", "background-color:#e90;color:#fff;border-radius:3px;padding:2px;margin-right:4px;display:inline-block;margin-top:4px;");
            writeHtml("WARNING:");
        writeEnd();
        writeHtml(html);
        return this;
    }

    public UiWriter writeAlertHtml(String html) throws IOException {

        writeStart("span", "style", "background-color:#c20;color:#fff;border-radius:3px;padding:2px;margin-right:4px;display:inline-block;margin-top:4px;");
        writeHtml("ALERT:");
        writeEnd();
        writeHtml(html);
        return this;
    }

    public UiWriter writeNoticeHtml(String html) throws IOException {

        writeStart("span", "style", "background-color:#31708f;color:#fff;border-radius:3px;padding:2px;margin-right:4px;display:inline-block;margin-top:4px;");
        writeHtml("NOTICE:");
        writeEnd();
        writeHtml(html);
        return this;
    }

    /**
     * Returns the contents of this writer as a String.
     *
     * @return the markup stored in this writer.
     */
    public String toHtml() {
        return toString();
    }

    @Override
    public String toString() {
        return writer.toString();
    }

    /**
     * Returns an absolute version of the given {@code path} in context
     * of the CMS, modified by the given {@code parameters}.
     *
     * @param path May be {@code null}.
     * @param parameters May be {@code null}.
     */
    public String jsgCmsUrl(String path, Object... parameters) {
        return toolUrl(getToolByClass(JsgTool.class), path, parameters);
    }
}
