
package test;

import java.io.File;

import mz.cassamo.jls.LanguageSystem;
import mz.cassamo.jls.exceptions.BuilderParseException;

/**
 *
 * @author cassamo
 */
public class ExampleBuildingLS {
    
 
public static void main(String[] args) {
    LanguageSystem.Builder builder = new LanguageSystem.Builder();
    

  
    try {
    	// If the file doesn't exist, it will be created during the save step
		builder.loadFromFile("test/languages.xml");
		// Add new languages to the language system
	    builder.putLanguage("english");
	    builder.putLanguage("portuguese");    
	    // Add translations for the "hello_world" key in both languages.
	    builder.putTranslation("english", "hello_world", "Hello, World!");
	    builder.putTranslation("portuguese", "hello_world", "Olá, Mundo!");
	    
	    // Save the updated translations to the same file.
	    builder.save();
	} catch (BuilderParseException e) {
			e.printStackTrace();
	}
    
    
}

    
}
