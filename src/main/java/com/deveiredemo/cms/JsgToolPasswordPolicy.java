package com.deveiredemo.cms;

import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.PasswordException;
import com.psddev.dari.util.Settings;
import com.psddev.dari.util.UserPasswordPolicy;
import com.psddev.util.SettingsOverride;

import com.google.common.collect.ImmutableMap;
import com.deveiredemo.model.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Univision specific cms tool password policy.
 */
public class JsgToolPasswordPolicy implements UserPasswordPolicy,
                                              SettingsOverride {

    // Have to hard code the setting name because the CMS lib doesn't expose
    // it as a variable
    private static final String CMS_TOOL_USER_PASSWORD_POLICY_SETTING = "cms/tool/userPasswordPolicy";

    public static final int MINIMUM_PASSWORD_LENGTH = 3;

    @Override
    public void validate(Object user, String password) throws PasswordException {

        StringBuilder error = new StringBuilder();

        char[] pwArr = password.toCharArray();

        if (pwArr.length < MINIMUM_PASSWORD_LENGTH) {
            error.append("Password must be a minimum of " + MINIMUM_PASSWORD_LENGTH + " characters in length. ");
        }

        if (error.length() > 0) {
            error.setLength(error.length() - 1);
            throw new PasswordException(error.toString());
        }
    }

    @Override
    public void initialize(String settingsKey, Map<String, Object> settings) {
        // nothing to do here
    }

    @Override
    public Map<String, Object> getOverrideSettings() {

        Map<String, Object> overrides = new HashMap<>();

        if (Settings.get(String.class, CMS_TOOL_USER_PASSWORD_POLICY_SETTING) == null) {

            CollectionUtils.putByPath(overrides, CMS_TOOL_USER_PASSWORD_POLICY_SETTING, Constants.APP_KEY);

            CollectionUtils.putByPath(overrides,
                    UserPasswordPolicy.SETTING_PREFIX + "/" + Constants.APP_KEY,
                    ImmutableMap.of("class", getClass().getName()));
        }

        return overrides;
    }
}

