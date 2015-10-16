package com.deveiredemo.cms;

import com.psddev.cms.tool.Plugin;
import com.psddev.cms.tool.Search;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

import com.deveiredemo.model.Constants;
import com.deveiredemo.util.AbstractGruntTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Recordable.DisplayName("J. Spieth Golf CMS")
public class JsgTool extends AbstractGruntTool {

    // --- Application Support ---

    @Override
    public String getApplicationName() {
        return Constants.TOOL_KEY;
    }

    // --- Tool Support --

    @Override
    public List<Plugin> getPlugins() {
        List<Plugin> plugins = new ArrayList<>();
        return plugins;
    }

    // --- Abstract Grunt Tool Support ---

    @Override
    protected String getBaseStylePath() {
        return "/assets/styles";
    }

    @Override
    protected String getBaseScriptPath() {
        return "/assets/scripts";
    }

    @Override
    protected List<String> getToolCssPaths() {
        return Arrays.asList("/cms/jsg-cms.less");
    }

    @Override
    protected List<String> getToolJsPaths() {
        return Collections.emptyList();
    }

    @Override
    public void writeHeaderAfterStyles(ToolPageContext page) throws IOException {
        super.writeHeaderAfterStyles(page);
    }

    @Override
    public void writeHeaderAfterScripts(ToolPageContext page) throws IOException {
        super.writeHeaderAfterScripts(page);
    }

    // --- Custom Search Support ---

    @Override
    public void initializeSearch(Search search, ToolPageContext page) {
        super.initializeSearch(search, page);
    }

    @Override
    public void updateSearchQuery(Search search, Query<?> query) {
        super.updateSearchQuery(search, query);
    }

    @Override
    public void writeSearchFilters(Search search, ToolPageContext page) throws IOException {
        super.writeSearchFilters(search, page);
    }
}
