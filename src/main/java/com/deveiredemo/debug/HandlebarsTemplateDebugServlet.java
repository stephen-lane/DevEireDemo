package com.deveiredemo.debug;

import com.psddev.dari.util.DebugFilter;
import com.psddev.dari.util.IoUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Settings;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@DebugFilter.Path("bsp-handlebars")
@SuppressWarnings("serial")
public class HandlebarsTemplateDebugServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlebarsTemplateDebugServlet.class);

    private static final String DEBUG_SCRIPT_NAME = "Handlebars Template Debug";

    public static final String JSON_DIRECTORY_SETTING = "hbsDebug/jsonDirectory";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        new DebugFilter.PageWriter(getServletContext(), request, response) {
            {
                startPage(DEBUG_SCRIPT_NAME);
                {
                    writeStart("style", "type", "text/css");
                    writeEnd();

                    writeStart("script");
                    {
                        write("$(document).ready(function(){");
                        write("});");
                    }
                    writeEnd();

                    String jsonDirectory = Settings.get(String.class, JSON_DIRECTORY_SETTING);
                    if (jsonDirectory != null) {

                        Map<String, JsonDataFile> jsonDataFiles = new TreeMap<>();
                        Map<String, HbsTemplate> templates = new TreeMap<>();

                        for (File file : FileUtils.listFiles(new File(jsonDirectory), new String[] { "json" }, true)) {
                            updateTemplateUsages(file, jsonDataFiles, templates);
                        }

                        String id = request.getParameter("id");

                        boolean done = false;

                        if (id != null) {

                            HbsTemplate selectedTemplate = templates.get(id);
                            if (selectedTemplate != null) {
                                renderTemplateUsages(selectedTemplate, templates, jsonDataFiles, jsonDirectory);
                                done = true;
                            }

                            JsonDataFile selectedJsonDataFile = jsonDataFiles.get(jsonDirectory + id);
                            if (!done && selectedJsonDataFile != null) {
                                renderJsonDataFile(selectedJsonDataFile, templates, jsonDataFiles, jsonDirectory);
                                done = true;
                            }
                        }

                        if (!done) {
                            renderOverview(templates, jsonDataFiles, jsonDirectory);
                        }

                    } else {
                        writeStart("p");
                        {
                            writeHtml("In order to use this page you must add the ");
                            writeStart("code");
                            {
                                writeHtml(JSON_DIRECTORY_SETTING);
                            }
                            writeEnd();
                            writeHtml(" setting to your context.xml with the full path to the styleguide directory of your project.");
                        }
                        writeEnd();
                        writeStart("p");
                        {
                            writeHtml("Ex. ");
                            writeStart("code");
                            {
                                writeHtml("<Environment name=\"hbsDebug/jsonDirectory\" type=\"java.lang.String\" value=\"/path/to/project/styleguide\" />");
                            }
                            writeEnd();

                        }
                        writeEnd();
                    }
                }
                endPage();
            }

            private void renderJsonDataFile(JsonDataFile jsonDataFile, Map<String, HbsTemplate> templates, Map<String, JsonDataFile> jsonDataFiles, String jsonDirectory) throws IOException, ServletException {

                String templateName = jsonDataFile.getTemplate();
                HbsTemplate template = templates.get(templateName);

                writeStart("a", "href", page.url("", "id", null));
                {
                    writeRaw("&larr; Back to Overview");
                }
                writeEnd();

                writeStart("h2");
                {
                    writeHtml(jsonDataFile.getFileName().replace(jsonDirectory, ""));
                }
                writeEnd();

                writeStart("h3");
                {
                    writeHtml("Template: ");
                    writeStart("a", "href", page.url("", "id", template.getName()));
                    {
                        writeHtml(template.getName());
                    }
                    writeEnd();
                }
                writeEnd();

                writeStart("pre");
                {
                    writeHtml(ObjectUtils.toJson(jsonDataFile.getJsonData(), true));
                }
                writeEnd();
            }

            private void renderTemplateUsages(HbsTemplate template, Map<String, HbsTemplate> templates, Map<String, JsonDataFile> jsonDataFiles, String jsonDirectory) throws IOException, ServletException {

                writeStart("a", "href", page.url("", "id", null));
                {
                    writeRaw("&larr; Back to Overview");
                }
                writeEnd();

                List<HbsTemplateUsage> usages = template.getUsages();

                writeStart("h2");
                {
                    writeHtml(template.getName());
                }
                writeEnd();

                Map<String, HbsTemplateField> fields = template.getFields(templates, jsonDataFiles);

                writeStart("table", "class", "table table-striped table-condensed table-bordered", "style", "width: auto;");
                {
                    writeStart("thead");
                    {
                        writeStart("tr");
                        {
                            writeStart("th");
                            {
                                writeHtml("#");
                            }
                            writeEnd();
                            writeStart("th");
                            {
                                writeHtml("Field Name");
                            }
                            writeEnd();
                            writeStart("th");
                            {
                                writeHtml("Type");
                            }
                            writeEnd();
                            writeStart("th");
                            {
                                writeHtml("# Usages (Total " + usages.size() + ")");
                            }
                            writeEnd();
                        }
                        writeEnd();
                    }
                    writeEnd();
                    writeStart("tbody");
                    {
                        int count = 1;
                        for (HbsTemplateField field : fields.values()) {

                            writeStart("tr");
                            {
                                writeStart("td");
                                {
                                    writeHtml(count++);
                                }
                                writeEnd();
                                writeStart("td");
                                {
                                    writeHtml(field.getName());
                                }
                                writeEnd();
                                writeStart("td");
                                {
                                    String fieldType = field.getType();

                                    if (field.isTemplate()) {

                                        writeStart("a", "href", page.url("", "id", fieldType));
                                        {
                                            writeHtml(fieldType);
                                        }
                                        writeEnd();

                                    } else {
                                        writeHtml(fieldType);
                                    }
                                }
                                writeEnd();
                                writeStart("td");
                                {
                                    writeHtml(field.getUsages());
                                }
                                writeEnd();
                            }
                            writeEnd();
                        }
                    }
                    writeEnd();
                }
                writeEnd();

                writeStart("ol");
                {
                    for (HbsTemplateUsage usage : usages) {

                        String jsonFile = usage.getParentJsonFile();
                        String relativePath = jsonFile.replace(jsonDirectory, "");

                        writeStart("li");
                        {
                            writeStart("a", "href", page.url("") + "#json-" + relativePath);
                            {
                                writeHtml(relativePath);
                            }
                            writeEnd();
                        }
                        writeEnd();
                    }
                }
                writeEnd();

                writeTag("hr");

                writeStart("ol");
                {
                    for (HbsTemplateUsage usage : usages) {

                        String jsonFile = usage.getParentJsonFile();
                        String relativePath = jsonFile.replace(jsonDirectory, "");
                        Map<String, Object> jsonData = usage.getJsonData();

                        writeStart("li", "id", "json-" + relativePath);
                        {
                            writeStart("span");
                            {
                                writeStart("a", "href", page.url("", "id", relativePath));
                                {
                                    writeHtml(relativePath);
                                }
                            }
                            writeEnd();
                            writeStart("pre");
                            {
                                writeHtml(ObjectUtils.toJson(jsonData, true));
                            }
                            writeEnd();
                        }
                        writeEnd();
                    }
                }
                writeEnd();
            }

            private void renderOverview(Map<String, HbsTemplate> templates, Map<String, JsonDataFile> jsonDataFiles, String jsonDirectory) throws IOException, ServletException {

                writeStart("h2").writeHtml(DEBUG_SCRIPT_NAME).writeEnd();

                writeTag("br");

                writeStart("a", "href", page.url("") + "#jsonj");
                {
                    writeHtml("JSON Data Files (By JSON)");
                }
                writeEnd();

                writeHtml(" | ");

                writeStart("a", "href", page.url("") + "#jsont");
                {
                    writeHtml("JSON Data Files (By Template)");
                }
                writeEnd();

                writeTag("br");
                writeTag("br");

                writeStart("h3");
                {
                    writeHtml("Templates");
                }
                writeEnd();

                writeStart("table", "class", "table table-striped table-condensed table-bordered", "style", "width: auto;");
                {
                    writeStart("thead");
                    {
                        writeStart("tr");
                        {
                            writeStart("th");
                            {
                                writeHtml("#");
                            }
                            writeEnd();
                            writeStart("th");
                            {
                                writeHtml("Template Name");
                            }
                            writeEnd();
                            writeStart("th");
                            {
                                writeHtml("# Usages");
                            }
                            writeEnd();
                        }
                        writeEnd();
                    }
                    writeEnd();
                    writeStart("tbody");
                    {
                        int count = 1;
                        for (HbsTemplate template : templates.values()) {
                            writeStart("tr");
                            {
                                writeStart("td");
                                {
                                    writeHtml(count++);
                                }
                                writeEnd();
                                writeStart("td");
                                {
                                    writeStart("a", "href", page.url("", "id", template.getName()));
                                    {
                                        writeHtml(template.getName());
                                    }
                                    writeEnd();
                                }
                                writeEnd();
                                writeStart("td");
                                {
                                    writeHtml(template.getUsages().size());
                                }
                                writeEnd();
                            }
                            writeEnd();
                        }
                    }
                    writeEnd();
                }
                writeEnd();

                Collection<JsonDataFile> jsonDataFilesByJson = jsonDataFiles.values();

                List<JsonDataFile> jsonDataFilesByTemplate = new ArrayList<>(jsonDataFilesByJson);
                Collections.sort(jsonDataFilesByTemplate, (json1, json2) -> ObjectUtils.compare(json1.getTemplate(), json2.getTemplate(), true));

                for (int i = 0; i < 2; i++) {

                    Collection<JsonDataFile> dataFiles;
                    String heading;
                    String headingId;
                    if (i == 0) {
                        dataFiles = jsonDataFilesByJson;
                        heading = "JSON Data Files (By JSON)";
                        headingId = "jsonj";
                    } else {
                        dataFiles = jsonDataFilesByTemplate;
                        heading = "JSON Data Files (By Template)";
                        headingId = "jsont";
                    }

                    writeStart("h3", "id", headingId);
                    {
                        writeHtml(heading);
                    }
                    writeEnd();

                    writeStart("table", "class", "table table-striped table-condensed table-bordered", "style", "width: auto;");
                    {
                        writeStart("thead");
                        {
                            writeStart("tr");
                            {
                                writeStart("th");
                                {
                                    writeHtml("#");
                                }
                                writeEnd();
                                writeStart("th");
                                {
                                    writeHtml("JSON Data File Name");
                                }
                                writeEnd();

                                writeStart("th");
                                {
                                    writeHtml("Template Name");
                                }
                                writeEnd();
                            }
                            writeEnd();
                        }
                        writeEnd();
                        writeStart("tbody");
                        {
                            int count = 1;
                            for (JsonDataFile jsonDataFile : dataFiles) {

                                String relativePath = jsonDataFile.getFileName().replace(jsonDirectory, "");

                                writeStart("tr");
                                {
                                    writeStart("td");
                                    {
                                        writeHtml(count++);
                                    }
                                    writeEnd();
                                    writeStart("td");
                                    {
                                        writeStart("a", "href", page.url("", "id", relativePath));
                                        {
                                            writeHtml(relativePath);
                                        }
                                        writeEnd();
                                    }
                                    writeEnd();
                                    writeStart("td");
                                    {
                                        writeHtml(jsonDataFile.getTemplate());
                                    }
                                    writeEnd();
                                }
                                writeEnd();
                            }
                        }
                        writeEnd();
                    }
                    writeEnd();
                }
            }
        };
    }

    private static void updateTemplateUsages(File file, Map<String, JsonDataFile> jsonDataFiles, Map<String, HbsTemplate> templates) {

        String jsonString;
        try {
            jsonString = IoUtils.toString(file, StandardCharsets.UTF_8);

            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> jsonMap = (Map<String, Object>) ObjectUtils.fromJson(jsonString);

                if (jsonMap != null) {
                    updateTemplateUsages(file, jsonMap, jsonDataFiles, templates, false);
                }

            } catch (Exception e) {
                LOGGER.warn("Could not parse file: " + file.getAbsolutePath());
            }

        } catch (IOException e) {
            LOGGER.warn("Error reading file: " + file.getAbsolutePath());
        }
    }

    private static void updateTemplateUsages(File jsonFile, Object jsonObject, Map<String, JsonDataFile> jsonDataFiles, Map<String, HbsTemplate> templates, boolean isEmbedded) {

        if (jsonObject instanceof Map) {

            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = (Map<String, Object>) jsonObject;

            Object templateName = jsonMap.get("_template");
            if (templateName instanceof String) {

                HbsTemplate template = templates.get(templateName);
                if (template == null) {
                    template = new HbsTemplate((String) templateName);
                    templates.put((String) templateName, template);
                }

                if (!isEmbedded) {
                    jsonDataFiles.put(jsonFile.getAbsolutePath(), new JsonDataFile(jsonFile.getAbsolutePath(), (String) templateName, deepCopyMap(jsonMap)));
                }

                template.getUsages().add(new HbsTemplateUsage(jsonFile.getAbsolutePath(), isEmbedded, deepCopyMap(jsonMap)));
            }

            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {

                Object value = entry.getValue();

                if (value instanceof Map || value instanceof List) {
                    updateTemplateUsages(jsonFile, value, jsonDataFiles, templates, true);
                }
            }

        } else if (jsonObject instanceof List) {
            for (Object jsonItem : (List<?>) jsonObject) {
                updateTemplateUsages(jsonFile, jsonItem, jsonDataFiles, templates, true);
            }
        }
    }

    private static Map<String, Object> deepCopyMap(Map<String, Object> jsonMap) {
        // TODO: Still need to implement
        return jsonMap;
    }

    private static final class JsonDataFile {

        private String fileName;
        private String template;
        private Map<String, Object> jsonData;

        private JsonDataFile(String fileName, String template, Map<String, Object> jsonData) {
            this.fileName = fileName;
            this.template = template;
            this.jsonData = jsonData;
        }

        public String getFileName() {
            return fileName;
        }

        public String getTemplate() {
            return template;
        }

        public Map<String, Object> getJsonData() {
            return jsonData;
        }
    }

    private static final class HbsTemplate {

        private String name;
        private List<HbsTemplateUsage> usages;

        private HbsTemplate(String name) {
            this.name = name;
        }

        private HbsTemplate(String name, List<HbsTemplateUsage> usages) {
            this.name = name;
            this.usages = usages;
        }

        public String getName() {
            return name;
        }

        public List<HbsTemplateUsage> getUsages() {
            if (usages == null) {
                usages = new ArrayList<>();
            }
            return usages;
        }

        public Map<String, HbsTemplateField> getFields(Map<String, HbsTemplate> templates, Map<String, JsonDataFile> jsonDataFiles) {

            Map<String, HbsTemplateField> fields = new LinkedHashMap<>();

            for (HbsTemplateUsage usage : getUsages()) {
                Map<String, Object> jsonData = usage.getJsonData();

                for (Map.Entry<String, Object> entry : jsonData.entrySet()) {

                    String fieldName = entry.getKey();
                    Object fieldValue = entry.getValue();

                    if (!"_template".equals(fieldName)) {

                        HbsTemplateField field = fields.get(fieldName);
                        if (field == null) {

                            field = new HbsTemplateField(fieldName);
                            fields.put(fieldName, field);
                        }

                        field.addValue(fieldValue);
                    }
                }
            }

            for (HbsTemplateField field : fields.values()) {
                field.aggregateData(templates, jsonDataFiles);
            }

            return fields;
        }
    }

    private static final class HbsTemplateUsage {

        private String parentJsonFile;
        private boolean isEmbedded;
        private Map<String, Object> jsonData;

        public HbsTemplateUsage(String parentJsonFile, boolean isEmbedded, Map<String, Object> jsonData) {
            this.parentJsonFile = parentJsonFile;
            this.isEmbedded = isEmbedded;
            this.jsonData = jsonData;
        }

        public String getParentJsonFile() {
            return parentJsonFile;
        }

        public boolean isEmbedded() {
            return isEmbedded;
        }

        public Map<String, Object> getJsonData() {
            return jsonData;
        }
    }

    private static final class HbsTemplateField {

        private String name;

        private String type;

        private boolean isTemplate;

        private int usages;

        List<Object> values = new ArrayList<>();

        public HbsTemplateField(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getUsages() {
            return usages;
        }

        public boolean isTemplate() {
            return isTemplate;
        }

        public void addValue(Object value) {
            values.add(value);
        }

        public void aggregateData(Map<String, HbsTemplate> templates, Map<String, JsonDataFile> jsonDataFiles) {

            usages = values.size();

            Set<String> valueTypes = new HashSet<>();
            for (Object value : values) {

                String valueType;

                Class<?> valueClass = value.getClass();

                if (value instanceof String) {
                    valueType = "String";

                } else if (value instanceof List) {
                    valueType = "List";

                } else if (value instanceof Map) {
                    Object template = ((Map<?, ?>) value).get("_template");
                    Object dataUrl = ((Map<?, ?>) value).get("_dataUrl");

                    if (template instanceof String) {
                        valueType = (String) template;
                        isTemplate = true;

                    } else if (dataUrl instanceof String) {
                        JsonDataFile jsonDataFile = jsonDataFiles.get(dataUrl);

                        if (jsonDataFile != null) {
                            valueType = jsonDataFile.getTemplate();
                            isTemplate = true;

                        } else {
                            valueType = "?";
                        }

                    } else {
                        valueType = "Map";
                    }
                } else if (value instanceof Number) {
                    valueType = "Number";

                } else {
                    valueType = valueClass.getSimpleName();
                }

                valueTypes.add(valueType);
            }

            if (valueTypes.size() == 1) {
                type = valueTypes.iterator().next();

            } else {
                type = "?";
                isTemplate = false;
            }

            values.clear();
        }
    }
}
