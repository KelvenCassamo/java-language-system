package test;

import mz.cassamo.jls.LanguageSystem;

import javax.swing.*;
import java.awt.*;

/**
 * Using LanguageSystem to auto-translate components.
 */
public class ExampleToComponents {

    public static void main(String[] args) {

       //Path to XML file
        //Note: The path must be external
        //Example: The current example uses external path language
        String xmlFilePath = "languages.xml";

        // Initialize with 'english' language
        //Note: The 'english' language must be included on languages.xml file
        LanguageSystem.initializeFromFile("english", xmlFilePath);
        
        
        // Add language change notification
        LanguageSystem.addLanguageSystemInterface(new LanguageSystem.LanguageSystemInterface() {
            @Override
            public void onChange(String current_language) {
                System.out.println("Language changed to: " + current_language); // Display changed language
            }
        });

       
        JFrame frame = new JFrame("Example with components");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        
        JLabel label = new JLabel();
        JButton button = new JButton();
        JTextArea textArea = new JTextArea(5, 20);

        // Automatically translate components
        // To automatically translate components, you can use the autoTranslateComponents
        LanguageSystem.autoTranslateComponents("hello_world", label, button, textArea);
        LanguageSystem.autoTranslateComponent(button, "presentation");

        button.setText(LanguageSystem.getf("presentation", "Cassamo")); // Example of formatted text

        
        JButton changeLangButton = new JButton("Change Language");
        changeLangButton.addActionListener(e -> {
            // Switch between languages
            String currentLang = LanguageSystem.getCurrentLanguage();
            if ("english".equals(currentLang)) {
                LanguageSystem.setCurrentLanguage("portuguese");
            } else {
                LanguageSystem.setCurrentLanguage("english");
            }
        });

       
        frame.setLayout(new FlowLayout());
        frame.add(label);
        frame.add(button);
        frame.add(textArea);
        frame.add(changeLangButton);

        frame.setVisible(true); 
    }
}
