package com.deveiredemo.view;

import com.psddev.handlebars.HandlebarsTemplate;

import com.google.common.collect.ImmutableMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@HandlebarsTemplate("common/head")
public interface HeadView {

    String getCanonicalLink();

    String getTitle();

    String getDescription();

    String getKeywords();

    List<Meta> getMeta();

    List<Style> getStyle();

    List<Script> getScript();

    List<Icon> getIcon();

    interface Style {

        Map<String, String> getAttributes();

        String getContent();
    }

    interface Script {

        Map<String, String> getAttributes();

        String getContent();
    }

    interface Meta {

        Map<String, String> getAttributes();
    }

    interface Icon {

        Map<String, String> getAttributes();
    }

    static Meta createPropertyMeta(String property, String content) {
        return createCustomMeta("property", property, "content", content);
    }

    static Meta createNamedMeta(String name, String content) {
        return createCustomMeta("name", name, "content", content);
    }

    static Meta createCustomMeta(String... keyValuePairs) {

        Map<String, String> attributes = new LinkedHashMap<>();

        if (keyValuePairs != null) {
            for (int i = 1; i < keyValuePairs.length; i += 2) {

                String key = keyValuePairs[i - 1];
                String value = keyValuePairs[i];
                if (key != null) {
                    attributes.put(key, value);
                }
            }
        }

        return !attributes.isEmpty() ? () -> attributes : null;
    }

    static Style createSourceStyle(String source) {
        return new Style() {
            @Override
            public Map<String, String> getAttributes() {
                return ImmutableMap.of(
                        "rel", "stylesheet",
                        "type", "text/css",
                        "href", source);
            }

            @Override
            public String getContent() {
                return null;
            }
        };
    }

    static Style createInlineStyle(String content) {
        return new Style() {
            @Override
            public Map<String, String> getAttributes() {
                return null;
            }

            @Override
            public String getContent() {
                return content;
            }
        };
    }

    static Script createSourceScript(String source) {
        return new Script() {
            @Override
            public Map<String, String> getAttributes() {
                return ImmutableMap.of(
                        "type", "text/javascript",
                        "src", source);
            }

            @Override
            public String getContent() {
                return null;
            }
        };
    }

    static Script createInlineScript(String content) {
        return new Script() {
            @Override
            public Map<String, String> getAttributes() {
                return ImmutableMap.of("type", "text/javascript");
            }

            @Override
            public String getContent() {
                return content;
            }
        };
    }

    static Icon createFavicon(String path) {
        return () -> ImmutableMap.of(
                "rel", "shortcut icon",
                "href", path);
    }

    static Icon createFaviconResize(String path, Integer width, Integer height) {
        return () -> ImmutableMap.of(
                "rel", "icon",
                "type", "image/png",
                "href", path,
                "sizes", width + "x" + height);
    }

    static Icon createAppleTouchIcon(String path, Integer width, Integer height) {
        return () -> ImmutableMap.of(
                "rel", "apple-touch-icon",
                "href", path,
                "sizes", width + "x" + height);
    }
}
