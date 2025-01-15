package test;

import mz.cassamo.jls.LanguageSystem;

/**
 * Using LanguageSystem from an XML file.
 */
public class ExampleFromFile {

    public static void main(String[] args) {
        //Path to XML file
        //Note: The path must be external
        //Example: The current example uses external path language
        String xmlFilePath = "languages.xml";

        // Initialize with 'english' language
        //Note: The 'english' language must be included on languages.xml file
        LanguageSystem.initializeFromFile("english", xmlFilePath);

        // Displays the current language
        System.out.println("Current language: " + LanguageSystem.getCurrentLanguage());
        // Displays the translation for "hello_world"
        //Note: The "hello_world" translation key must be included on languages.xml file
        System.out.println(LanguageSystem.get("hello_world"));

        // Switch to 'portuguese' language
        //Note: The "hello_world" translation key must be included on languages.xml file
        LanguageSystem.setCurrentLanguage("portuguese");

        System.out.println("Current language: " + LanguageSystem.getCurrentLanguage());
        // Displays the translation for "hello_world" in portugues
        System.out.println(LanguageSystem.get("hello_world"));

        // Displays the translation of the verb "read" in its past participle in Portuguese
        System.out.println(LanguageSystem.getWord("read", "participle"));

         // Displays the translation for "multi_line_message" in portugues
         System.out.println(LanguageSystem.getf("multi_line_message", "Kelven", "kelvencassamo9@gmail.com"));
        
    }
}
