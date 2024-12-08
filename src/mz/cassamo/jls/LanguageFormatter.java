package mz.cassamo.jls;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Utility class for formatting strings with placeholders in the format $1, $2, etc.
 * Provides methods to detect, count, and replace placeholders with supplied values.
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * String template = "Hello $1, welcome to $2!";
 * String result = LanguageFormatter.format(template, "Alice", "Wonderland");
 * // Result: "Hello Alice, welcome to Wonderland!"
 * }</pre>
 * 
 * @author Cassamo
 * @version 1.0
 * @since 2024
 */
class LanguageFormatter {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$(\\d+)");

     /**
     * Checks if the provided text contains any placeholders.
     * 
     * @param text the text to check for placeholders
     * @return {@code true} if placeholders are found; {@code false} otherwise
     */
    public static boolean hasPlaceholders(String text) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        return matcher.find();
    }

     /**
     * Counts the number of placeholders in the given text.
     * 
     * @param text the text to count placeholders in
     * @return the number of placeholders found
     */
    public static int countPlaceholders(String text) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

        /**
     * Formats the given text by replacing placeholders with the corresponding values.
     * 
     * @param text the text containing placeholders in the format $1, $2, etc.
     * @param values the values to replace placeholders with
     * @return the formatted text with placeholders replaced
     * @throws NumberFormatException if a placeholder cannot be parsed as an integer
     */
    public static String format(String text, String... values) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            int index = Integer.parseInt(matcher.group(1)) - 1;
            String replacement = (index >= 0 && index < values.length) ? values[index] : matcher.group(0);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
