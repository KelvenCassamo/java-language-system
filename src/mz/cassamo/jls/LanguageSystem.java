package mz.cassamo.jls;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * A system for managing and applying language translations across user
 * interface components.
 * <p>
 * It supports features such as initialization from different sources (file,
 * resources), dynamic language switching, and automatic translation of
 * components based on language keys.</p>
 *
 * Example usage:
 * <pre>{@code
 * LanguageSystem.initialize("en");
 * LanguageSystem.setCurrentLanguage("fr");
 * }</pre>
 *
 * The system also supports formatting language strings with placeholders,
 * allowing dynamic content insertion.
 *
 * @author Cassamo
 */
public class LanguageSystem {

    private static final ArrayList<HashMap<String, Object>> appliedComponents = new ArrayList<>();
    private static final ArrayList<HashMap<String, Object>> appliedWidgets = new ArrayList<>();
    private static boolean debug = false;
    private static LanguageSystemInterface li;

    /**
     * Interface for receiving notifications when the language is changed.
     */
    public static interface LanguageSystemInterface {

        /**
         * Called when the language is changed.
         *
         * @param current_language the new language that is set.
         */
        void onChange(String current_language);
    }

    /**
     * Registers a listener to receive language change notifications.
     *
     * @param _li the listener to be notified of language changes.
     */
    public static void addLanguageSystemInterface(LanguageSystemInterface _li) {
        li = _li;
    }

    /**
     * Initializes the language system from a specified file.
     *
     * @param default_language the default language to be used.
     * @param xml_file_path the path to the XML file containing language
     * translations.
     */
    public static void initializeFromFile(String default_language, String xml_file_path) {
        LanguageReader.initFromFile(default_language, xml_file_path);
    }

    /**
     * Initializes the language system from a specified resource.
     *
     * @param default_language the default language to be used.
     * @param _class the class used to load the resource.
     * @param xml_file_path the path to the XML resource containing language
     * translations.
     */
    public static void initializeFromResources(String default_language, Class _class, String xml_file_path) {
        LanguageReader.initFromRes(default_language, _class, xml_file_path);
    }

    /**
     * Gets the current active language.
     *
     * @return the current language.
     */
    public static String getCurrentLanguage() {
        return LanguageReader.getCurrentLanguage();
    }

    /**
     * Gets a list of available languages.
     *
     * @return a list of language codes.
     */
    public static ArrayList<String> getLanguages() {
        ArrayList<String> langs = new ArrayList<>();
        for (String string : LanguageReader.getLanguages().keySet()) {
            langs.add(string);
        }
        return langs;
    }

    /**
     * Gets a list of available translations for specific language.
     *
     * @param language the language to be used.
     * @return a list of translations.
     */
    public static ArrayList<String> getTranslationKeys(String language) {
        ArrayList<String> trans = new ArrayList<>();
        for (String string : LanguageReader.getLanguages().get(language).keySet()) {
            trans.add(string);
        }
        return trans;
    }

    /**
     * Sets the current language of the system and triggers automatic
     * translation of components.
     *
     * @param language the language code to be set.
     */
    public static void setCurrentLanguage(String language) {
        LanguageReader.setLanguage(language);
        autoInsertLanguage();
        if (li != null) {
            li.onChange(language);
        }
    }

    /**
     * Sets the current language of the system with a fallback to the default
     * language if the specified language does not exist.
     *
     * @param language the desired language code.
     * @param default_language the default language code to use as a fallback.
     */
    public static void setCurrentLanguage(String language, String default_language) {
        String lang = language;
        if (!existsLanguage(language)) {
            lang = default_language;
        }
        LanguageReader.setLanguage(lang);
        autoInsertLanguage();
        if (li != null) {
            li.onChange(language);
        }
    }

    /**
     * Automatically translates a single component based on the provided
     * language key.
     *
     * @param component the component to be translated.
     * @param language_key the language key for translation.
     */
    public static void autoTranslateComponent(Component component, String language_key) {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("component", component);
        temp.put("language_value", language_key);
        appliedComponents.add(temp);
        autoInsertLanguage();
    }

    /**
     * Automatically translates a single component based on the provided
     * language key.
     *
     * @param widget the android widget to be translated.
     * @param language_key the language key for translation.
     */
    /*  public static void autoTranslateWidget(Object widget, String language_key) {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("widget", widget);
        temp.put("language_value", language_key);
        appliedWidgets.add(temp);
        autoInsertLanguage();
    }*/
    /**
     * Automatically translates multiple components based on the provided
     * language key.
     *
     * @param language_key the language key for translation.
     * @param components the components to be translated.
     */
    public static void autoTranslateComponents(String language_key, Component... components) {
        if (components.length > 0) {
            for (Component component : components) {
                HashMap<String, Object> temp = new HashMap<>();
                temp.put("component", component);
                temp.put("language_value", language_key);
                appliedComponents.add(temp);
                autoInsertLanguage();
            }
        }
    }

    /**
     * Checks if a component can be translated by checking if it has a 'setText'
     * method.
     *
     * @param component the component to check.
     * @return true if the component can be translated, false otherwise.
     */
    public static boolean canTranslateComponent(Component component) {
        return component != null && existsMethodInClass(component.getClass(), "setText", String.class);
    }

    /**
     * Automatically inserts the translated text into the components that need
     * it. This method checks each registered component and updates it with the
     * translated text.
     */
    private static void autoInsertLanguage() {
        if (!appliedComponents.isEmpty()) {
            for (HashMap<String, Object> map : appliedComponents) {
                Object component = map.get("component");
                String lang_val = (String) map.get("language_value");
                //sSystem.err.println(component.getClass());
                if (component != null && existsMethodInClass(component.getClass(), "setText", String.class)) {
                    try {
                        Method setTextMethod = component.getClass().getMethod("setText", String.class);
                        setTextMethod.invoke(component, get(lang_val));
                    } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                        if (isDebugMode()) {
                            System.err.println("LanguageSystemDebugOutput\n");
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (isDebugMode()) {
                        System.err.println("LanguageSystemDebugOutput\nError: Method 'setText(String)' not found in component " + component.toString() + ".");
                    }

                }
            }
        }
    }

    /**
     * Retrieves the translated value for a given key.
     *
     * @param key the translation key.
     * @return the translated string, or null if the key is not found.
     */
    public static String get(String key) {
        return LanguageReader.getValue(key, null);
    }

    /**
     * Retrieves the translated value for a given key, with an optional default
     * value.
     *
     * @param key the translation key.
     * @param default_value the value to return if the key is not found.
     * @return the translated string, or the default value if the key is not
     * found.
     */
    public static String get(String key, String default_value) {
        return LanguageReader.getValue(key, default_value);
    }

    /**
     * Retrieves and formats the translated string for a given key, replacing
     * placeholders with values.
     *
     * @param key the translation key.
     * @param values the values to replace in the placeholders.
     * @return the formatted translated string.
     */
    public static String getf(String key, String... values) {
        return LanguageFormatter.format(LanguageReader.getValue(key, "null"), values);
    }

    /**
     * Formats a string with placeholders replaced by the provided values.
     *
     * @param text the string to be formatted.
     * @param values the values to replace in the placeholders.
     * @return the formatted string.
     */
    public static String format(String text, String... values) {
        if (text == null) {
            return "null";
        }
        return LanguageFormatter.format(text, values);
    }

    /**
     * Checks if a string can be formatted by checking if it contains
     * placeholders.
     *
     * @param text the string to check.
     * @return true if the string contains placeholders, false otherwise.
     */
    public static boolean isFormatable(String text) {
        return LanguageFormatter.hasPlaceholders(text);
    }

    /**
     * Checks if a method exists in a class with the given name and parameter
     * types.
     *
     * @param clazz the class to check.
     * @param methodName the method name.
     * @param paramTypes the parameter types of the method.
     * @return true if the method exists, false otherwise.
     */
    private static boolean existsMethodInClass(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            Method method = clazz.getMethod(methodName, paramTypes);
            return method != null;
        } catch (NoSuchMethodException e) {
            return false;
        }

    }

    /**
     * Sets the debug mode to enable or disable debug messages.
     *
     * @param _debug true to enable debug mode, false to disable it.
     */
    public static void setDebugMode(boolean _debug) {
        debug = _debug;
    }

    /**
     * Checks if the debug mode is enabled.
     *
     * @return true if debug mode is enabled, false otherwise.
     */
    public static boolean isDebugMode() {
        return debug;
    }

    /**
     * Checks if the specified language exists in the system.
     *
     * @param language the language code to check.
     * @return true if the language exists, false otherwise.
     */
    public static boolean existsLanguage(String language) {
        return LanguageReader.existsLanguage(language);
    }

    /**
     * Checks if the specified key exists in the system.
     *
     * @param key the key to check.
     * @return true if the key(translation_key) exists, false otherwise.
     */
    public static boolean existsKey(String key) {
        return get(key, null) != null;
    }

    /**
     * Retrieves the default system language based on the user's locale.
     *
     * @return the system's default language in English.
     */
    public static String getDefaultSystemLanguage() {
        Locale currentLocale = Locale.getDefault();
        String language = currentLocale.getDisplayLanguage(Locale.ENGLISH);

        return language.toLowerCase();
    }

    public static class Builder extends mz.cassamo.jls.Builder {

        public Builder() {
        super();
        }
        
    }

}
