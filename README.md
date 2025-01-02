

# Java Language System (JLS)

The **Java Language System (JLS)** is a Java library designed to facilitate the internationalization (i18n) of applications, enabling dynamic language management and text translation, **Swing** components, and **Widgets** on **Android**, based on XML files.

With JLS, you can:

- Initialize languages from external files or project resources.
- Switch between languages at runtime, ensuring that texts and components (or widgets) are automatically updated.
- Support multiple languages without changing the application’s source code.
- Use dynamic placeholders (`$1`, `$2`, etc.) to create personalized messages.
- Easily integrate translations into both desktop and mobile applications.

### **1. Installation**

The **Java Language System (JLS)** can be easily integrated into your Java project, either by downloading the JAR file directly or by cloning the repository and compiling the source code.

#### **1.1 Downloading the JAR**

You can find the latest version of JLS in JAR format in the **Releases** section on GitHub. To do this, visit the releases page and download the JAR file:

- **Find the latest version of the JAR here**: [JLS Releases Page](https://github.com/KelvenCassamo/java-language-system/releases)
- Or, you can download the latest compiled JAR directly from the link below:
 [📦 Download the latest compiled JAR](https://github.com/KelvenCassamo/java-language-system/releases/download/v1.0.0-beta/java-language-system-1.0.0.jar)


### 2. Main Features

The **Java Language System (JLS)** offers various features to facilitate the internationalization and translation of applications. 

#### **2.1 Creating the translations**

To configure the language system, start by creating translations in an XML file. Below is an example of a `languages.xml` file with translations in, for example **English** and **Portuguese**.
````xml
<languages>
    <language value="english">
        <translated value="hello_world">
            <value>Hello, World!</value>
        </translated>
        <translated value="presentation">
            <value>My name is $1!</value>
        </translated>
    </language>

    <language value="portuguese">
        <translated value="hello_world">
            <value>Olá, Mundo!</value>
        </translated>
        <translated value="presentation">
            <value>O meu nome é $1!</value>
        </translated>
    </language>
</languages>
````
-   **Language keys** (e.g., `hello_world`, `presentation`) are identifiers for the text.
-   **Placeholders** (e.g., `$1`) can dynamically insert values into the text.

#### **2.2. Structure of a `languages.xml` File**

The `languages.xml` file contains translations for various languages and their respective text values. Below is a detailed table explaining each element and attribute in the file.


| **Element/Attribute** | **Description**                                                                                     | **Attributes**                      | **Example**                                    |
|------------------------|----------------------------------------------------------------------------------------------------|--------------------------------------|------------------------------------------------|
| `<languages>`          | Root element that contains all language definitions.                                               | None                                 | `<languages> ... </languages>`                 |
| `<language>`           | Represents a specific language and contains its translations.                                      | `value` (language code)              | `<language value="english"> ... </language>`   |
| `<translated>`         | Defines a single translation entry, identified by a unique key.                                     | `value` (translation key)            | `<translated value="hello_world"> ... </translated>` |
| `<value>`              | Specifies the actual translation text for a given key.                                             | None                                 | `<value>Hello, World!</value>`                 |



#### **2.3. Attributes Details**

#### `<language>` Attributes
- **`value`**: The language code or name (e.g., "english", "portuguese"). This helps in identifying the language.

#### `<translated>` Attributes
- **`value`**: The unique key for the translation (e.g., "hello_world", "presentation"). This key is used to reference the translation programmatically.



#### **2.4. Language Initialization from Files or Resources**

JLS allows you to initialize the language system in two ways:

- **From external XML files**, which is useful when you have translation files outside your source code (e.g., in a directory or location accessible by the application).
- **From internal project resources**, making it easy to include the XML file directly within the app package without the need for external access.

**Example of initialization from an external file:**
```java
LanguageSystem.initializeFromFile("english", "path/to/languages.xml");
```
**Example of initialization from an internal resource:**
````java
LanguageSystem.initializeFromResources("english", ExampleFromRes.class, "path/to/resources/languages.xml");
````

#### **2.5. Dynamic Language Change**

With JLS, you can change the application’s language at runtime, allowing texts and interface components to be automatically updated to the new language. This is done using the `setCurrentLanguage` method, passing the desired language code.

**Example of language change:**
````java
LanguageSystem.setCurrentLanguage("portuguese");
````

When changing the language, JLS automatically translates all registered components to the new language.

#### **2.6. Automatic Component Translation**

The system supports the automatic translation of graphical interface components, such as **Swing** components (e.g., `JLabel`, `JButton`) and **Widgets** (e.g., `TextView`, `Button`, `EditText`, and others with the method **setText**) on Android, using predefined translation keys. You can associate a translation key with a component, and it will be automatically updated when the language is changed.

#### **Example of automatic translation for Swing components:**
````java
JLabel label = new JLabel();
LanguageSystem.autoTranslateComponent(label, "hello_world");
````

**Important Note:** The `autoTranslateComponent` method should be called only once for each component. In the example above, the text of the `JLabel` will be automatically translated based on the selected language.

#### **Example of automatic translation for Android widgets:**
````java
TextView textView = new TextView();
LanguageSystem.autoTranslateView(textView, "hello_world");
````

**Important Note:** The `autoTranslateView` method should be called only once for each widget. In the example above, the text of the `TextView` will be automatically translated based on the selected language.

**Alternatives for components and widgets:** As an alternative, you can use the `get` method of the `LanguageSystem` class, for example:

**For widgets:**
````java
TextView textView = new TextView();
textView.setText(LanguageSystem.get("hello_world"));
````

**For components:**
````java
JLabel label = new JLabel();
label.setText(LanguageSystem.get("hello_world"));
````

#### **2.7. Support for Dynamic Placeholders**

JLS supports the use of **dynamic placeholders** in translations, allowing variable values to be inserted into messages. This is useful for personalized messages, such as greetings or warnings, where parts of the text can change depending on the context.

Placeholders are represented by **$1**, **$2**, etc., in the translation file. You can replace these placeholders with specific values when calling the translation.

#### **Example of translation with placeholders:**
````xml
<translated value="presentation">
    <value>My name is $1!</value>
</translated>
````

**Example of usage in code:**
````java
String translatedText = LanguageSystem.getf("presentation", "Cassamo");
````
The result would be: `"My name is Cassamo!"`.

**Note:** The `LanguageSystem.getf(translation_key, ...placeholder_replacement)` method does not guarantee that the `translation_key` exists, and if it does not exist, it will return null. To avoid this result, you can check if the translation is available by calling the `LanguageSystem.canTranslate(translation_key)` method, which will return a boolean.

As an alternative, you can use the `LanguageSystem.format(text_content, ...placeholder_replacement)` method in conjunction with `LanguageSystem.get(translation_key, default_value)` to ensure it never returns null. Example:
````java
String translatedText = LanguageSystem.format(LanguageSystem.get("presentation", "My name is $1"), "Cassamo");
````

#### **2.8. Support for Multiple Languages**

JLS allows the inclusion of multiple languages in a single XML file, and switching between them can be done without altering the application’s source code. This makes it easy to add new languages and maintain translations in multilingual applications.

#### **Example of translation in multiple languages in XML:**
````xml
<language value="english">
    <translated value="hello_world">
        <value>Hello, World!</value>
    </translated>
</language>

<language value="portuguese">
    <translated value="hello_world">
        <value>Olá, mundo!</value>
    </translated>
</language>
````


### 3. Practical Example
Let's see a practical example of how to integrate the **Java Language System (JLS)** into a Java application **(Normal, Desktop and Android)**, for both configuring and switching languages, and for translating texts and displaying dynamic information with placeholders.

#### **3.1. Initial Setup**

First, let's configure the language system by loading the translations from an XML file or internal resources. Suppose we have a `languages.xml` file containing translations in English and Portuguese.
````xml
<languages>
    <language value="english">
        <translated value="hello_world">
            <value>Hello, World!</value>
        </translated>
        <translated value="presentation">
            <value>My name is $1!</value>
        </translated>
    </language>

    <language value="portuguese">
        <translated value="hello_world">
            <value>Olá, Mundo!</value>
        </translated>
        <translated value="presentation">
            <value>O meu nome é $1!</value>
        </translated>
    </language>
</languages>
````


#### **3.2. Initializing the Language System**

Now, let's set up the **Java Language System (JLS)** to use the XML file with translations and define an initial language (e.g., English).

#### Without graphical interface
````java
import mz.cassamo.jls.LanguageSystem;

public class Example {

    public static void main(String[] args) {

        // Initializing the language system with the "english" language from an XML file
        String xmlFilePath = "languages.xml";
        LanguageSystem.initializeFromFile("english", xmlFilePath);

        // Displaying the translation for the "hello_world" key
        System.out.println(LanguageSystem.get("hello_world"));  // Output: Hello, World!

        // Displaying the translation for the "presentation" key with placeholder
        System.out.println(LanguageSystem.getf("presentation", "Cassamo"));  // Output: My name is Cassamo!
    }
}

````

#### **3.4. Using Placeholders**

**JLS** also allows the use of **placeholders** to insert dynamic values into translations. For example, in the `"presentation"` key, we can insert a dynamic name using `$1` as a placeholder.

````java
public class ExampleWithPlaceholders {

    public static void main(String[] args) {

        // Initializing the language system with the "english" language
        String xmlFilePath = "languages.xml";
        LanguageSystem.initializeFromFile("english", xmlFilePath);

        // Displaying a translation with a placeholder
        String name = "Cassamo";
        System.out.println(LanguageSystem.getf("presentation", name));  // Output: My name is Cassamo!
    }
}

````




#### 3.5. In Java Swing
````java
import mz.cassamo.jls.LanguageSystem;
import javax.swing.*;
import java.awt.*;

public class ExampleWithUI {

    public static void main(String[] args) {

        // Initializing the language system with the "english" language from an XML file
        String xmlFilePath = "languages.xml";
        LanguageSystem.initializeFromFile("english", xmlFilePath);

        // Creating a simple graphical interface with Swing
        JFrame frame = new JFrame("Language Switch Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JLabel label = new JLabel(LanguageSystem.get("hello_world"));
        JButton changeLanguageButton = new JButton("Change Language");

        // Button to switch between languages
        changeLanguageButton.addActionListener(e -> {
            String currentLanguage = LanguageSystem.getCurrentLanguage();
            if ("english".equals(currentLanguage)) {
                LanguageSystem.setCurrentLanguage("portuguese");
            } else {
                LanguageSystem.setCurrentLanguage("english");
            }
            label.setText(LanguageSystem.get("hello_world"));
        });

        frame.setLayout(new FlowLayout());
        frame.add(label);
        frame.add(changeLanguageButton);

        frame.setVisible(true);
    }
}
````
If you want the label text to change automatically, you can simply call the `autoTranslateComponent` method. See below:
````java
import mz.cassamo.jls.LanguageSystem;
import javax.swing.*;
import java.awt.*;

public class ExampleWithUI {

    public static void main(String[] args) {

        // Initializing the language system with the "english" language from an XML file
        String xmlFilePath = "languages.xml";
        LanguageSystem.initializeFromFile("english", xmlFilePath);

        // Creating a simple graphical interface with Swing
        JFrame frame = new JFrame("Language Switch Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JLabel label = new JLabel(LanguageSystem.get("hello_world"));
        JButton changeLanguageButton = new JButton("Change Language");

        // Automatically translate components
        // To automatically translate components, you can use the autoTranslateComponents method
        LanguageSystem.autoTranslateComponents("hello_world", label, button, textArea);

        // Button to switch between languages
        changeLanguageButton.addActionListener(e -> {
            String currentLanguage = LanguageSystem.getCurrentLanguage();
            if ("english".equals(currentLanguage)) {
                LanguageSystem.setCurrentLanguage("portuguese");
            } else {
                LanguageSystem.setCurrentLanguage("english");
            }
            label.setText(LanguageSystem.get("hello_world"));
        });

        frame.setLayout(new FlowLayout());
        frame.add(label);
        frame.add(changeLanguageButton);

        frame.setVisible(true);
    }
}

````

#### 3.6. In Android
```java
package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import mz.cassamo.jls.LanguageSystem;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button changeLanguageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing the language system with the "english" language from an XML file
        String xmlFilePath = getFilesDir() + "/languages.xml"; // Ensure the XML file is in the correct directory
        LanguageSystem.initializeFromFile("english", xmlFilePath);

        textView = findViewById(R.id.textView);
        changeLanguageButton = findViewById(R.id.button);

        // Setting the initial text of the TextView
        textView.setText(LanguageSystem.get("hello_world"));

        // Button to switch between languages
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentLanguage = LanguageSystem.getCurrentLanguage();
                if ("english".equals(currentLanguage)) {
                    LanguageSystem.setCurrentLanguage("portuguese");
                } else {
                    LanguageSystem.setCurrentLanguage("english");
                }
                textView.setText(LanguageSystem.get("hello_world"));
            }
        });
    }
}

````
Unfortunately, for Android, it is not yet possible to initialize from Assets. This will be implemented soon.


### **4. System Requirements**

The **Java Language System (JLS)** is designed to be a lightweight and easy-to-integrate solution for different types of Java applications. Below are the system requirements and dependencies needed to use JLS.

#### **4.2. Software Requirements**

- **Java Runtime Environment (JRE)**:
    - JRE 8 or higher.
    - For development, it is recommended to use **Java Development Kit (JDK)** version 8 or higher.

#### **4.3. Platform Requirements**

The **Java Language System (JLS)** is a pure Java library, so it can be used on any platform that supports Java, including:

- **Development Environments**:
    
    - **Recommended IDEs**: IntelliJ IDEA, Eclipse, NetBeans, and Android Studio.
    - It can be used in any code editor, like VS Code, as long as the Java environment is properly set up.

#### **4.4. Application Compatibility**

- **Desktop Applications**: JLS can be easily used in Java desktop applications that utilize **Swing**.
- **Android Applications**: JLS is also compatible with Android, allowing translation of **Widgets** and texts at runtime. For integration into Android, it is necessary to set up the Android Studio development environment.


### 5. Public Methods in `LanguageSystem`

List of some public methods available in the `LanguageSystem` class. You will find details about initializing and managing supported languages.


### **5.1. Language Management**

| **Method**                                              | **Description**                                                                                           | **Parameters**                                                                                                                   | **Returns**                    |
|---------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|--------------------------------|
| `void addLanguageSystemInterface(LanguageSystemInterface _li)` | Registers a listener to receive notifications when the language changes.                                  | `_li`: Implementation of `LanguageSystemInterface`.                                                                             | N/A                            |
| `void initializeFromFile(String default_language, String xml_file_path)` | Initializes translations from an external XML file.                                                       | `default_language`: Default language code. <br> `xml_file_path`: Path to the XML file.                                          | N/A                            |
| `void initializeFromResources(String default_language, Class _class, String xml_file_path)` | Initializes translations from a resource file in the classpath.                                           | `default_language`: Default language code. <br> `_class`: Class used to load the resource. <br> `xml_file_path`: Resource path. | N/A                            |
| `String getCurrentLanguage()`                          | Returns the current active language.                                                                      | None                                                                                                                            | Current language code.         |
| `ArrayList<String> getLanguages()`                     | Retrieves a list of all available language codes.                                                         | None                                                                                                                            | List of language codes.         |
| `ArrayList<String> getTranslationKeys(String language)` | Retrieves all translation keys for a specific language.                                                   | `language`: Target language code.                                                                                               | List of translation keys.       |
| `void setCurrentLanguage(String language)`             | Sets the current language and updates all registered components.                                          | `language`: Language code to switch to.                                                                                        | N/A                            |
| `boolean existsLanguage(String language)`              | Checks if a specified language is available.                                                              | `language`: Language code to check.                                                                                            | `true` if the language exists. |
| `boolean existsKey(String key)`                        | Checks if a specific translation key exists in the system.                                                | `key`: Translation key to check.                                                                                               | `true` if the key exists.      |

---

### **5.2. Component Translation**

| **Method**                                              | **Description**                                                                                           | **Parameters**                                                                                      | **Returns**                    |
|---------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------|--------------------------------|
| `void autoTranslateComponent(Component component, String language_key)` | Translates a Swing component using the specified language key.                                            | `component`: Component to translate. <br> `language_key`: Translation key.                         | N/A                            |
| `void autoTranslateComponents(String language_key, Component... components)` | Translates multiple Swing components using the same language key.                                        | `language_key`: Translation key. <br> `components`: List of components to translate.                | N/A                            |
| `boolean canTranslateComponent(Component component)`   | Checks if a component is translatable (e.g., it has a `setText` method).                                  | `component`: Component to check.                                                                    | `true` if translatable.        |

---

### **5.3. String Translation**

| **Method**                                              | **Description**                                                                                           | **Parameters**                                                                                      | **Returns**                    |
|---------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------|--------------------------------|
| `String get(String key)`                               | Retrieves the translated value for a specific key.                                                        | `key`: Translation key.                                                                              | Translated string or `null`.   |
| `String get(String key, String default_value)`         | Retrieves the translation for a key, or a default value if the key is not found.                          | `key`: Translation key. <br> `default_value`: Fallback value.                                       | Translated string or fallback. |
| `String getf(String key, String... values)`            | Retrieves and formats a translation string, replacing placeholders with values.                           | `key`: Translation key. <br> `values`: Values to replace placeholders.                              | Formatted translated string.   |

---

### **5.4. String Formatting**

| **Method**                                              | **Description**                                                                                           | **Parameters**                                                                                      | **Returns**                    |
|---------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------|--------------------------------|
| `String format(String text, String... values)`         | Formats a string by replacing placeholders with specified values.                                         | `text`: String to format. <br> `values`: Values to insert into placeholders.                        | Formatted string.              |
| `boolean isFormatable(String text)`                    | Checks if a string contains placeholders for formatting.                                                  | `text`: String to evaluate.                                                                         | `true` if formatable.          |

---

### **5.5. Debugging**

| **Method**                                              | **Description**                                                                                           | **Parameters**                                                                                      | **Returns**                    |
|---------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------|--------------------------------|
| `void setDebugMode(boolean _debug)`                    | Enables or disables debug mode.                                                                           | `_debug`: `true` to enable debug mode, `false` to disable it.                                        | N/A                            |
| `boolean isDebugMode()`                                | Checks if debug mode is currently enabled.                                                                | None                                                                                               | `true` if debug mode is on.    |







### **6. Contribution and Support**

#### **6.1 How to Contribute**

The **Java Language System (JLS)** is an open-source project, and we are always open to community contributions! If you wish to contribute to the development and improvement of the library, follow the steps below:

1. **Fork the repository**: Click "Fork" on GitHub to create a copy of the repository in your own account.
2. **Clone the repository**: Clone the repository to your computer with the command:
````bash
git clone https://github.com/KelvenCassamo/java-language-system.git
````

**Create a branch for your feature**: Before making changes, create a new branch with a descriptive name:
````bash
git checkout -b feature-name
````

4.  **Make the changes**: Make the desired modifications to the code.
5.  **Add tests** (if necessary): If your contribution includes significant changes, write tests to ensure the code functions as expected.    
6.  **Commit your changes**:
````bash
git add .
git commit -m "Description of the changes made"
````

7. **Push to the repository**:
````bash
git push origin feature-name
````

8.  **Open a Pull Request**: On GitHub, open a Pull Request to the main repository with a clear description of your changes.
    

#### **6.2 Support**

If you encounter issues using the **Java Language System (JLS)** or need assistance, there are several ways to get support:

1.  **Issues on GitHub**:
    
    -   If you find a bug or have a question, open an "Issue" on the [GitHub repository](https://github.com/KelvenCassamo/java-language-system/issues) so that I or other developers can help.
        
    -   Be as specific as possible when describing the problem. Include error logs, screenshots, and expected behavior.
        
-   **Forums and Communities**:
    
    -   **Stack Overflow**: Ask or search for issues related to JLS with the tags `#jls` or `#java-language-system`.
        
    -   **Reddit**: Participate in discussions about software internationalization and Java on the [subreddit r/java](https://www.reddit.com/r/java/).


**Email Support**:

-   If you need more direct support, send an email to my address: `kelvencassamo9@gmail.com`.


