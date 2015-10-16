package com.deveiredemo.cms;

import com.psddev.cms.db.ToolAuthenticationPolicy;
import com.psddev.cms.db.ToolUser;
import com.psddev.dari.util.AuthenticationException;
import com.psddev.dari.util.AuthenticationPolicy;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.Settings;
import com.psddev.util.SettingsOverride;

import com.google.common.collect.ImmutableMap;
import com.deveiredemo.model.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Univision specific cms tool authentication policy.
 */
public class JsgToolAuthenticationPolicy extends ToolAuthenticationPolicy implements SettingsOverride {

    // Have to hard code the setting name because the CMS lib doesn't expose
    // it as a variable
    private static final String CMS_TOOL_AUTHENTICATION_POLICY_SETTING = "cms/tool/authenticationPolicy";

    @Override
    public ToolUser authenticate(String username, String password) throws AuthenticationException {
        // Add custom logic. Just delegates to the CMS policy for now...
        return super.authenticate(username, password);
    }

    @Override
    public void initialize(String settingsKey, Map<String, Object> settings) {
        super.initialize(settingsKey, settings);
    }

    @Override
    public Map<String, Object> getOverrideSettings() {

        Map<String, Object> overrides = new HashMap<>();

        if (Settings.get(String.class, CMS_TOOL_AUTHENTICATION_POLICY_SETTING) == null) {

            CollectionUtils.putByPath(overrides, CMS_TOOL_AUTHENTICATION_POLICY_SETTING, Constants.APP_KEY);

            CollectionUtils.putByPath(overrides,
                    AuthenticationPolicy.SETTING_PREFIX + "/" + Constants.APP_KEY,
                    ImmutableMap.of("class", getClass().getName()));
        }

        return overrides;
    }
}
