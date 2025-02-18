package test;

import mz.cassamo.jls.LanguageSystem;

/**
 * Using LanguageSystem from an XML in resources.
 */
public class ExampleFromRes {

    public static void main(String[] args) {

        // Path to XML file
        // Note: The path must be internal (from your project resources)
        // Example: The current example uses internal path language
        // The languages.xml file is stored on package mz.cassamo.ls.resources
        String xmlFilePath = "mz/cassamo/ls/resources/languages.xml";
        // Initialize with 'english' language
        // Note: The 'english' language must be included on languages.xml file
        LanguageSystem.initializeFromResources("english", ExampleFromRes.class, xmlFilePath);

        // Display the current language
        System.out.println("Current language: " + LanguageSystem.getCurrentLanguage());
        // Display translation for "hello_world"
        // Note: The "hello_world" translation key must be included on languages.xml
        // file
        System.out.println(LanguageSystem.get("hello_world"));

        // Switch to 'portuguese' language
        // Note: The "hello_world" translation key must be included on languages.xml
        // file
        LanguageSystem.setCurrentLanguage("portuguese");

        System.out.println("Current language: " + LanguageSystem.getCurrentLanguage());
        // Display translation for "hello_world" in portugues
        System.out.println(LanguageSystem.get("hello_world"));
        // Displays the translation of the verb "read" in its past participle in
        // Portuguese
        System.out.println(LanguageSystem.getWord("read", "participle"));

        // Displays the translation for "multi_line_message" in portugues
        System.out.println(LanguageSystem.getf("multi_line_message", "Kelven", "kelvencassamo9@gmail.com"));
    }
}
