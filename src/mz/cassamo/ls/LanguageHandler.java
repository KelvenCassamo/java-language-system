package mz.cassamo.ls;

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
 * Expected XML structure:</p>
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
 * Usage example:</p>
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

    /**
     * Stores translations for all languages as nested maps.
     */
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    /**
     * StringBuilder for accumulating character data within a <value> element.
     */
    private StringBuilder currentValueBuilder;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("language")) {
            currentLanguage = attributes.getValue("value");
            translations.putIfAbsent(currentLanguage, new HashMap<>());
        } else if (qName.equalsIgnoreCase("translated")) {
            currentTranslationKey = attributes.getValue("value");
        } else if (qName.equalsIgnoreCase("value")) {
            isValueElement = true;
            currentValueBuilder = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("value")) {
            isValueElement = false;

            if (currentValueBuilder != null && currentLanguage != null && currentTranslationKey != null) {
                String translationValue = currentValueBuilder.toString().trim();
                translations.get(currentLanguage).put(currentTranslationKey, translationValue);
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
     * @param language the language to look up
     * @param translationKey the key for the desired translation
     * @param defaultValue the value to return if the key or language is not
     * found
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
}
