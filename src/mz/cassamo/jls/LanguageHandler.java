package mz.cassamo.jls;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX handler for parsing language XML files and constructing a map of
 * translations. Supports multiple languages and translations identified by
 * keys.
 *
 * <p>
 * Expected XML structure:
 * </p>
 * 
 * <pre>{@code
 * <languages>
 *     <language value="english">
 *         <translated value="greeting">
 *             <value>Hello, world!</value>
 *         </translated>
 *     </language>
 * <language value="portuguese">
 *         <translated value="greeting">
 *             <value>Olá, mundo!</value>
 *         </translated>
 *     </language>
 * </languages>
 * }</pre>
 *
 * <p>
 * Usage example:
 * </p>
 * 
 * <pre>{@code
 * LanguageHandler handler = new LanguageHandler();
 * // Parse XML using an appropriate SAX parser...
 * String translation = handler.getLanguageValue("english", "greeting", "Default");
 * }</pre>
 *
 * @authorá Cassamo
 * @version 1.0
 * @since 2024
 */
class LanguageHandler extends DefaultHandler {

    /**
     * The currently processed language name.
     */
    private String currentLanguage;
    /**
     * The currently processed translation key.
     */
    private String currentTranslationKey;
    private boolean isValueElement = false;
    private String tenseKey = null;



    /**
     * Stores translations for all languages as nested maps.
     */
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    /**
     * StringBuilder for accumulating character data within a <value> element.
     */
    private StringBuilder currentValueBuilder;

    private String currentFilePath;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
          if (qName.equalsIgnoreCase("import-language")) {

            // Processa a tag <import-language file="..."/>
            String importFilePath = attributes.getValue("file");
            if (importFilePath != null) {
                // Chama o método recursivamente para carregar e processar o arquivo de importação
                try {
                    initFromFile(importFilePath);
                } catch (IOException e) {
                    System.err.println("Error while importing file: " + importFilePath);
                }
            }
        } else if (qName.equalsIgnoreCase("language")) {
            currentLanguage = attributes.getValue("value");
            translations.putIfAbsent(currentLanguage, new HashMap<>());
            tenseKey = null;

        } else if (qName.equalsIgnoreCase("translated")) {
            currentTranslationKey = attributes.getValue("value");
            tenseKey = null;
        } else if (qName.equalsIgnoreCase("value")) {
            isValueElement = true;
            if (attributes.getValue("tense") != null) {
                tenseKey = attributes.getValue("tense");
            } else {
                tenseKey = null;
            }
            currentValueBuilder = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("value")) {
            isValueElement = false;

            if (currentValueBuilder != null && currentLanguage != null && currentTranslationKey != null) {
                String translationValue = currentValueBuilder.toString().trim();
                // System.out.println(currentTranslationKey);
                String tempCurrentTranslationKey = currentTranslationKey;
                // System.out.println(tenseKey);
                if (translations.get(currentLanguage).containsKey(tempCurrentTranslationKey)
                        && !tempCurrentTranslationKey.contains("~") && tenseKey != null) {
                    /**
                     * We add ~ if the current translation has multiple values and these values
                     * contain tense attribute.
                     */

                    if (tenseKey != null) {
                        tempCurrentTranslationKey = tempCurrentTranslationKey + "~" + tenseKey;
                        tenseKey = null;
                    }
                } else if (!translations.get(currentLanguage).containsKey(tempCurrentTranslationKey)
                        && !tempCurrentTranslationKey.contains("~") && tenseKey != null) {
                    tempCurrentTranslationKey = tempCurrentTranslationKey + "~" + tenseKey;
                    tenseKey = null;
                }

                //System.out.println(tempCurrentTranslationKey);

                translations.get(currentLanguage).put(tempCurrentTranslationKey, translationValue);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isValueElement && currentValueBuilder != null) {
            currentValueBuilder.append(ch, start, length);
        }
    }

    /**
     * Retrieves a translation value for a given language and key.
     *
     * @param language       the language to look up
     * @param translationKey the key for the desired translation
     * @param defaultValue   the value to return if the key or language is not
     *                       found
     * @return the translation value, or the default value if not found
     */
    public String getLanguageValue(String language, String translationKey, String default_value) {
        Map<String, String> languageMap = translations.get(language);
        if (languageMap != null) {
            return languageMap.getOrDefault(translationKey, default_value);
        } else {
            return default_value;
        }
    }

    public Map<String, String> getLanguageValues(String language) {
        Map<String, String> languageMap = translations.get(language);
        System.out.println(translations);
        return languageMap;
        /*
         * if (languageMap != null) {
         * return languageMap.getOrDefault(translationKey, default_value);
         * } else {
         * return default_value;
         * }
         */
    }

    /**
     * Returns all loaded translations grouped by language.
     *
     * @return a map of translations by language
     */
    public Map<String, Map<String, String>> getLanguages() {
        return translations;
    }

    /**
     * Checks if a specific language exists in the loaded translations.
     *
     * @param language the language to check
     * @return {@code true} if the language exists; {@code false} otherwise
     */
    public boolean existsLanguage(String language) {
        return translations.containsKey(language);
    }


    /**
     * Inicia o parsing de um arquivo XML, incluindo seus arquivos importados.
     *
     * @param xmlFilePath Caminho para o arquivo XML a ser processado
     * @throws IOException Se houver erro ao processar o arquivo
     */
    public void initFromFile(String xmlFilePath) throws IOException {
        currentFilePath = xmlFilePath; // Salva o caminho do arquivo atual

        File file = new File(xmlFilePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + xmlFilePath);
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            // Usando SAXParser para processar o XML
            javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputStream, this);
        } catch (Exception e) {
            throw new IOException("Error while parsing file: " + xmlFilePath, e);
        }
    }
}

