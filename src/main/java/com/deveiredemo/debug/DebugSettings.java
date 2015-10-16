package com.deveiredemo.debug;

import com.psddev.dari.util.CodeDebugServlet;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.StringUtils;
import com.psddev.util.SettingsOverride;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class DebugSettings implements SettingsOverride {

    @Override
    public Map<String, Object> getOverrideSettings() {

        Map<String, Object> overrides = new HashMap<>();

        List<String> debugCodeImports = Arrays.asList(
                "com.psddev.cms.db.*",
                "java.io.*",
                "java.lang.reflect.*",
                "java.net.*",
                "org.joda.time.*");

        CollectionUtils.putByPath(overrides,
                CodeDebugServlet.INCLUDE_IMPORTS_SETTING,
                StringUtils.join(debugCodeImports, " "));

        return overrides;
    }
}
