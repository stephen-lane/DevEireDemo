package com.psddev.util;

import com.psddev.dari.util.IoUtils;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.json.JsonXMLOutputFactory;
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

/**
 * Collection of utility methods for converting XML to JSON, and JSON to XML.
 */
public final class StaxonUtils {

    private StaxonUtils() {
    }

    /**
     * XML to JSON to converter according to the
     * <a href="https://github.com/beckchr/staxon/wiki/Converting-XML-to-JSON">
     * Staxon API</a>.
     */
    public static String xmlToJson(String xml, boolean formatted) throws XMLStreamException {

        InputStream input = new ByteArrayInputStream(xml.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        JsonXMLConfig config = new JsonXMLConfigBuilder()
                .autoArray(true)
                .autoPrimitive(true)
                .prettyPrint(formatted)
                .build();

        try {
            /* Create reader (XML). */
            XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(input);

            /* Create writer (JSON). */
            XMLEventWriter writer = new JsonXMLOutputFactory(config).createXMLEventWriter(output);

            /* Copy events from reader to writer. */
            writer.add(reader);

            /* Close reader/writer. */
            reader.close();
            writer.close();

            try {
                return output.toString("UTF-8");

            } catch (UnsupportedEncodingException e) {
                throw new XMLStreamException(e.getMessage());
            }

        } finally {
            IoUtils.closeQuietly(output);
            IoUtils.closeQuietly(input);
        }
    }

    /**
     * JSON to XML converter according to the
     * <a href="https://github.com/beckchr/staxon/wiki/Converting-JSON-to-XML">
     * Staxon API</a>.
     */
    public static String jsonToXml(String json, boolean formatted) throws XMLStreamException {

        InputStream input = new ByteArrayInputStream(json.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        JsonXMLConfig config = new JsonXMLConfigBuilder().multiplePI(false).build();
        try {
            /* Create reader (JSON). */
            XMLEventReader reader = new JsonXMLInputFactory(config).createXMLEventReader(input);

            /* Create writer (XML). */
            XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(output);
            if (formatted) {
                writer = new PrettyXMLEventWriter(writer); // format output
            }

            /* Copy events from reader to writer. */
            writer.add(reader);

            /* Close reader/writer. */
            reader.close();
            writer.close();

            try {
                return output.toString("UTF-8");

            } catch (UnsupportedEncodingException e) {
                throw new XMLStreamException(e.getMessage());
            }

        } finally {
            IoUtils.closeQuietly(output);
            IoUtils.closeQuietly(input);
        }
    }

    /**
     * XML to JSON to converter according to the
     * <a href="https://github.com/beckchr/staxon/wiki/Converting-XML-to-JSON">
     * Staxon API</a>. Suppresses any exceptions.
     */
    public static String xmlToJsonOrNull(String xml, boolean formatted) {
        try {
            return xmlToJson(xml, formatted);
        } catch (XMLStreamException | FactoryConfigurationError e) {
            return null;
        }
    }

    /**
     * JSON to XML converter according to the
     * <a href="https://github.com/beckchr/staxon/wiki/Converting-JSON-to-XML">
     * Staxon API</a>. Suppresses any exceptions.
     */
    public static String jsonToXmlOrNull(String json, boolean formatted) {
        try {
            return jsonToXml(json, formatted);
        } catch (XMLStreamException | FactoryConfigurationError e) {
            return null;
        }
    }
}
