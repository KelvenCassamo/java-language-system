package mz.cassamo.jls;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author cassamo
 */
class Builder {

    private final Map<String, Map<String, String>> translations = new HashMap<>();
private String filePath = null;
    public Builder() {
    }

   
    public void loadFromFile(String filePath) {
        this.filePath = filePath;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            LanguageHandler handler = new LanguageHandler();

            saxParser.parse(new File(filePath), handler);
            mergeTranslations(handler.getLanguages());
        } catch (Exception e) {
          
        }
    }

   
    public void loadFromResources(String resourcePath, Class<?> resourceClass) {
        try (InputStream inputStream = resourceClass.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            LanguageHandler handler = new LanguageHandler();

            saxParser.parse(inputStream, handler);
            mergeTranslations(handler.getLanguages());
        } catch (Exception e) {
           
        }
    }

   
    public void putLanguage(String language) {
        translations.putIfAbsent(language, new HashMap<>());
        normalizeTranslations();
    }

   
    public void removeLanguage(String language) {
        translations.remove(language);
    }

   
    public void putTranslation(String language, String key, String value) {
        translations.putIfAbsent(language, new HashMap<>());
        translations.get(language).put(key, value);
        normalizeTranslations();
    }

   
    public void removeTranslation(String language, String key) {
        if (translations.containsKey(language)) {
            translations.get(language).remove(key);
        }
        normalizeTranslations();
    }

   
    public void saveToFile(String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(toXmlString());
        } catch (IOException e) {
            if (LanguageSystem.isDebugMode()) {
               
            }
        }
    }
  
    public void save() {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(toXmlString());
        } catch (IOException e) {
            if (LanguageSystem.isDebugMode()) {
                e.printStackTrace();
            }
        }
    }

    public String toXmlString() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<!--%s %s-->\n".formatted(Info.LIB_NAME, Info.VERSION));
        xmlBuilder.append("<!--LANGUAGES: ").append(translations.size()).append("-->\n");
        
        xmlBuilder.append("<languages>\n");

        for (Map.Entry<String, Map<String, String>> languageEntry : translations.entrySet()) {
            String language = languageEntry.getKey();
            Map<String, String> languageTranslations = languageEntry.getValue();

            xmlBuilder.append("  <language value=\"").append(language).append("\">\n");
            for (Map.Entry<String, String> translationEntry : languageTranslations.entrySet()) {
                String key = translationEntry.getKey();
                String value = translationEntry.getValue();

                xmlBuilder.append("    <translated value=\"").append(key).append("\">\n");
                xmlBuilder.append("      <value>").append(escapeXml(value)).append("</value>\n");
                xmlBuilder.append("    </translated>\n");
            }
            xmlBuilder.append("  </language>\n");
        }

        xmlBuilder.append("</languages>");
        return xmlBuilder.toString();
    }

// Escapa caracteres especiais para XML
    private String escapeXml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

   
    private void mergeTranslations(Map<String, Map<String, String>> newTranslations) {
        for (Map.Entry<String, Map<String, String>> entry : newTranslations.entrySet()) {
            translations.putIfAbsent(entry.getKey(), new HashMap<>());
            translations.get(entry.getKey()).putAll(entry.getValue());
        }
        normalizeTranslations();
    }

   
    private void normalizeTranslations() {
        Set<String> allKeys = new HashSet<>();

       
        for (Map<String, String> languageMap : translations.values()) {
            allKeys.addAll(languageMap.keySet());
        }

        
        for (Map.Entry<String, Map<String, String>> entry : translations.entrySet()) {
            Map<String, String> languageMap = entry.getValue();
            for (String key : allKeys) {
                languageMap.putIfAbsent(key, ""); 
            }
        }
    }
}
