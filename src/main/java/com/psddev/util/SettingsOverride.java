package com.psddev.util;

import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.Settings;
import com.psddev.dari.util.TypeDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Permanently define or override {@link com.psddev.dari.util.Settings settings}.
 */
public interface SettingsOverride {

    /**
     * Returns a map of {@link com.psddev.dari.util.Settings settings} to be
     * permanently overridden in the application.
     *
     * @return the settings
     */
    Map<String, Object> getOverrideSettings();

    @WebListener("Applies permanent settings overrides to the application.")
    public static class ContextListener implements ServletContextListener {

        private static final Logger LOGGER = LoggerFactory.getLogger(ContextListener.class);

        @Override
        public void contextInitialized(ServletContextEvent sce) {

            for (Class<? extends SettingsOverride> settingsOverrideClass : ClassFinder.findClasses(SettingsOverride.class)) {
                try {
                    SettingsOverride settingsOverride = TypeDefinition.getInstance(settingsOverrideClass).newInstance();

                    Map<String, Object> overrides = settingsOverride.getOverrideSettings();

                    if (overrides != null) {
                        Settings.putPermanentOverrides(settingsOverrideClass.getName(), overrides);
                    }

                } catch (Exception e) {
                    LOGGER.error("Failed to initialize settings for class [" + settingsOverrideClass.getName() + "]. Cause: " + e.getMessage());
                }
            }
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {

            for (Class<? extends SettingsOverride> settingsOverrideClass : ClassFinder.findClasses(SettingsOverride.class)) {
                Settings.removePermanentOverrides(settingsOverrideClass.getName());
            }
        }
    }
}
