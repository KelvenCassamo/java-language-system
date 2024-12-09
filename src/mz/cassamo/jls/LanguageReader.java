/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mz.cassamo.jls;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Cassamo
 */
class LanguageReader {

    /*
      try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            LanguageHandler handler = new LanguageHandler();
            saxParser.parse("languages.xml", handler);

    
            String value = handler.getLanguageValue("portuguese", "bem_vindo");
            System.out.println("Translation in portuguese for 'bem_vindo': " + value);

            value = handler.getLanguageValue("english", "sair");
            System.out.println("Translation in english for 'sair': " + value);

    
            boolean exists = handler.existsLanguage("portuguese");
            System.out.println("Does 'portuguese' exist? " + exists);

            exists = handler.existsLanguage("french");
            System.out.println("Does 'french' exist? " + exists);

        } catch (Exception e) {
            e.printStackTrace();
        }
     */
    private static SAXParserFactory factory = SAXParserFactory.newInstance();
    private static LanguageHandler handler = new LanguageHandler();
    private static String language = "";
    private static SAXParser saxParser = null;
    private static Map<String, Map<String, String>> languages;

    public static void init(String _language) {
        language = _language;
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse("languages.xml", handler);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            if (LanguageSystem.isDebugMode()) {
                e.printStackTrace();
            }
        }
    }

    public static void initFromFile(String _language, String xml_file_path) {
        language = _language;
        File file = new File(xml_file_path);
        if (!file.exists()) {
            return;
        }
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse(file.getAbsolutePath(), handler);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            if (LanguageSystem.isDebugMode()) {
                e.printStackTrace();
            }
        }
    }

    public static void initFromRes(String _language, Class<?> _class, String resourcePath) {
        language = _language;
        if (!resourcePath.startsWith("/")) {
            resourcePath = "/".concat(resourcePath);
        }
        try (InputStream inputStream = _class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                if (LanguageSystem.isDebugMode()) {
                    System.err.println("Resource not found: " + resourcePath);
                }

                return;
            }

            saxParser = factory.newSAXParser();
            saxParser.parse(inputStream, handler);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            if (LanguageSystem.isDebugMode()) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Map<String, String>> getLanguages() {
        if (saxParser != null) {
            return handler.getLanguages();
        } else {
            return new HashMap<>();
        }

    }

    public static void setLanguage(String lang) {
        language = lang;
    }

    public static String getCurrentLanguage() {
        return language;
    }

    public static String getValue(String key, String default_value) {
        key = key.toLowerCase();
        if (saxParser != null) {
            return handler.getLanguageValue(language, key, default_value);
        } else {
            return "";
        }

    }

    public static boolean existsLanguage(String language) {
        return getLanguages().containsKey(language);
    }

}
